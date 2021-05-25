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

package com.presinal.tradingbot.market.client.types;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AccountBalance implements Serializable {

    private Map<String, Balance> balances;

    public AccountBalance() {
       balances = new TreeMap<>();
    }
    
    public void addBalance(String asset, double available, double reserved) {
        
        if(asset != null && !asset.isEmpty()){
            balances.put(asset, new Balance(asset, reserved, available));
        }
    }
    
    public Balance getBalanceFor(String asset){
        return balances.get(asset);
    }
    
    public Set<String> getAssetList(){
        return balances.keySet();
    }
    
    public static class Balance {
        public final String asset;
        public final double reserved;
        public final double available;

        public Balance(String asset, double qtyReserved, double qtyAvailable) {
            this.asset = asset;
            this.reserved = qtyReserved;
            this.available = qtyAvailable;
        }

        @Override
        public String toString() {
            return "Balance{" + "asset=" + asset + ", reserved=" + reserved + ", available=" + available + '}';
        }

    } 

    @Override
    public String toString() {
        return "AccountBalance{" + "balances=" + balances + '}';
    }

}
