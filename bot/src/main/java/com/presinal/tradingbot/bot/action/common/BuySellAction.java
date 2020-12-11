/*
 * The MIT License
 *
 * Copyright 2018 Miguel Presinal.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.presinal.tradingbot.bot.action.common;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import com.presinal.tradingbot.market.client.MarketClient;
import com.presinal.tradingbot.market.client.MarketClientException;
import com.presinal.tradingbot.market.client.types.AccountBalance;
import com.presinal.tradingbot.market.client.types.Order;
import com.presinal.tradingbot.bot.action.AbstractBotAction;
import com.presinal.tradingbot.bot.action.BotAction;
import com.presinal.tradingbot.bot.util.AssetLostProfit;
import com.presinal.tradingbot.bot.util.ProfitLedgerFile;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class BuySellAction extends AbstractBotAction {

    private static final String CLASS_NAME = BuySellAction.class.getName();
    private static final Logger logger = Logger.getLogger(CLASS_NAME);    
    public static final String KEY = CLASS_NAME;;

    private boolean signalRecieved = false;

    // Action context key of the action that generated buy/sell orders
    private String generatorOrderActionKey;    
    private AccountBalance accountBalance;    
    private MarketClient client;
    
    private Map<String, AssetLostProfit> ledger;
    private ProfitLedgerFile ledgerFile;
    
    public BuySellAction(MarketClient client, BotAction generatorOrderAction) {
        this(client, generatorOrderAction.getContextKey());
    }
    
    public BuySellAction(MarketClient client, String generatorOrderActionKey) {
        super();
        this.client = client;
        this.generatorOrderActionKey=generatorOrderActionKey;
        
        ledger = new HashMap<>();
        setupLogger();
    }
    
    private void setupLogger() {
        
        logger.setLevel(Level.INFO);
        
        try {            
            FileHandler fileHandler = new FileHandler(BuySellAction.class.getSimpleName()+".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error adding File Handler. "+ex.getMessage(), ex);            
        }        
    }
    
    private void init(){
        try {
            ledgerFile = new ProfitLedgerFile(Paths.get("ledgers", client.getMarketName()));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not be posible to initialize Profit Ledger File. "+ex.getMessage(), ex);
        }
    }

    @Override
    public String getContextKey() {
        return KEY;
    }

    @Override
    public void run() {
        logger.entering(CLASS_NAME, "run");        
        
        init();
        
        boolean result;
        Order order = null;
        while (!isActionEnded()) {

            synchronized (this) {
                logger.info("Waiting for signal to place an order");

                while (!signalRecieved) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        logger.log(Level.SEVERE, "InterruptedException. "+ex.getMessage(), ex);
                    }
                }

                logger.info("signal's received!!!");
                
                signalRecieved = false;
                Object signalData = getContext().get(getSignalDataProducerKey());
                logger.info("** signalData = " + signalData);
                
                if(signalData instanceof Order){
                    logger.info("Placing order");
                    order = (Order)signalData;
                    result = placeOrder(order);
                    logger.info("Order placed? "+result);
                    
                    if(result){
                        
                        if(Order.SIDE_SELL.equals(order.getSide())){                            
                            getContext().put(KEY, order.getAssetPair());
                            notifyListener();
                        }                        
                    }
                }               
            }

        }
        
        logger.exiting(CLASS_NAME, "run");
    }

    private boolean placeOrder(Order order) {        
        double quantity = 0.0;
        try {
            
            /*Balance balance = accountBalance.getBalanceFor(order.getAssetPair().getQuoteAsset());
            double total = quantity * order.getPrice();
            
            // check if there is enough balance to place the order.
            if(balance != null && (balance.available > 0.0 && balance.available > total)){ */
    
                Order placedOrder = null;
                
                AssetLostProfit assetLostProfit =  ledger.getOrDefault(order.getAssetPair().getBaseAsset(), new AssetLostProfit());
                assetLostProfit.setAsset(order.getAssetPair());
                
                if( Order.SIDE_BUY.equals(order.getSide()) ) {
                    assetLostProfit.setBuyPrice(order.getPrice());
                    assetLostProfit.setBuyDate(new Date());
                    placedOrder = client.placeBuyOrder(order.getAssetPair(), order.getPrice(), quantity, order.getType());
                    
                } else if( Order.SIDE_SELL.equals(order.getSide()) ) {
                    assetLostProfit.setSellPrice(order.getPrice());
                    assetLostProfit.setSellDate(new Date());
                    placedOrder = client.placeSellOrder(order.getAssetPair(), order.getPrice(), quantity, order.getType());
                    
                    assetLostProfit.computeProfits();
                    logger.info("** profit = "+assetLostProfit.getProfit()+", percentage = " + assetLostProfit.getProfitPercentage()+", asset = "+order.getAssetPair());
                }
                
                if(placedOrder != null) {
                    ledger.put(assetLostProfit.getAsset().getBaseAsset(), assetLostProfit);
                    logger.info("Order placed successfully. Order id = " + placedOrder.getOrderId());

                    order.setExecutedQty(placedOrder.getExecutedQty());
                    order.setClientOrderId(placedOrder.getClientOrderId());
                    order.setOrderId(placedOrder.getOrderId());
                    order.setStatus(placedOrder.getStatus());
                    order.setTransactionTime(placedOrder.getTransactionTime());
                }
                
                // if a pair of buy/sell has been placed then write it in the ledger book
                if(assetLostProfit.getBuyDate() != null && assetLostProfit.getSellDate() != null) {
                    writeOnLedgerBook(assetLostProfit);
                }
                
            /*} else {
                logger.info("Not enough balance to place the order: "+order);
            } */
            
        } catch (MarketClientException ex) {
            logger.log(Level.SEVERE, "Error placing order. "+ex.getMessage(), ex);
            return false;
        }
        
        return true;
    }
    
    private void writeOnLedgerBook(AssetLostProfit assetLostProfit){
        try {
            logger.info("Writing entry on ledger book...");
            ledgerFile.writeEntry(assetLostProfit);
            logger.info("Writing entry on ledger book...OK");
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not write lost or profit on ledger book. "+ex.getMessage(), ex);
        }
    }   
    
    @Override
    public void notifySignal() {
        logger.entering(CLASS_NAME, "notifySignal()");
        logger.info("Signal notificacion received!!!");
        synchronized (this) {
            notifyAll();
            signalRecieved = true;
        }
        logger.exiting(CLASS_NAME, "notifySignal()");
    }
    
    private void updateLedger() {
        
    }
    
    public AccountBalance getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(AccountBalance accountBalance) {
        this.accountBalance = accountBalance;
    }
}
