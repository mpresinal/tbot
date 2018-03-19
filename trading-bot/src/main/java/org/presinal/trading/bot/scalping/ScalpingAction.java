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
package org.presinal.trading.bot.scalping;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.OrderType;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Order;
import org.presinal.trading.bot.action.AbstractBotAction;
import org.presinal.trading.bot.action.common.AssetSelectionAction;
import org.presinal.trading.bot.strategy.Signal;
import org.presinal.trading.bot.strategy.Strategy;
import org.presinal.trading.bot.strategy.listener.TradingStrategyListener;
import org.presinal.trading.bot.strategy.scalping.ScalpingStrategy;
import org.presinal.trading.bot.strategy.scalping.ScalpingStrategyConfig;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class ScalpingAction extends AbstractBotAction implements TradingStrategyListener {

    public static final String KEY = ScalpingAction.class.getSimpleName();
    private String name = KEY;

    private boolean signalRecieved = false;
    private ScalpingStrategy strategy;
    private MarketClient client;

    private final ScalpingStrategyConfig strategyConfig;

    public ScalpingAction(MarketClient client) {
        super();
        this.client = client;

        strategyConfig = new ScalpingStrategyConfig();        
        strategyConfig.setTrendLineTimeFrame(TimeFrame.EIGHT_HOURS);
        strategyConfig.setTrendLinePeriod(ScalpingStrategyConfig.DEFAULT_TREND_LINE_PERIOD);
        
        strategyConfig.setIndicatorTimeFrame(TimeFrame.THIRTY_MINUTES);
        strategyConfig.setIncludeVolumeAverageCondition(false);
        strategyConfig.setVolumeIndicatorPeriod(10);
        strategyConfig.setIncludeTrendLineVerification(false);
    }

    @Override
    public String getContextKey() {
        return name;
    }

    @Override
    public void run() {
        System.out.println(name + " :: performeAction() Enter");

        while (!isActionEnded()) {

            synchronized (this) {
                System.out.println(name + " :: performeAction() Waiting for signal to place an order");

                while (!signalRecieved) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScalpingAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                signalRecieved = false;
                Object signalData = getContext().get(AssetSelectionAction.KEY);
                System.out.println(name + " :: performeAction() Executing task");
                System.out.println(name + " :: performeAction() signalData = " + signalData);

                if (signalData instanceof AssetPair) {
                    strategy = new ScalpingStrategy(client, (AssetPair) signalData, strategyConfig);
                    strategy.init();
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
        Order order = createOrder(asset, price, Order.SIDE_BUY);
        getContext().put(KEY, order);        
        notifyListener();
    }

    @Override
    public void onSellSignal(AssetPair asset, double price) {
        System.out.println("sell signal: asset = " + asset + ", price=" + price);
        Order order = createOrder(asset, price, Order.SIDE_SELL);
        getContext().put(KEY, order);        
        notifyListener();
    }

    @Override
    public void onSignal(Signal sginal, Strategy source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private Order createOrder(AssetPair asset, double price, String side) {
        Order order = new Order();
        order.setAssetPair(asset);
        order.setPrice(price);
        order.setSide(side);
        order.setType(OrderType.LIMIT);
        return order;
    }
}
