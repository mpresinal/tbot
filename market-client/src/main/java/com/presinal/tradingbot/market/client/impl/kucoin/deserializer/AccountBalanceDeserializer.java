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
import com.presinal.tradingbot.market.client.types.AccountBalance;
import com.presinal.tradingbot.market.client.util.JsonDeserializerUtil;
import static com.presinal.tradingbot.market.client.util.JsonObjectDeserializerUtil.getAttrAsDoubleIfNotNull;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AccountBalanceDeserializer implements JsonDeserializer<AccountBalance> {

   /* */
    @Override
    public AccountBalance deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        AccountBalance accountBalance = null;

        JsonArray jsonArr = JsonDeserializerUtil.getAsJsonArrayIfNotNull(je); //getAttrAsJsonArrayIfNotNull(getAsJsonObjectIfNotNull(je), "data");

        if(jsonArr != null){
            accountBalance = new AccountBalance();

            int size = jsonArr.size();
            double balance = 0.0, freezeBalance = 0.0;
            JsonObject balanceObj;
            
            Iterator<JsonElement> it = jsonArr.iterator();
            while(it.hasNext()){
                balanceObj = it.next().getAsJsonObject();
                balance =  getAttrAsDoubleIfNotNull(balanceObj, "balance");
                freezeBalance = getAttrAsDoubleIfNotNull(balanceObj, "freezeBalance");
                accountBalance.addBalance(balanceObj.get("coinType").getAsString(), Math.abs(balance - freezeBalance), freezeBalance);
            }
        }
        return accountBalance;
    }

}
