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

import java.util.Collection;
import java.util.List;
import org.presinal.market.client.types.Candlestick;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class MovingAverage extends AbstractIndicator<Double> {

    private static final String NAME = "Moving Average";
    private double mean;
    
    public static enum CandlestickSource {
        CLOSED_PRICE, VOLUME
    }
    
    private CandlestickSource source;

    public MovingAverage() {
        this(NAME);
        
    }
    
    protected MovingAverage(String name) {
        super(name, ResultType.SINGLE_RESULT);
        source = CandlestickSource.CLOSED_PRICE;
    }
    
    protected MovingAverage(String name, CandlestickSource source) {
        this(name);
        this.source = source;
    }

    @Override
    public Double getSingleResult() {
        return mean;
    }

    @Override
    public Collection<Double> getMultiResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluate(List<Candlestick> data) {
        
        if (data != null && !data.isEmpty()) {
            double total = 0.0;
            
            // calculating the range index
            int length = data.size();            
            int start=0;
            
            int tmpPeriod = getPeriod();
            int p = tmpPeriod;
            
            if(length > tmpPeriod){
                start = length - tmpPeriod;                
            } else {
                p =  length;
            }         
            
            double value;
            for (int i = start; i < length; i++) {
                
                switch(source) {                    
                    case VOLUME:
                        value = data.get(i).volume;
                        break;
                    case CLOSED_PRICE:    
                    default:
                        value = data.get(i).closePrice;
                        break;                        
                }
                
                total = total + value;
            }            
            
            mean = total / p;
            notifyListeners();            
        }
    }
}
