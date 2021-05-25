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

package com.presinal.tradingbot.market.client.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Date;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsDateIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsDoubleIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsFloatIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsJsonArrayIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsJsonObjectIfNotNull;
import static com.presinal.tradingbot.market.client.util.JsonDeserializerUtil.getAsStringIfNotNull;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class JsonObjectDeserializerUtil {

    public static JsonArray getAttrAsJsonArrayIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsJsonArrayIfNotNull(obj.get(attributeName));
        }        
        return null;
    }
    
    public static JsonObject getAttrAsJsonObjectIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsJsonObjectIfNotNull(obj.get(attributeName));
        }        
        return null;
    }

    public static double getAttrAsDoubleIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsDoubleIfNotNull(obj.get(attributeName));
        }        
        return 0;
    }
    
     public static float getAttrAsFloatIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsFloatIfNotNull(obj.get(attributeName));
        }        
        return 0;
    }

    public static String getAttrAsStringIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsStringIfNotNull(obj.get(attributeName));
        }        
        return null;
    }
    
    
    public static Date getAttrAsDateIfNotNull(JsonObject obj, String attributeName ){
        if(obj != null){
            return getAsDateIfNotNull(obj.get(attributeName));
        }        
        return null;
    }
    
    
}
