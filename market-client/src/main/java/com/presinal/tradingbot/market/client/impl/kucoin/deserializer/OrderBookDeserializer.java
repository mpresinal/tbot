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

package com.presinal.tradingbot.market.client.impl.kucoin.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import com.presinal.tradingbot.market.client.types.OrderBook;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsJsonObjectIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonObjectDeserializerUtil.getAttrAsJsonArrayIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonObjectDeserializerUtil.getAttrAsJsonObjectIfNotNull;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class OrderBookDeserializer implements JsonDeserializer<OrderBook> {

    @Override
    public OrderBook deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        OrderBook orderBook= null;        
        JsonObject dataObj = getAsJsonObjectIfNotNull(je);        
        
        if(dataObj != null){
            orderBook = new OrderBook(null);

            // Kucoin is a little bit confuse on the order book
            // Sell is the ask
            // Buy is the bid        
            addEntrie(getAttrAsJsonArrayIfNotNull(dataObj, "SELL"), orderBook.getAsks());  
            addEntrie(getAttrAsJsonArrayIfNotNull(dataObj, "BUY"), orderBook.getBids());  
        }
        return orderBook;
    }
    
    public OrderBook deserializex(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        
        JsonObject dataObj = getAttrAsJsonObjectIfNotNull(getAsJsonObjectIfNotNull(je), "data");        
        OrderBook orderBook = new OrderBook(null);
        
        // Kucoin is a little bit confuse on the order book
        // Sell is the ask
        // Buy is the bid        
        addEntrie(getAttrAsJsonArrayIfNotNull(dataObj, "SELL"), orderBook.getAsks());  
        addEntrie(getAttrAsJsonArrayIfNotNull(dataObj, "BUY"), orderBook.getBids());  
        
        return orderBook;
    }
    
    private void addEntrie(JsonArray array, List<OrderBook.OrderBookEntry> ordersList) {
        if(array != null){
           Iterator<JsonElement> it = array.iterator();
           JsonArray subArray;
           int size;
           while(it.hasNext()){
                subArray = it.next().getAsJsonArray();
                size = subArray.size();
                
                if(size >= 2) {
                    ordersList.add(new OrderBook.OrderBookEntry(subArray.get(0).getAsDouble(), subArray.get(1).getAsDouble()));
                }                
            }
        }        
    }

}
