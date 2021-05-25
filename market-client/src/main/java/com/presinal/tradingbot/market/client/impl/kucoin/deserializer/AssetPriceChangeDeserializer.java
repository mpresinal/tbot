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
 *             "coinType": "BTC",
 *             "trading": true,
 *             "symbol": "BTC-USDT",
 *             "lastDealPrice": 10594.999995,
 *             "buy": 10554.37895,
 *             "sell": 10594.999998,
 *             "change": 347.899806,
 *             "coinTypePair": "USDT",
 *             "sort": 100,
 *             "feeRate": 0.001,
 *             "volValue": 3116833.91217824,
 *             "high": 10924.0,
 *             "datetime": 1519746684000,
 *             "vol": 296.77903081,
 *             "low": 10105.0,
 *             "changeRate": 0.034
 *         }
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
            priceChange.setAssetPair(new AssetPair(getAttrAsStringIfNotNull(obj, "coinType"), getAttrAsStringIfNotNull(obj, "coinTypePair")));
            priceChange.setOpenTime(getAttrAsDateIfNotNull(obj, "datetime"));
            
            priceChange.setPriceChange(getAttrAsDoubleIfNotNull(obj, "change"));
            
            String changeRateStr = getAttrAsStringIfNotNull(obj, "changeRate");
            if(changeRateStr != null && !changeRateStr.isEmpty()){
                BigDecimal val = new BigDecimal(changeRateStr);            
                priceChange.setPriceChangePercent(val.multiply(PERCENT).floatValue());
            }
            
            // buy is the bid at kucoin exchanger
            priceChange.setBidPrice(getAttrAsDoubleIfNotNull(obj, "buy"));
            
            // sell is the ask at kucoin exchanger
            priceChange.setAskPrice(getAttrAsDoubleIfNotNull(obj, "sell"));
            
            priceChange.setHighPrice(getAttrAsDoubleIfNotNull(obj, "high"));
            priceChange.setLowPrice(getAttrAsDoubleIfNotNull(obj, "low"));
            priceChange.setVolume(getAttrAsDoubleIfNotNull(obj, "vol"));
            priceChange.setQuoteVolume(getAttrAsDoubleIfNotNull(obj, "volValue"));
        }
        
        return priceChange;
    }
    
    

}
