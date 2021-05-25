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
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class Candlestick implements Serializable {
    
    public final double openPrice;
    public final double closePrice;
    public final double lowPrice;
    public final double highPrice;
    public final double volume;
    public final Instant dateTime;

    public Candlestick(double openPrice, double closePrice, double lowPrice, double highPrice, double volume, Instant dateTime) {
        this.openPrice = openPrice;
        this.closePrice = closePrice;
        this.lowPrice = lowPrice;
        this.highPrice = highPrice;
        this.volume = volume;
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Candlestick{" + "openPrice=" + openPrice
                + ", closePrice=" + closePrice 
                + ", lowPrice=" + lowPrice 
                + ", highPrice=" + highPrice 
                + ", volume=" + volume 
                + ", dateTime=" + dateTime + "}";
    }
    
    
    
}
