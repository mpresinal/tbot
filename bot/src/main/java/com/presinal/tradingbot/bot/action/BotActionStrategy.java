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

package com.presinal.tradingbot.bot.action;

import com.presinal.tradingbot.market.client.enums.OrderType;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.Order;
import com.presinal.tradingbot.bot.strategy.Strategy;
import com.presinal.tradingbot.bot.strategy.StrategyFactory;
import com.presinal.tradingbot.bot.strategy.listener.TradingStrategyListener;
import java.util.HashMap;
import java.util.Map;


/**
 * Run a strategy when the asset selection action notify a new asset selected.
 * 
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class BotActionStrategy extends AbstractBotAction implements TradingStrategyListener {

    public static final String CONTEXT_KEY = BotActionStrategy.class.getSimpleName();
    private String name = CONTEXT_KEY;
    
    private boolean signalRecieved = false;
    private boolean running = false;
    private StrategyFactory strategyFactory;
    private Map<String, Strategy> strategyCache;

    public BotActionStrategy() {
        strategyCache = new HashMap<>();
    }
    
    
    public String getContextKey() {
        return CONTEXT_KEY;
    }
    
    @Override
    public void run() {
        System.out.println(name + " :: performeAction() Enter");
        Strategy strategy;
        AssetPair asset;
        while (!isActionEnded()) {

            synchronized (this) {
                System.out.println(name + " :: performeAction() Waiting for signal to place an order");

                while (!signalRecieved) {
                    try {
                        wait();
                    } catch (InterruptedException ex) { 
                        ex.printStackTrace(); 
                    }
                }

                signalRecieved = false;
                Object signalData = getContext().get(getSignalDataProducerKey());
                System.out.println(name + " :: performeAction() Executing task");
                System.out.println(name + " :: performeAction() signalData = " + signalData);

                if (signalData instanceof AssetPair) {
                    asset = (AssetPair) signalData;
                    strategy = strategyCache.computeIfAbsent(asset.toSymbol(), key -> strategyFactory.newStrategy());
                    strategy.setContext(getContext());
                    System.out.println(name + " :: performeAction() strategy = "+strategy);
                    strategy.setAsset(asset);
                    strategy.setListener(this);                    
                    new Thread(strategy).start();
                }

            }
        }

        System.out.println(name + " :: performeAction() Exit");
    }

    @Override
    public void notifySignal() {
        System.out.println(name + " :: update() Enter");
        synchronized (this) {
            notifyAll();
            signalRecieved = true;
        }
        System.out.println(name + " :: update() Exit");
    }

    @Override
    public void onBuySignal(AssetPair asset, double price) {
        System.out.println("buy signal: asset = " + asset + ", price=" + price);
        createOrderAndNotify(asset, price, Order.SIDE_BUY);   
    }

    @Override
    public void onSellSignal(AssetPair asset, double price) {
        System.out.println("sell signal: asset = " + asset + ", price=" + price);
        createOrderAndNotify(asset, price, Order.SIDE_SELL);
    }
    
    private void createOrderAndNotify(AssetPair asset, double price, String side) {        
        Order order = createOrder(asset, price, Order.SIDE_SELL);
        getContext().put(getContextKey(), order);        
        notifyListener();
    }
    
    private Order createOrder(AssetPair asset, double price, String side) {
        Order order = new Order();
        order.setAssetPair(asset);
        order.setPrice(price);
        order.setSide(side);
        order.setType(OrderType.LIMIT);
        return order;
    }

    public StrategyFactory getStrategyFactory() {
        return strategyFactory;
    }

    public void setStrategyFactory(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }
}
