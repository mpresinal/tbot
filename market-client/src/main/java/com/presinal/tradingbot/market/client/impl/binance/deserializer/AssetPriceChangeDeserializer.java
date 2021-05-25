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

package com.presinal.tradingbot.market.client.impl.binance.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.AssetPriceChange;

import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.*;
import static com.presinal.tradingbot.market.client.util.JsonObjectDeserializerUtil.*;

/**
 * This class de-serialize the following json object into <code>AssetPriceChange</code>.<br/><br/>
 * Json object:
 * {
 *   "symbol": "BNBBTC",
 *   "priceChange": "-94.99999800",
 *   "priceChangePercent": "-95.960",
 *   "weightedAvgPrice": "0.29628482",
 *   "prevClosePrice": "0.10002000",
 *   "lastPrice": "4.00000200",
 *   "lastQty": "200.00000000",
 *   "bidPrice": "4.00000000",
 *   "askPrice": "4.00000200",
 *   "openPrice": "99.00000000",
 *   "highPrice": "100.00000000",
 *   "lowPrice": "0.10000000",
 *   "volume": "8913.30000000",
 *   "quoteVolume": "15.30000000",
 *   "openTime": 1499783499040,
 *   "closeTime": 1499869899040,
 *   "fristId": 28385,   // First tradeId
 *   "lastId": 28460,    // Last tradeId
 *   "count": 76         // Trade count
 * }
 * 
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetPriceChangeDeserializer implements JsonDeserializer<AssetPriceChange> {
    
    private static final BigDecimal PERCENT = new BigDecimal(100);
    
    @Override
    public AssetPriceChange deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject obj = getAsJsonObjectIfNotNull(je);
        
        AssetPriceChange priceChange = null;
        
        if(obj != null) {
            
            priceChange = new AssetPriceChange();
            priceChange.setAssetPair(new AssetPair(getAttrAsStringIfNotNull(obj, "symbol")));            
            priceChange.setOpenTime(getAttrAsDateIfNotNull(obj, "openTime"));            
            priceChange.setPriceChange(getAttrAsDoubleIfNotNull(obj, "priceChange"));
            priceChange.setBidPrice(getAttrAsDoubleIfNotNull(obj, "bidPrice"));             
            priceChange.setAskPrice(getAttrAsDoubleIfNotNull(obj, "askPrice"));
            
            String changeRateStr = getAttrAsStringIfNotNull(obj, "priceChangePercent");
            if(changeRateStr != null && !changeRateStr.isEmpty()){
                BigDecimal val = new BigDecimal(changeRateStr);            
                priceChange.setPriceChangePercent(val.multiply(PERCENT).floatValue());
            }

            priceChange.setHighPrice(getAttrAsDoubleIfNotNull(obj, "highPrice"));
            priceChange.setLowPrice(getAttrAsDoubleIfNotNull(obj, "lowPrice"));
            priceChange.setVolume(getAttrAsDoubleIfNotNull(obj, "volume"));
            priceChange.setQuoteVolume(getAttrAsDoubleIfNotNull(obj, "quoteVolume"));
        }
        
        return priceChange;
    }
    
    

}
