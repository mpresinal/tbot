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

package org.presinal.market.client.impl.kucoin;

import java.util.Date;
import java.util.List;
import org.presinal.market.client.AbstractMarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.OrderStatus;
import org.presinal.market.client.enums.OrderType;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.types.AccountBalance;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.AssetPriceChange;
import org.presinal.market.client.types.Candlestick;
import org.presinal.market.client.types.OpenedOrder;
import org.presinal.market.client.types.Order;
import org.presinal.market.client.types.OrderBook;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class KucoinMarketClient extends AbstractMarketClient {

    public KucoinMarketClient(String apiURL, String apiKey, String secretKey) {
        super(apiURL, apiKey, secretKey);
    } 

    @Override
    public boolean testConnection() throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadMarketInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadRecentTrades() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderBook loadOrderBook(AssetPair assetPair, int limit) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Candlestick> loadCandlestick(AssetPair assetPair, TimeFrame timeFrame, Date startDate, Date endDate, int limit) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AssetPriceChange loadAssetPriceChange(AssetPair asset) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getAssetPrice(AssetPair asset) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order placeBuyOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order placeSellOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OrderStatus getOrderStatus(long orderId) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelOrder(long orderId) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OpenedOrder loadOpenedOrders(AssetPair asset) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AccountBalance getAccountBalance() throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
