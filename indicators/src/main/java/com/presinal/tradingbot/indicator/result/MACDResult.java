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
package com.presinal.tradingbot.indicator.result;

import java.math.BigDecimal;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class MACDResult implements Comparable<MACDResult> {

    public final BigDecimal macd;
    public final BigDecimal signal;
    public final BigDecimal fastMA;
    public final BigDecimal slowMA;
    private final BigDecimal histogram;
    private final boolean overBaseLine;

    public MACDResult(BigDecimal macd, BigDecimal signal, BigDecimal fastMA, BigDecimal slowMA) {
        this.macd = macd == null ? BigDecimal.ZERO : macd;
        this.signal = signal == null ? BigDecimal.ZERO : signal;
        this.fastMA = fastMA == null ? BigDecimal.ZERO : fastMA;
        this.slowMA = slowMA == null ? BigDecimal.ZERO : slowMA;
        
        int comp = macd.compareTo(signal);
        if (comp > 0) {
            this.histogram = macd.subtract(signal);
        } else {
            this.histogram = signal.add(macd); // this is equivalent a substract because macd is negative
        }
        
        this.overBaseLine = macd.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public int compareTo(MACDResult o) {
        return macd.compareTo(o.macd);
    }

    @Override
    public String toString() {        
        return  macd + " " + signal + " " + histogram;
    }

}
