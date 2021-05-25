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
package com.presinal.tradingbot.indicator.util;

import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DataScaleTimeFrame {

    /**
     * Scale the data in particular time frame to other time frame.
     * 
     * @param data A chronological ordered list of Candlestick. The list should be ordered by time in ascending order.
     * @param dataTimeFrame the time frame of data
     * @param timeFrameToScale time frame to scale data
     * @return 
     */
    public static List<Candlestick> scaleToTimeFrame(List<Candlestick> data, TimeFrame dataTimeFrame, TimeFrame timeFrameToScale) {

        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("data is null or empty");
        }

        if (dataTimeFrame == null) {
            throw new NullPointerException("dataTimeFrame cannot be null");
        }

        if (timeFrameToScale == null) {
            throw new NullPointerException("timeFrameToScale cannot be null");
        }

        List<Candlestick> list = new ArrayList<>();
        double openPrice = 0;
        double closePrice = 0;
        double lowPrice = Double.MAX_VALUE;
        double highPrice = Double.MIN_VALUE;
        double volume = 0;
        Instant dateTime = null;

        int amount = (int) TimeFrame.howManyTimeFit(dataTimeFrame, timeFrameToScale);
        int i = 1;
        int dataSize = data.size();
        for (Candlestick c : data) {

            if (dateTime == null) {
                dateTime = c.dateTime;
                openPrice = c.openPrice;
            }
            
            lowPrice = lowPrice < c.lowPrice? lowPrice : c.lowPrice;
            highPrice = highPrice > c.highPrice? highPrice : c.highPrice;            
            volume += c.volume;

            if (i % amount == 0 || i == dataSize) {
                list.add(new Candlestick(openPrice, c.closePrice, lowPrice, highPrice, volume, dateTime));
                dateTime = null;
                openPrice = 0;                
                lowPrice = 0;
                highPrice = 0;
                volume = 0;
            }

            ++i;
        }

        return list;
    }
}
