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

package org.presinal.trading.indicator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.indicator.listener.IndicatorListener;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class RSI extends AbstractIndicator<Double> {

    private static final String INDICATOR_NAME = "RSI";
    
    private Double rsiValue = null;
    private int overBoughtLevel = 70;
    private int overSoldLevel = 30;
    
    private int normalLevel = 50;
    
    public RSI() {
        super(INDICATOR_NAME, ResultType.SINGLE_RESULT);
    }
    
    public RSI(int period) {
        this();
        setPeriod(period);
    }
    
    public boolean isOverBought() {
        return rsiValue >= overBoughtLevel;
    }
    
    public boolean isOverSold(){
        return rsiValue <= overSoldLevel;
    }
    
    public boolean isNormal(){
        return (rsiValue >= ((overSoldLevel+normalLevel) / 2.0) && rsiValue <= normalLevel)
                || (rsiValue <= ((overBoughtLevel+normalLevel) / 2.0) && rsiValue >= normalLevel);
    }
    
    @Override
    public Double getSingleResult() {
        return rsiValue;
    }

    @Override
    public Collection<Double> getMultiResult() {
        throw new UnsupportedOperationException("Not supported for SINGLE_RESULT type.");
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

            double upward = 0.0;
            double downward = 0.0;
            double delta;
            double rs;
            
            // calculating the range index
            int length = data.size();             
            int end = length-1;
            
            int start;
            if(length > period){
                start = length - period;                
            } else {
                start = 1;
            }

            System.out.println("length = "+length);
            System.out.println("start = "+start);
            System.out.println("end = "+end);
            
            DecimalFormat format = new DecimalFormat("#.########");
            Candlestick prev = data.get(start-1);
            System.out.println(format.format(prev.closePrice));
            Candlestick current;
            //start; 
            for (int i = start; i <= end; i++) {
                current = data.get(i);
                System.out.println(format.format(current.closePrice));
                
                delta = Math.abs(current.closePrice - prev.closePrice);
                
                if(current.closePrice > prev.closePrice ) {
                    upward +=  delta;
                } else if(current.closePrice < prev.closePrice ) {
                    downward +=  delta;
                }               
                
                prev = current;
            }
            
            double tmpPeriod =  period;
            rs = (upward/tmpPeriod) / (downward/tmpPeriod);
            rsiValue = 100.0 - (100.0/(1+rs));
            
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
