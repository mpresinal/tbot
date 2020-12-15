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
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import com.presinal.tradingbot.market.client.types.Candlestick;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class RSI extends AbstractIndicator<Double> {

    private static final String INDICATOR_NAME = "RSI";    
    
    private final BigDecimal ONE_HUNDRE = new BigDecimal("100.0");
    
    private int overBoughtLevel = 70;
    private int overSoldLevel = 30;
    
    private int normalLevel = 50;   
    
    public RSI() {
        super(INDICATOR_NAME);
    }
    
    public RSI(int period) {
        this();
        setPeriod(period);
    }
    
    public boolean isOverBought() {
        return getResult() >= overBoughtLevel;
    }
    
    public boolean isOverSold(){
        return getResult() <= overSoldLevel;
    }
    
    public boolean isNormal(){
        Double rsiValue = getResult();
        return (rsiValue >= ((overSoldLevel+normalLevel) / 2.0) && rsiValue <= normalLevel)
                || (rsiValue <= ((overBoughtLevel+normalLevel) / 2.0) && rsiValue >= normalLevel);
    }
    

    @Override
    public void evaluate(List<Candlestick> data) {
        /*
        * The formula to calculate the relative strength index is:
        * RSI = 100 - 100 /(1+RS)
        * Where RS = average gain of up periods / average lost of down periods
        *
        * The closed price will be used for calculate the RSI
        *
        */
        
        if(data != null && data.size() >= 2 ) {

            BigDecimal upward = BigDecimal.ZERO;
            BigDecimal downward = BigDecimal.ZERO;
            BigDecimal rs;
                        
            // calculating the range index
            int length = data.size();             
            int end = length-1;
            
            int start;
            if(length > period){
                start = length - period;                
            } else {
                start = 1;
            }

            Candlestick prev = data.get(start);            
            Candlestick current;
            //start; 
            for (int i = start+1; i <= end; i++) {
                current = data.get(i);
                                
                if(current.closePrice > prev.closePrice ) {
                    
                    upward = upward.add(BigDecimal.valueOf(current.closePrice).subtract(BigDecimal.valueOf(prev.closePrice)));
                    
                } else if(current.closePrice < prev.closePrice ) {                    
                    
                    downward = downward.add(BigDecimal.valueOf(prev.closePrice).subtract(BigDecimal.valueOf(current.closePrice)));                    
                    
                }               
                
                prev = current;
            }
            
            BigDecimal tmpPeriod =  BigDecimal.valueOf(period);
            BigDecimal avgUpward = upward.divide(tmpPeriod, NumberUtil.MATHCONTEXT);
            BigDecimal avgDownward = downward.divide(tmpPeriod, NumberUtil.MATHCONTEXT);

            rs =  avgUpward.divide(avgDownward, MathContext.DECIMAL64);             
            
            //rs = (upward/tmpPeriod) / (downward/tmpPeriod);
            //rsiValue = 100.0 - (100.0/(1+rs));            
            Double rsiValue = ONE_HUNDRE.subtract(
                    ONE_HUNDRE.divide(BigDecimal.ONE.add(rs), NumberUtil.MATHCONTEXT)
            ).doubleValue(); 
            
            setResult(rsiValue);         
            notifyListeners();
        }
    }

    public int getOverBoughtLevel() {
        return overBoughtLevel;
    }

    public void setOverBoughtLevel(int overBoughtLevel) {
        this.overBoughtLevel = overBoughtLevel;
    }

    public int getOverSoldLevel() {
        return overSoldLevel;
    }

    public void setOverSoldLevel(int overSoldLevel) {
        this.overSoldLevel = overSoldLevel;
    }
    
    
}
