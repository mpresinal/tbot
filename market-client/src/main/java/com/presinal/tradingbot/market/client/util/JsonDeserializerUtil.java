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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class JsonDeserializerUtil {

    public static JsonArray getAsJsonArrayIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsJsonArray();
        }        
        return null;
    }
    
    public static JsonObject getAsJsonObjectIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsJsonObject();
        }
        
        return null;
    }
    
    public static double getAsDoubleIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsBigDecimal().doubleValue();
        }
        
        return 0;
    }
    
    public static long getAsLongIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsLong();
        }
        
        return 0;
    }
    
    public static float getAsFloatIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsFloat();
        }
        
        return 0;
    }
    
    public static String getAsStringIfNotNull(JsonElement jsonEl){
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return jsonEl.getAsString();
        }
        
        return null;
    }
    
    public static Date getAsDateIfNotNull(JsonElement jsonEl) {
        if(jsonEl != null && !jsonEl.isJsonNull()){
            return new Date(jsonEl.getAsLong());
        }
        return null;
    }    
    
     public static Date getArrayValueAsDate(JsonArray array, int index) {
        return getAsDateIfNotNull(getArrayValueAtIfNotOutBound(array, index));  
    }
     
    public static double getArrayValueAsDouble(JsonArray array, int index) {
        return getAsDoubleIfNotNull(getArrayValueAtIfNotOutBound(array, index));  
    }
    
      public static long getArrayValueAsLong(JsonArray array, int index) {
        return getAsLongIfNotNull(getArrayValueAtIfNotOutBound(array, index));  
    }
      
    public static JsonElement getArrayValueAtIfNotOutBound(JsonArray array, int index) {
        if(array != null && array.size() > index){
            return array.get(index);
        }
        return null;
    }
}
