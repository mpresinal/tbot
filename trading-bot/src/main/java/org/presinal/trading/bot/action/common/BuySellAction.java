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
package org.presinal.trading.bot.action.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.types.AccountBalance;
import org.presinal.market.client.types.AccountBalance.Balance;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Order;
import org.presinal.trading.bot.action.AbstractBotAction;

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

    @Override
    public String getContextKey() {
        return KEY;
    }

    @Override
    public void run() {
        logger.entering(CLASS_NAME, "run");
        boolean result;
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
                Object signalData = getContext().get(generatorOrderActionKey);
                logger.info("** signalData = " + signalData);
                
                if(signalData instanceof Order){
                    logger.info("Placing order");
                    result = placeOrder((Order)signalData);
                    logger.info("Order placed? "+result);
                }
                
                notifyListener();
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
                assetLostProfit.asset = order.getAssetPair();
                
                if( Order.SIDE_BUY.equals(order.getSide()) ) {
                    assetLostProfit.buyPrice = order.getPrice();
                    placedOrder = client.placeBuyOrder(order.getAssetPair(), order.getPrice(), quantity, order.getType());
                    
                } else if( Order.SIDE_SELL.equals(order.getSide()) ) {
                    assetLostProfit.sellPrice = order.getPrice();
                    placedOrder = client.placeSellOrder(order.getAssetPair(), order.getPrice(), quantity, order.getType());
                    
                    assetLostProfit.computeProfits();
                    logger.info("** profit = "+assetLostProfit.profit+", percentage = " + assetLostProfit.profitPercentage+", asset = "+order.getAssetPair());
                }
                
                if(placedOrder != null) {
                    ledger.put(assetLostProfit.asset.getBaseAsset(), assetLostProfit);
                    logger.info("Order placed successfully. Order id = " + placedOrder.getOrderId());

                    order.setExecutedQty(placedOrder.getExecutedQty());
                    order.setClientOrderId(placedOrder.getClientOrderId());
                    order.setOrderId(placedOrder.getOrderId());
                    order.setStatus(placedOrder.getStatus());
                    order.setTransactionTime(placedOrder.getTransactionTime());
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
    
    private static class AssetLostProfit {
        
        double buyPrice;
        double sellPrice;
        AssetPair asset;

        private double profit = -1;
        private double profitPercentage;
        
        public void computeProfits() {
            profit = (sellPrice - buyPrice);
            profitPercentage = (profit / buyPrice) * 100.0;
        }
        
        public boolean IsLoose() {
            return profit < 0;
        }
        
        public boolean isProfit() {
            return !IsLoose();
        }

        @Override
        public String toString() {
            return "AssetLostProfit{ asset=" + asset
                    + ", buyPrice=" + buyPrice 
                    + ", sellPrice=" + sellPrice 
                    + ", profit=" + profit 
                    + ", profitPercentage=" + profitPercentage + " }";
        }
    }
}
