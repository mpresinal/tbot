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

import org.presinal.trading.indicator.listener.IndicatorListener;
import java.util.HashSet;
import java.util.Set;
import static javafx.scene.input.KeyCode.R;
import org.presinal.market.client.enums.TimeFrame;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public abstract class AbstractIndicator<T> implements Indicator<T>{

    protected int period;
    protected TimeFrame timeFrame;
    private String name;
    private ResultType resultType;    
    
    protected Set<IndicatorListener> listeners;
    
    protected AbstractIndicator(String name, ResultType resultType) {
        this.name = name;
        this.resultType=resultType;        
    }
    
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public ResultType getResultType() {
        return this.resultType;
    }

    @Override
    public void addListener(IndicatorListener listener) {
        if(listeners == null) {
            this.listeners = new HashSet<>();
        }
        
        listeners.add(listener);
    }
    
    protected void notifyListeners(){
        if(listeners != null && !listeners.isEmpty()){
            for (IndicatorListener listener : listeners) {
                listener.onEvaluate(this);
            }
        }
    }

    public int getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(int period) {
        this.period = period;
    }

    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    @Override
    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

}
