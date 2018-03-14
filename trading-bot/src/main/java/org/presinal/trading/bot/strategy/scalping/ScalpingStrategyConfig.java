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

package org.presinal.trading.bot.strategy.scalping;

import java.io.Serializable;
import org.presinal.market.client.enums.TimeFrame;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class ScalpingStrategyConfig implements Serializable {

    public static final int DEFAULT_TREND_LINE_PERIOD = 200;
    
    private TimeFrame trendLineTimeFrame;
    private int trendLinePeriod;
    private TimeFrame indicatorTimeFrame;
    
    private int volumeIndicatorPeriod;
    
    /*
    * Flag that indicates that the volume average must be taken into consideration in the logic for buy/sell signal
    */
    private boolean includeVolumeAverageCondition = true;
    
    private static ScalpingStrategyConfig defaultConfig;

    public ScalpingStrategyConfig() {
    }
    
    public ScalpingStrategyConfig(ScalpingStrategyConfig config) {
        this.trendLineTimeFrame = config.trendLineTimeFrame; 
        this.trendLinePeriod = config.trendLinePeriod;
        this.indicatorTimeFrame = config.indicatorTimeFrame;
        this.includeVolumeAverageCondition = config.includeVolumeAverageCondition;
        this.volumeIndicatorPeriod=config.volumeIndicatorPeriod;
    }    
    
    
    public static final ScalpingStrategyConfig getDefault() {
        
        if(defaultConfig != null) {
            return defaultConfig;            
        }
        
        defaultConfig = new ScalpingStrategyConfig();
        defaultConfig.trendLineTimeFrame = TimeFrame.EIGHT_HOURS; // Default value 8 houres
        defaultConfig.trendLinePeriod = DEFAULT_TREND_LINE_PERIOD; // Default value 200 period
        defaultConfig.indicatorTimeFrame = TimeFrame.FIFTEEN_MINUTES; // Default value 15 minutes
        defaultConfig.includeVolumeAverageCondition = true;
        defaultConfig.volumeIndicatorPeriod = 20; // Default value 20 period
        return defaultConfig;
    }
    
    public TimeFrame getTrendLineTimeFrame() {
        return trendLineTimeFrame;
    }

    public void setTrendLineTimeFrame(TimeFrame timeFrame) {
        this.trendLineTimeFrame = timeFrame;
    }

    public int getTrendLinePeriod() {
        return trendLinePeriod;
    }

    public void setTrendLinePeriod(int period) {
        this.trendLinePeriod = period;
    }

    public TimeFrame getIndicatorTimeFrame() {
        return indicatorTimeFrame;
    }

    public void setIndicatorTimeFrame(TimeFrame timeFrame) {
        this.indicatorTimeFrame = timeFrame;
    }

    public int getVolumeIndicatorPeriod() {
        return volumeIndicatorPeriod;
    }

    public void setVolumeIndicatorPeriod(int volumeIndicatorPeriod) {
        this.volumeIndicatorPeriod = volumeIndicatorPeriod;
    }
    
    public boolean isIncludeVolumeAverageCondition() {
        return includeVolumeAverageCondition;
    }

    public void setIncludeVolumeAverageCondition(boolean value) {
        this.includeVolumeAverageCondition = value;
    }

    @Override
    public String toString() {
        return "ScalpingStrategyConfig{" + "trendLineTimeFrame=" + trendLineTimeFrame 
                + ", trendLinePeriod=" + trendLinePeriod 
                + ", indicatorTimeFrame=" + indicatorTimeFrame 
                + ", volumeIndicatorPeriod="+volumeIndicatorPeriod
                + ", includeVolumeAverageCondition=" + includeVolumeAverageCondition 
                + "}";
    }
    
    
}
