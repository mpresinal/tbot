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
package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.indicator.result.MACDResult;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class MACD extends AbstractIndicator<MACDResult> {

    public static final String NAME = "MACD";
    private static final String TO_STR_FORMAT = "%1$s(%2$d %3$d): %4$s";

    /*
     * Fast moving average period. 12 by default
     */
    private int fastPeriod = 12;

    /*
     * Slow moving average period. 12 by default
     */
    private int slowPeriod = 26;

    /*
     * The signal Moving average period. 9 by default
     */
    private int signalPeriod = 9;

    /*
     * Flag to indicate what type of moving average to use, a simple or exponential moving average.
     * false by default which means exponential moving average will get used for the calculation
     */
    private boolean useSimpleMA = false;
    
    private MovingAverage fastMA;
    private MovingAverage slowMA;
    private MovingAverage signalMA;

    public MACD() {
        super(NAME);
        changeMAType();
    }

    @Override
    public void evaluate(List<Candlestick> data) {

        if (data.size() < getHigerPeriod()) {
            throw new IllegalArgumentException("The data list size is lower than the higer period.");
        }
        
        // add 1 to perios
        fastMA.evaluate(data.subList(0, incrementByOneIfNotUseSimpleMA(fastPeriod)));
        slowMA.evaluate(data.subList(0, incrementByOneIfNotUseSimpleMA(slowPeriod)));
        signalMA.evaluate(data.subList(0, incrementByOneIfNotUseSimpleMA(signalPeriod)));
        
        BigDecimal macdValue = fastMA.getResult().subtract(slowMA.getResult());
        setResult(new MACDResult(macdValue, signalMA.getResult(), fastMA.getResult(), slowMA.getResult()));
        notifyListeners();
    }

    private void changeMAType() {
        if (useSimpleMA) {
            fastMA = new SMA(fastPeriod, getTimeFrame());
            slowMA = new SMA(slowPeriod, getTimeFrame());
            signalMA = new SMA(signalPeriod, getTimeFrame());
            
        } else {
            fastMA = new EMA(fastPeriod, getTimeFrame());
            slowMA = new EMA(slowPeriod, getTimeFrame());
            signalMA = new EMA(signalPeriod, getTimeFrame());
        }       

    }
    
    private int getHigerPeriod() {
        int higer = slowPeriod >= fastPeriod ? slowPeriod : fastPeriod;
        return higer > signalPeriod ? higer : signalPeriod;
    }

    private int incrementByOneIfNotUseSimpleMA(int period) {
        return !useSimpleMA? ++period : period;
    }
    
    public int getFastPeriod() {
        return fastPeriod;
    }

    public void setFastPeriod(int fastPeriod) {
        this.fastPeriod = fastPeriod;
    }

    public int getSlowPeriod() {
        return slowPeriod;
    }

    public void setSlowPeriod(int slowPeriod) {
        this.slowPeriod = slowPeriod;
    }

    public int getSignalPeriod() {
        return signalPeriod;
    }

    public void setSignalPeriod(int signalPeriod) {
        this.signalPeriod = signalPeriod;
    }

    public boolean isUseSimpleMA() {
        return useSimpleMA;
    }

    public void setUseSimpleMA(boolean useSimpleMA) {
        this.useSimpleMA = useSimpleMA;
        changeMAType();
    }    

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, NAME, fastPeriod, slowPeriod, getResult());
    }

}
