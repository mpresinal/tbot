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
import org.presinal.trading.indicator.PivotPoint.PivotPointResult;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PivotPoint extends AbstractIndicator<PivotPointResult> {

    private int amountOfPoints;
    
    public PivotPoint(String name, ResultType resultType) {
        super(name, resultType);
    }

    @Override
    public PivotPointResult getSingleResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<PivotPointResult> getMultiResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void evaluate(List<Candlestick> data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void evaluate(Candlestick previousDay) {
        /*
        Formula:
        PP: (H+L+C)/3 (a simple average of the three prices)  
        S1: (2*PP) – H  
        S2: PP-H+L  
        R1: (2*PP)-L  
        R2: PP+H-L
        Leer más en: http://www.pullback.es/los-niveles-psicologicos-los-pivot-points/
        */
        double pp = (previousDay.highPrice+previousDay.lowPrice+previousDay.closePrice) / 3.0;
        Double[] resistance = new Double[3];
    }

    public static class PivotPointResult {
        public final Double pivotPoint;
        public final Double[] supports;
        public final Double[] resistance;

        public PivotPointResult(Double pivotPoint, Double[] supports, Double[] resistance) {
            this.pivotPoint = pivotPoint;
            this.supports = supports;
            this.resistance = resistance;
        }        
        
    }
}
