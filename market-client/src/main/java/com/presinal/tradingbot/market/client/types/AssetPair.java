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
import java.util.Objects;

/**
 * This class represent a symbol in a trading platform.
 * A symbol is compounded by a pair of assets.
 * 
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetPair implements Serializable {

    private String baseAsset;
    private String quoteAsset;
    private String symbol;

    public AssetPair(String symbol) {
        this.symbol = symbol;
    }
    
    public AssetPair(String baseAsset, String quoteAsset) {
        this.baseAsset = baseAsset;
        this.quoteAsset = quoteAsset;
    }

    /**
     * 
     * @param symbolSeparator a separator character to use as a part of the symbol.
     * separator may vary depending of the market trading. 
     * 
     * @return a String containing the symbol.
     */
    public String toSymbol(String symbolSeparator) {
        
        if(symbol != null) {
            return symbol;
        } 
        
        return baseAsset + (symbolSeparator == null? "" : symbolSeparator) + quoteAsset;
    }
    
    public String toSymbol() {
        return toSymbol("-");
    }
    
    public String getBaseAsset() {
        return baseAsset;
    }

    public String getQuoteAsset() {
        return quoteAsset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.baseAsset);
        hash = 61 * hash + Objects.hashCode(this.quoteAsset);
        hash = 61 * hash + Objects.hashCode(this.symbol);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AssetPair other = (AssetPair) obj;
        if (!Objects.equals(this.baseAsset, other.baseAsset)) {
            return false;
        }
        if (!Objects.equals(this.quoteAsset, other.quoteAsset)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AssetPaire {" + "baseAsset=" + baseAsset + ", quoteAsset=" + quoteAsset 
                + ", symbol="+symbol+"}";
    }    
    
}
