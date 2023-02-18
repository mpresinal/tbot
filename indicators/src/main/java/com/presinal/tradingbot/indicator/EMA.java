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

import com.presinal.tradingbot.indicator.util.NumberUtil;
import java.util.List;
import java.util.logging.Logger;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * This indicator required data list size greater than the period.
 * 
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class EMA extends MovingAverage {

    private final String CLASS_NAME = EMA.class.getSimpleName();
    private Logger logger = Logger.getLogger(CLASS_NAME);

    private static final String NAME = "EMA";

    private BigDecimal ema;
    private BigDecimal previousEma = BigDecimal.valueOf(-1.0);
    private BigDecimal alpha;
    private SMA sma;

    public EMA() {
        super(NAME);        
    }

    public EMA(int period, TimeFrame timeFrame) {
        this();
        setPeriod(period);
        setTimeFrame(timeFrame);
    }

    /**
     * @param data data list size must be greater than the period
     */
    @Override    
    public void evaluate(List<Candlestick> data) {

        if (data != null && !data.isEmpty()) {

            if (sma == null) {
                initSMA();
            }
            
            int dataLength = data.size();
            
            if (alpha == null) {
                alpha = BigDecimal.valueOf(2.0).divide( BigDecimal.valueOf(getPeriod() + 1.0), NumberUtil.MATHCONTEXT);
            }
            
            if(dataLength > getPeriod()) {
                //if (previousEma <= 0) {
                if (previousEma.compareTo(BigDecimal.ZERO) <= 0) {
                    // computing simple moving average
                    sma.evaluate(data.subList(0, getPeriod()));
                    previousEma = sma.getResult();                    
                }
                                
                for(int i = getPeriod(); i < dataLength; i++) {                    
                    evaluate(data.get(i), previousEma);
                    previousEma = getResult();
                }

            }           
            
        }

    }

    public void evaluate(Candlestick current, BigDecimal previousEma) {        
        /*
         * Formula:         
         * EMA = CLOSING_PRICE * ALPHA + PREVIOUS_EMA * (1 - ALPHA)
         * Where ALPHA = 2 / (PERIOD + 1)
         * we are goin to use a SMA as a previous EMA for the first EMA calculation
         */
        BigDecimal a = BigDecimal.valueOf(current.closePrice).multiply(alpha, NumberUtil.MATHCONTEXT);
        BigDecimal b = previousEma.multiply(BigDecimal.ONE.subtract(alpha, NumberUtil.MATHCONTEXT), NumberUtil.MATHCONTEXT);                  
        this.setResult(a.add(b, NumberUtil.MATHCONTEXT));
        notifyListeners();
    }    
    

    private void initSMA() {
        sma = new SMA();
        sma.setPeriod(getPeriod());
        sma.setTimeFrame(getTimeFrame());
    }
}
