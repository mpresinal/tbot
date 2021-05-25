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

package com.presinal.tradingbot.market.client.impl.kucoin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import com.presinal.tradingbot.market.client.AbstractMarketClient;
import com.presinal.tradingbot.market.client.MarketClientException;
import com.presinal.tradingbot.market.client.enums.OrderStatus;
import com.presinal.tradingbot.market.client.enums.OrderType;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.impl.kucoin.deserializer.AccountBalanceDeserializer;
import com.presinal.tradingbot.market.client.impl.kucoin.deserializer.AssetPriceChangeDeserializer;
import com.presinal.tradingbot.market.client.impl.kucoin.deserializer.CandlestickDeserializer;
import com.presinal.tradingbot.market.client.impl.kucoin.deserializer.OrderBookDeserializer;
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
public class KucoinMarketClient extends AbstractMarketClient {
    
    private static final String MARKET_NAME ="kucoin";
    
    public static final String API_URL = "https://api.kucoin.com";
    private static final String ORDER_BOOK_ENDPOINT = "v1/open/orders";
    private static final String OPEN_TICK_ENDPOINT = "v1/open/tick";
    private static final String CANDLESTICK_TRV_VERSION_ENDPOINT = "v1/open/chart/history";
    
    
    private static final int ORDER_BOOK_LIMIT = 100;    
    private static final String SYMBOL_SEPERATOR = "-";
    
    public KucoinMarketClient(String apiURL, String apiKey, String secretKey) throws MarketClientException {
        super(apiURL, apiKey, secretKey);
    } 

    @Override
    public void registerTypeDeserializers(GsonBuilder builder){
        builder.registerTypeAdapter(AccountBalance.class, new AccountBalanceDeserializer());
        builder.registerTypeAdapter(OrderBook.class, new OrderBookDeserializer());
        builder.registerTypeAdapter(Candlestick[].class, new CandlestickDeserializer());
        builder.registerTypeAdapter(AssetPriceChange.class, new AssetPriceChangeDeserializer());
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
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("symbol", assetPair.toSymbol(SYMBOL_SEPERATOR));
        paramMap.put("limit", (limit <= 0? ORDER_BOOK_LIMIT : limit));
        
        String response = doGetRequest(ORDER_BOOK_ENDPOINT, paramMap);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        OrderBook orderBook = gson.fromJson(el.getAsJsonObject().get("data"), OrderBook.class);
        orderBook.setAssetPair(assetPair);
        return orderBook;
    }

    @Override
    public List<Candlestick> loadCandlestick(AssetPair assetPair, TimeFrame timeFrame, Instant startDate, Instant endDate, int limit) throws MarketClientException {
        // https://api.kucoin.com/v1/open/chart/history
        //
        /*
        System.out.println(" ****  assetPair = "+assetPair);
        System.out.println(" ****  timeFrame = "+timeFrame);
        System.out.println(" ****  startDate = "+startDate);
        System.out.println(" ****  endDate = "+endDate); */
                
        if( !isTimeFrameSupported(timeFrame)){
            throw new MarketClientException("Not supported time frame: "+timeFrame);
        }
        
        /*long fromTimeStamp = startDate.getTime() / TimeFrame.UNIX_FACTOR;
        long toTimeStamp = endDate.getTime() / TimeFrame.UNIX_FACTOR;
        */
        String resolution = null;
        
        if(timeFrame == TimeFrame.ONE_DAY){
            resolution = "D";
        } else if(timeFrame == TimeFrame.ONE_WEEK){
            resolution = "W";
        } else {
            resolution = Long.toString(timeFrame.toMinute());
        }
        
        TreeMap<String, Object> paramMap = new TreeMap<>();
        paramMap.put("symbol", assetPair.toSymbol(SYMBOL_SEPERATOR));
        paramMap.put("resolution", resolution);
        paramMap.put("from", startDate.getEpochSecond());
        paramMap.put("to", endDate.getEpochSecond());        
        
        //System.out.println(" ****  paramMap = "+paramMap);
        //System.out.println(" ****  endpoint = "+CANDLESTICK_TRV_VERSION_ENDPOINT);
        
        String response = doGetRequest(CANDLESTICK_TRV_VERSION_ENDPOINT, paramMap);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        Candlestick[] candlesticks = gson.fromJson(el, Candlestick[].class);
        return Arrays.asList(candlesticks);        
    }
    
    @Override
    public List<AssetPriceChange> loadAssetsPriceChange() throws MarketClientException {
                
        String response = doGetRequest(OPEN_TICK_ENDPOINT, null);
        Gson gson = getGson();
        JsonElement el = gson.fromJson(response, JsonElement.class);
        AssetPriceChange[] assetsPriceChange = gson.fromJson(el.getAsJsonObject().get("data").getAsJsonArray(), AssetPriceChange[].class);
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
        //
        AssetPriceChange price = getAssetPriceChange(asset);
        if(price != null){
            return price.getAskPrice();
        }
        
        return -0.0;        
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
        /*
        para implementar esta funcion primero se debe hacer 
        uso del endpoint: https://api.kucoin.com/v1/order/active.
        este endpoint retorna un array de array. el elemento en la poicion 5 del subarray es
        el orderId.
        
        Decimos que la orden esta en status NEW cuando el id de la orden pasado por parametro es igual al array[x][5].
        Si no se encontro el id del la orden en la respuesta del endpoint mencionadado anteriormente entonces procedemos a verificar
        si la orden se completo invocando el endpoint https://api.kucoin.com/v1/deal-orders
        */
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
        // https://api.kucoin.com/v1/account/balances
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isTimeFrameSupported(TimeFrame timeFrame) {
        
        switch(timeFrame){
            case ONE_MINUTE:
            case FIVE_MINUTES:
            case FIFTEEN_MINUTES:
            case THIRTY_MINUTES:
            case ONE_HOUR:
            case EIGHT_HOURS:
            case ONE_DAY:
            case ONE_WEEK:
                return true;
        }
        
        return false;
    }
}
