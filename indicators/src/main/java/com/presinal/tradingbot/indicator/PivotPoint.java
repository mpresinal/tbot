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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.indicator.PivotPoint.PivotPointResult;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PivotPoint extends AbstractIndicator<PivotPointResult> {

    private static final String NAME = "Pivot Point";
    
    private final int LEVELS = 3;
    private PivotPointResult result;
    public PivotPoint() {
        super(NAME, ResultType.SINGLE_RESULT);
    }
    
    @Override
    public PivotPointResult getResult() {
        return result;
    }

    @Override
    public void evaluate(List<Candlestick> data) {
        if(data != null && !data.isEmpty()){
            evaluate(data.get(data.size()-1));
        }
    }
    
    public void evaluate(Candlestick previousDay) {
        /*
        Formula:
        PP: (H+L+C)/3 (a simple average of the three prices)  
        R1: (2*PP)-L  
        S1: (2*PP) – H  
        
        R2: PP+(H-L)
        S2: PP-(H-L)        
        
        R3 = H + 2(PP – L) => R1 + (H − L)
        S3 = L – 2(H – PP) => S1 − (H − L)
        Leer más en: http://www.pullback.es/los-niveles-psicologicos-los-pivot-points/
        https://www.babypips.com/learn/forex/how-to-calculate-pivot-points
        https://en.wikipedia.org/wiki/Pivot_point_(technical_analysis)        
        */
        double pp = (previousDay.highPrice+previousDay.lowPrice+previousDay.closePrice) / 3.0;
        
        Double[] resistance = new Double[LEVELS];
        Double[] supports = new Double[LEVELS];
        
        // First level resistance and support
        resistance[0] = (2.0*pp) - previousDay.lowPrice;
        supports[0] = (2.0*pp) - previousDay.highPrice;
        
        // Second level resistance and support
        resistance[1] = pp + (previousDay.highPrice - previousDay.lowPrice);
        supports[1] = pp - (previousDay.highPrice - previousDay.lowPrice);
        
        // Thierd level resistance and support
        resistance[2] =  resistance[0] + (previousDay.highPrice - previousDay.lowPrice);//previousDay.highPrice + (2*(pp - previousDay.lowPrice));
        supports[2] = supports[0] - (previousDay.highPrice - previousDay.lowPrice);//previousDay.lowPrice + (2*(previousDay.highPrice - pp));
        
        result = new PivotPointResult(pp, supports, resistance);
        notifyListeners();        
    }
    
    private double round(double value ){
        return value;
        //return NumberUtil.round(value);
    }

    public static class PivotPointResult implements Comparable<PivotPointResult>{
        public final Double pivotPoint;
        public final Double[] supports;
        public final Double[] resistance;

        public PivotPointResult(Double pivotPoint, Double[] supports, Double[] resistance) {
            this.pivotPoint = pivotPoint;
            this.supports = supports;
            this.resistance = resistance;
        }

        @Override
        public String toString() {
            return "PivotPointResult{" + "pivotPoint=" + pivotPoint 
                    + ", supports=" + (supports != null? Arrays.toString(supports) : null) 
                    + ", resistance=" + (resistance != null? Arrays.toString(resistance) : null) + '}';
        }

        @Override
        public int compareTo(PivotPointResult o) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
