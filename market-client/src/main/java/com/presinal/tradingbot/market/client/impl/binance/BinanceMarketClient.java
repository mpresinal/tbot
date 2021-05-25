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

package com.presinal.tradingbot.market.client.impl.binance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import com.presinal.tradingbot.market.client.AbstractMarketClient;
import com.presinal.tradingbot.market.client.MarketClientException;
import com.presinal.tradingbot.market.client.enums.OrderStatus;
import com.presinal.tradingbot.market.client.enums.OrderType;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.impl.binance.deserializer.*;
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
public class BinanceMarketClient extends AbstractMarketClient {

    private static final String MARKET_NAME = "binance";
    
    public static final String API_URL = "https://api.binance.com/api/";    
    private static final String OPEN_TICK_ENDPOINT = "v1/ticker/24hr";
    private static final String CANDLESTICK_ENDPOINT = "v1/klines";

    private static final int ORDER_BOOK_LIMIT = 100;    
    private static final String SYMBOL_SEPERATOR = "";
    
    public BinanceMarketClient(String apiURL, String apiKey, String secretKey) throws MarketClientException {
        super(apiURL, apiKey, secretKey);
    }

    @Override
    public void registerTypeDeserializers(GsonBuilder builder) {
        builder.registerTypeAdapter(AssetPriceChange.class, new AssetPriceChangeDeserializer());
        builder.registerTypeAdapter(Candlestick[].class, new CandlestickDeserializer());
    }

    public String getMarketName() {
        return MARKET_NAME;
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
    public List<Candlestick> loadCandlestick(AssetPair assetPair, TimeFrame timeFrame, Instant startDate, Instant endDate, int limit) throws MarketClientException {
        
        TimeFrame interval = timeFrame == null? TimeFrame.EIGHT_HOURS : timeFrame;
        
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("symbol", assetPair.toSymbol(SYMBOL_SEPERATOR));
        paramMap.put("interval", interval.getTimeLabeled());
        paramMap.put("limit", limit);
        
        /*
        if(startDate != null){
            paramMap.put("startTime", startDate.getEpochSecond());
        }
        
        if(endDate != null){
            paramMap.put("endTime", endDate.getEpochSecond());
        }*/
        
        String response = doGetRequest(CANDLESTICK_ENDPOINT, paramMap);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        Candlestick[] candlesticks = gson.fromJson(el, Candlestick[].class);
        
        if(candlesticks != null){
            return Arrays.asList(candlesticks);
        }
        
        return new ArrayList<>();
    }

    @Override
    public List<AssetPriceChange> loadAssetsPriceChange() throws MarketClientException {
        String response = doGetRequest(OPEN_TICK_ENDPOINT, null);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        AssetPriceChange[] assetsPriceChange = gson.fromJson(el.getAsJsonArray(), AssetPriceChange[].class);
        List<AssetPriceChange> list = Arrays.asList(assetsPriceChange);
        return list;
    }

    @Override
    public AssetPriceChange getAssetPriceChange(AssetPair asset) throws MarketClientException {
        
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("symbol", asset.toSymbol(SYMBOL_SEPERATOR));
        
        String response = doGetRequest(OPEN_TICK_ENDPOINT, paramMap);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        AssetPriceChange assetPriceChange = gson.fromJson(el.getAsJsonObject(), AssetPriceChange.class);
        
        return assetPriceChange;
    }

    @Override
    public double getAssetPrice(AssetPair asset) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Order placeBuyOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException {
        // delete after test
        Order order = new Order();
        order.setOrderId(Long.toString(new Date().getTime()));
        order.setAssetPair(asset);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setType(type);
        order.setTransactionTime(new Date());
        order.setExecutedQty(quantity);
        order.setClientOrderId(apiKey);
        return order;
    }

    @Override
    public Order placeSellOrder(AssetPair asset, double price, double quantity, OrderType type) throws MarketClientException {
        // delete after test
        Order order = new Order();
        order.setOrderId(Long.toString(new Date().getTime()));
        order.setAssetPair(asset);
        order.setPrice(price);
        order.setQuantity(quantity);
        order.setType(type);
        order.setTransactionTime(new Date());
        order.setExecutedQty(quantity);
        order.setClientOrderId(apiKey);
        return order;
    }

    @Override
    public OrderStatus getOrderStatus(AssetPair asset, String orderId) throws MarketClientException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean cancelOrder(String orderId) throws MarketClientException {
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
