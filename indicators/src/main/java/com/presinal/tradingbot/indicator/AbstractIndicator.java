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

import com.presinal.tradingbot.indicator.listener.IndicatorListener;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.presinal.tradingbot.market.client.enums.TimeFrame;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public abstract class AbstractIndicator<T extends Comparable> implements Indicator<T> {

    private static final String TO_STR_FORMAT = "%1$s(%2$d): %3$s";
    protected int period;
    protected TimeFrame timeFrame;
    private String name;
    private String id;
    private ResultType resultType;
    private T result;

    protected Set<IndicatorListener> listeners;

    protected AbstractIndicator(String name) {
        this(name, ResultType.SINGLE_RESULT);
    }
    
    protected AbstractIndicator(String name, ResultType resultType) {
        this.name = name;
        this.resultType = resultType;
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
        if (listeners == null) {
            this.listeners = new HashSet<>();
        }

        listeners.add(listener);
    }

    protected void notifyListeners() {
        if (listeners != null && !listeners.isEmpty()) {
            for (IndicatorListener listener : listeners) {
                listener.onEvaluate(this);
            }
        }
    }

    public int compareTo(Indicator<T> o) {
        return compareTo(o.getResult());
    }

    @Override
    public int compareTo(T o) {
        Comparable compData = getAsComparableIfItIs(getResult());
        Comparable compWith;
        if (o instanceof Indicator) {
            compWith = getAsComparableIfItIs(((Indicator) o).getResult());
        } else {
            compWith = getAsComparableIfItIs(o);
        }

        if (Objects.isNull(compData) && Objects.isNull(compWith)) {
            return 0;

        } else if (!Objects.isNull(compData) && Objects.isNull(compWith)) {
            return 1;

        } else if (Objects.isNull(compData) && !Objects.isNull(compWith)) {
            return -1;

        } else {
            return compData.compareTo(compWith);
        }
    }

    private Comparable getAsComparableIfItIs(Object o) {
        if (o != null && o instanceof Comparable) {
            return (Comparable) o;
        }

        return null;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public T getResult() {
        return this.result;
    }
    
    protected void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return String.format(TO_STR_FORMAT, name, period, getResult().toString());
    }

}
