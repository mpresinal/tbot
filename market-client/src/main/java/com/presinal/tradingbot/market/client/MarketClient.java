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

package com.presinal.tradingbot.market.client;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import com.presinal.tradingbot.market.client.enums.OrderStatus;
import com.presinal.tradingbot.market.client.enums.OrderType;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.AccountBalance;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.AssetPriceChange;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.market.client.types.OpenedOrder;
import com.presinal.tradingbot.market.client.types.Order;
import com.presinal.tradingbot.market.client.types.OrderBook;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public interface MarketClient extends Serializable {

    public static final int MAX_DATA_LIMIT = 500;
    
    public String getMarketName();
    
    /**
     * Make a test connection to validate the api url.
     * 
     * @return true is the api url is valid. false otherwise.
     * @throws com.presinal.tradingbot.market.client.MarketClientException
     */
    public boolean testConnection() throws MarketClientException;
    
    /**
     * Load the market trading rules
     */
    public void loadMarketInfo();
    
    public void loadRecentTrades();
    
    /**
     * Load the order book for a particular asset.
     * 
     * @param assetPair
     * @param limit
     * @return
     * @throws MarketClientException 
     */
    public OrderBook loadOrderBook(AssetPair assetPair, int limit) throws MarketClientException;
    
    /**
     * Load the candlestick information about particular asset.
     * 
     * @param assetPair
     * @param timeFrame
     * @param startDate
     * @param endDate
     * @param limit
     * @return
     * @throws MarketClientException 
     */
    public List<Candlestick> loadCandlestick(AssetPair assetPair, TimeFrame timeFrame, 
            Instant startDate, Instant endDate, int limit) throws MarketClientException;
       
    public List<AssetPriceChange> loadAssetsPriceChange() throws MarketClientException;
    
    public AssetPriceChange getAssetPriceChange(AssetPair asset) throws MarketClientException;
    
    public double getAssetPrice(AssetPair asset) throws MarketClientException;
    
    /**
     * Register a buy order in the market for a particular asset.
     * 
     * @param asset
     * @param price
     * @param quantity
     * @param type
     * @return
     * @throws MarketClientException 
     */
    public Order placeBuyOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException;
    
    public Order placeSellOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException;
    
    public OrderStatus getOrderStatus(AssetPair asset, String orderId) throws MarketClientException;
    
    public boolean cancelOrder(String orderId) throws MarketClientException;
    
    public OpenedOrder loadOpenedOrders(AssetPair asset) throws MarketClientException;
    
    public AccountBalance getAccountBalance()throws MarketClientException;
    
    default int getMaxDataLimit(){
        return MAX_DATA_LIMIT;
    }
}

