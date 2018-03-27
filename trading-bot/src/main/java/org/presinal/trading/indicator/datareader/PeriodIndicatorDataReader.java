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

package org.presinal.trading.indicator.datareader;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.impl.kucoin.KucoinMarketClient;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class PeriodIndicatorDataReader implements IndicatorDataReader<List<Candlestick>> {
    
    private MarketClient client;
    private AssetPair asset;
    private int period;
    private TimeFrame timeFrame;
    
    private Instant startDate, endDate;

    public PeriodIndicatorDataReader(AssetPair asset, int period, TimeFrame timeFrame) {
        this.period = period;
        this.timeFrame = timeFrame;
        this.asset = asset;
    }
    
    
    @Override
    public void setMarketClient(MarketClient client) {
        this.client = client;
    }

    @Override
    public void setAsset(AssetPair asset) {
        this.asset = asset;
    }
    
    public int getPeriod() {
        return period;
    }
    
    public TimeFrame getTimeFrame() {
        return timeFrame;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setTimeFrame(TimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }
    
    public void setDateRange(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;        
    }
    
    @Override
    public List<Candlestick> readData() {
        try {
            return client.loadCandlestick(asset, timeFrame, startDate, endDate, client.getMaxDataLimit());
        } catch (MarketClientException ex) {
            Logger.getLogger(PeriodIndicatorDataReader.class.getName()).log(Level.SEVERE, "Error loading candlestick", ex);
        }
        
        return null;
    }

}
