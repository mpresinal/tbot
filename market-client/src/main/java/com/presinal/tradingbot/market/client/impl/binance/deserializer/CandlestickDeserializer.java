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

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import com.presinal.tradingbot.market.client.types.Candlestick;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.*;

/**
 *
 * [
 *  [
 *      1499040000000, // Open time 
 *      "0.01634790", // Open 
 *      "0.80000000", // High
 *      "0.01575800", // Low 
 *      "0.01577100", // Close 
 *      "148976.11427815", // Volume
 *      1499644799999, // Close time 
 *      "2434.19055334", // Quote asset volume 
 *      308, // Number of trades 
 *      "1756.87402397", // Taker buy base asset volume
 *      "28.46694368", // Taker buy quote asset volume 
 *      "17928899.62484339" // Ignore
 *  ] 
 * ]
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class CandlestickDeserializer implements JsonDeserializer<Candlestick[]> {

    @Override
    public Candlestick[] deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {

        JsonArray jsonArray = getAsJsonArrayIfNotNull(je);
        List<Candlestick> list = new ArrayList<>();
        Candlestick[] candlesticks = null;
        if (jsonArray != null) {

            int length = jsonArray.size();

            if (length > 0) {                
                JsonArray kline;
                for (int i = 0; i < length; i++) {

                    kline = jsonArray.get(i).getAsJsonArray();
                    if(kline != null && kline.size() > 0) {
                        list.add(new Candlestick(getArrayValueAsDouble(kline, 1),
                                getArrayValueAsDouble(kline, 4),
                                getArrayValueAsDouble(kline, 3),
                                getArrayValueAsDouble(kline, 2),
                                getArrayValueAsDouble(kline, 5),
                                Instant.ofEpochSecond(getArrayValueAsLong(kline, 0))
                        ));
                    }
                }
            }
        }
        
        if( !list.isEmpty()) {
            candlesticks = list.toArray(new Candlestick[0]);
        }
        return candlesticks;
    }
    
    private Candlestick newCandlestick(JsonArray kline) {        
        return null;
    }

}
