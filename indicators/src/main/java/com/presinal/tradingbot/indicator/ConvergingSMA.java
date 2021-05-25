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

import com.presinal.tradingbot.indicator.result.ConverginIndicatorPairs;
import com.presinal.tradingbot.indicator.result.ConvergingResult;
import com.presinal.tradingbot.indicator.util.DataScaleTimeFrame;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.market.client.util.TimeFrameComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * This indicator tell if some moving average with different time frame and period converge.
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class ConvergingSMA extends AbstractIndicator<ConvergingResult> {

    private TimeFrame[] convergingTimeFrame;
    private int[] convergingPeriod;
    private Map<TimeFrame, List<MovingAverage>> timeFrameMAMap;

    public ConvergingSMA(TimeFrame[] convergingTimeFrame, int[] convergingPeriod) {
        super("Moving Averages Converging");

        if (convergingTimeFrame == null || convergingTimeFrame.length == 0) {
            throw new IllegalArgumentException("convergingTimeFrame is null or empty");
        }

        if (convergingPeriod == null || convergingPeriod.length == 0) {
            throw new IllegalArgumentException("convergingPeriod is null or empty");
        }

        this.convergingTimeFrame = convergingTimeFrame;
        this.convergingPeriod = convergingPeriod;

        timeFrameMAMap = new TreeMap<>(new TimeFrameComparator());
        init();

    }

    private void init() {
        List<MovingAverage> list;
        TimeFrame tf;
        for (int i = 0; i < convergingTimeFrame.length; i++) {
            tf = convergingTimeFrame[i];
            list = new ArrayList<>();
            for (int x = 0; x < convergingPeriod.length; x++) {
                list.add(new SMA(convergingPeriod[x], tf));
            }
            timeFrameMAMap.put(tf, list);
        }
    }

    @Override
    public void evaluate(List<Candlestick> data) {
        
        List<MovingAverage> maList;
        List<Candlestick> dataIntimeFrame;
        for (Entry<TimeFrame, List<MovingAverage>> entry : timeFrameMAMap.entrySet()) {
            maList = entry.getValue();
            
            if (maList != null) {
                dataIntimeFrame = DataScaleTimeFrame.scaleToTimeFrame(data, getTimeFrame(), entry.getKey());
                for (MovingAverage ma : maList) {
                    ma.evaluate(dataIntimeFrame);
                }                
            }            
        }
        
        final ConvergingResult result = new ConvergingResult();
        
        int length = convergingTimeFrame.length;
        TimeFrame t1, t2;        
        
        for (int i = 0; i < length; i++) {
            t1 = convergingTimeFrame[i];            
            maList = timeFrameMAMap.get(t1);
            
            for (int x = i+1; x < length; x++) {
                t2 = convergingTimeFrame[i+1];                
                addIfConverged(maList, timeFrameMAMap.get(t2), result);
            }
        }
        
        setResult(result);
    }
    
    private void addIfConverged(List<MovingAverage> listA, List<MovingAverage> listB, ConvergingResult result) {
        for (MovingAverage maa : listA) {
            for (MovingAverage mab : listB) {
                if (maa.getResult().equals(mab.getResult())) {
                    result.getConvergingIndicators().add(new ConverginIndicatorPairs(maa, mab));
                }
            }
        }
    }

    public TimeFrame[] getConvergingTimeFrame() {
        return convergingTimeFrame;
    }

    public int[] getConvergingPeriod() {
        return convergingPeriod;
    }

}
