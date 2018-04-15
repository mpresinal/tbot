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
import java.util.logging.Logger;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.types.Candlestick;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class EMA extends AbstractIndicator<Double> {

    private final String CLASS_NAME = EMA.class.getSimpleName();
    private Logger logger = Logger.getLogger(CLASS_NAME);

    private static final String NAME = "EMA";

    private double ema;
    private double previousEma = -1.0;

    private SMA sma;

    public EMA() {
        super(NAME, ResultType.SINGLE_RESULT);
    }

    public EMA(int period, TimeFrame timeFrame) {
        this();
        setPeriod(period);
        setTimeFrame(timeFrame);
    }

    public Double getResult() {
        return ema;
    }

    @Override
    public void evaluate(List<Candlestick> data) {

        if (data != null && !data.isEmpty()) {

            if (sma == null) {
                initSMA();
            }
            
            int dataLength = data.size();
            
            if(dataLength > getPeriod()) {
                if (previousEma <= 0) {
                    // computing simple moving average
                    sma.evaluate(data.subList(0, getPeriod()));
                    previousEma = sma.getResult().doubleValue();
                }
                
                for(int i = getPeriod(); i < dataLength; i++) {
                    double currentPrice = data.get(i).closePrice;
                    evaluate(data.get(i), previousEma);
                    previousEma = getResult();
                }
                
                ema = previousEma;
            }           
            
        }

    }

    public void evaluate(Candlestick current, Double previousEma) {        
        /*
         * Formula:
         * EMA = PREVIOUS_EMA + ALPHA (CURRENT_PRICE - PREVIOUS_EMA)
         * Where ALPHA = 2 / (PERIOD + 1)
         * we are goin to use a SMA as a previous EMA for the first EMA calculation
         */        
        double alpha = 2.0 / (getPeriod() + 1);
        double currentPrice = current.closePrice;
        ema = previousEma + alpha * (currentPrice - previousEma);
        notifyListeners();
    }

    private void initSMA() {
        sma = new SMA();
        sma.setPeriod(getPeriod());
        sma.setTimeFrame(getTimeFrame());
    }
}
