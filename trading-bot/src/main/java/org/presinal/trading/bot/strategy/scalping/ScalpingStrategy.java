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

import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.bot.strategy.Signal;
import org.presinal.trading.bot.strategy.Strategy;
import org.presinal.trading.bot.strategy.listener.StrategyListener;
import org.presinal.trading.bot.strategy.listener.TradingStrategyListener;
import org.presinal.trading.indicator.EMA;
import org.presinal.trading.indicator.VolumeMovingAverage;
import org.presinal.trading.indicator.datareader.PeriodIndicatorDataReader;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public final class ScalpingStrategy implements Strategy {

    public static final TimeFrame DEFAULT_TIME_FRAME = TimeFrame.FIFTEEN_MINUTES;

    private static final long RETRIEVE_DATA_EVERY_FIVE_SECONDS = 5 * 1000;

    private StrategyListener listener;

    // Market and Data Reader
    
    private MarketClient marketClient;
    private PeriodIndicatorDataReader dataReader;

    // Indicators
    private EMA fastEMAInd;
    private EMA slowEMAInd;
    private EMA trendLineEMA;
    private VolumeMovingAverage volumeInd;

    private AssetPair asset;

    // computed variables
    private Double trendAverage = null;
    private double currentPrice = -1;
    
    
    private boolean buySignalGenerated = false;
    private boolean sellSignalGenerated = false;
    
    // Strategy config  
   private ScalpingStrategyConfig config;
    

    public ScalpingStrategy(MarketClient marketClient, AssetPair asset, ScalpingStrategyConfig config) {
        setMarketClient(marketClient);

        if (asset == null) {
            throw new NullPointerException("asset can not be null");
        }

        this.asset = asset;
        
        if(config != null) {
            this.config = config;            
        } else {
            this.config = ScalpingStrategyConfig.getDefault();
        }
    }

    public ScalpingStrategy(MarketClient marketClient, AssetPair asset) {
        this(marketClient, asset, ScalpingStrategyConfig.getDefault());
    }

    public void init() {
        TimeFrame indicatorTimeFrame = config.getIndicatorTimeFrame();
        fastEMAInd = new EMA(13, indicatorTimeFrame);
        slowEMAInd = new EMA(34, indicatorTimeFrame);
        trendLineEMA = new EMA(config.getTrendLinePeriod(), config.getTrendLineTimeFrame());
        
        volumeInd = new VolumeMovingAverage();
        volumeInd.setPeriod(config.getVolumeIndicatorPeriod());

        // use trend line period multiply by 3. I's much more better to use a big data set for ema calculaton
        dataReader = new PeriodIndicatorDataReader(asset, config.getTrendLinePeriod()*3, indicatorTimeFrame);
        dataReader.setMarketClient(marketClient);
    }

    @Override
    public void run() {

        boolean trendUp = false;

        try {
            System.out.println("################## Computing Trend line....");
            // compute current Trend
            if (trendAverage == null || trendAverage <= 0) {
                computeTrendLine();
            }

            System.out.println("+++++++++++++++++++ Trend line average = " + trendAverage);
            System.out.println("################## Computing Trend line....DONE!!");

            System.out.println("################## Computing current price....");

            // Compute current price
            if (currentPrice <= 0) {
                currentPrice = getAssetCurrentPrice();
            }
            System.out.println("+++++++++++++++++++ currentPrice = " + currentPrice);
            System.out.println("################## Computing current price....DONE!!");

            // Check if the asset trend line is upward.
            trendUp = trendAverage < currentPrice;

            System.out.println("+++++++++++++++++++ is trend line up = " + trendUp);

            List<Candlestick> data;
            Double fastEmaValue, slowEmaValue, volumeAverageValue=0.0;
            Candlestick currentCandlestick;
            
            long iterationIdx = 0;
            
            boolean buySignalRequirementFullfiled = false;
            
            while (trendUp) {
                System.out.println("\n\n -----------------------------------------------------------------------");
                iterationIdx += 1;
                System.out.println("+++++++++++++++++++ iteration number: " + iterationIdx);
                
                System.out.println("################## Computing date range data reader....");
                // compute date range based on period and time frame to retrieve cancdlesticks
                computeDataReaderDateRange(dataReader);
                System.out.println("################## Computing date range data reader....DONE!!");

                System.out.println("################## Readeing data....");
                // read data candlesticks
                data = dataReader.readData();
                System.out.println("+++++++++++++++++++ data.size = " + (data != null? data.size() : "null"));
                System.out.println("################## Readeing data....DONE!!");

                System.out.println("################## Procesing data....");
                if (data != null && !data.isEmpty()) {

                    // Get current candlestick which is the last one in the list
                    currentCandlestick = data.get(data.size() - 1);

                    System.out.println("+++++++++++++++++++ currentCandlestick.closePrice = " + currentCandlestick.closePrice);
                    System.out.println("+++++++++++++++++++ currentCandlestick.volume = " + currentCandlestick.volume);

                    if (currentCandlestick.closePrice > 0) {

                        // compute indicators 
                        fastEMAInd.evaluate(data);
                        fastEmaValue = fastEMAInd.getSingleResult();

                        System.out.println("+++++++++++++++++++ fastEmaValue = " + fastEmaValue);

                        slowEMAInd.evaluate(data);
                        slowEmaValue = slowEMAInd.getSingleResult();
                        System.out.println("+++++++++++++++++++ slowEmaValue = " + slowEmaValue);
                        
                        if(config.isIncludeVolumeAverageCondition()) {
                            volumeInd.evaluate(data);
                            volumeAverageValue = volumeInd.getSingleResult();
                            System.out.println("+++++++++++++++++++ volumeAverageValue = " + volumeAverageValue);
                        }
                        
                        // ###########################################################################################                        
                        // Signa logic starte here

                        // process indictors values
                        if (fastEmaValue > slowEmaValue) {
                            
                            System.out.println("####### fastEma has crossesp up the slowEma");
                            
                            buySignalRequirementFullfiled = true;
                            
                            if(config.isIncludeVolumeAverageCondition()) {
                                // current volume must be greater than volume average
                                buySignalRequirementFullfiled = (currentCandlestick.volume > volumeAverageValue);                                
                            }
                            
                            // Notify lister with a buy signal   
                            if (!buySignalGenerated && buySignalRequirementFullfiled) {
                                notifySignal(new Signal<>(currentCandlestick.closePrice), currentCandlestick.closePrice, true);
                                buySignalGenerated = true;
                                sellSignalGenerated = false;
                            }

                        } else if (slowEmaValue > fastEmaValue) {
                            // Notify lister with a buy signal   
                            System.out.println("####### fastEma has crossesp down the slowEma");
                            
                            // Notify listener with a sell signal only if a previous buy signal was generated
                            if(!sellSignalGenerated && buySignalGenerated) {
                                notifySignal(new Signal<>(currentCandlestick.closePrice), currentCandlestick.closePrice, false);
                                buySignalGenerated = false;
                                sellSignalGenerated = true;
                            }
                        }                        
               
                        // Signa logic end here
                        // ###########################################################################################                        

                        System.out.println("################## Procesing data....DONE!!");

                        // check if it is still in up trend line. To do so the trend line must be recalculated                        
                        computeTrendLineMovement();
                        trendUp = trendAverage < currentCandlestick.closePrice;
                        
                    } // close price if
                    
                    if (trendUp) {
                        try {
                            Thread.sleep(RETRIEVE_DATA_EVERY_FIVE_SECONDS);
                        } catch (InterruptedException ex) {
                            System.out.println("ScalpingStrategy.run() Thread interrupted. Reason: " + ex.getMessage());
                        }
                    } // end if                   

                } // end data if                

                System.out.println("\n\n -----------------------------------------------------------------------");
            }

        } catch (Exception e) {
            System.out.println("ScalpingStrategy.run() Error runing strategy. " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private void computeDataReaderDateRange(PeriodIndicatorDataReader dataReader) {

        long perioTimestamp = dataReader.getTimeFrame().toMilliSecond() * dataReader.getPeriod();

        Calendar cal = Calendar.getInstance();
        Instant endDate = Instant.now();

        // remove second to avoid invalid date range
        cal.setTimeInMillis(endDate.toEpochMilli());
        endDate = Instant.ofEpochMilli(endDate.toEpochMilli() - (cal.get(Calendar.SECOND) * 1000));
        Instant startDate = Instant.ofEpochMilli(endDate.toEpochMilli() - perioTimestamp);
        dataReader.setDateRange(startDate, endDate);
    }

    private void computeTrendLine() {
        // get as much data as it can. Big data will guaranty a more acurate EMA        
        trendLineEMA.evaluate(readTrendLineData(trendLineEMA.getPeriod()*3));
        trendAverage = trendLineEMA.getSingleResult();
    }
    
    private void computeTrendLineMovement() {
        
        final int SHORT_PERIOD = 5;
        List<Candlestick> data = readTrendLineData(SHORT_PERIOD);
        
        if(data != null && !data.isEmpty()){            
            trendLineEMA.evaluate(data.get(data.size()-1), trendAverage);
            trendAverage = trendLineEMA.getSingleResult();
        }
    }

    private List<Candlestick> readTrendLineData(int period){
        PeriodIndicatorDataReader dreader = new PeriodIndicatorDataReader(asset, period, trendLineEMA.getTimeFrame());
        dreader.setMarketClient(marketClient);
        computeDataReaderDateRange(dreader);
        List<Candlestick> data = dreader.readData();
        return data;
    }
    
    private Double getAssetCurrentPrice() throws MarketClientException {
        return marketClient.getAssetPrice(this.asset);
    }

    protected void notifySignal(Signal signal, double price, boolean buySignal) {
        if (listener != null) {
            if (listener instanceof TradingStrategyListener) {
                TradingStrategyListener tradingListener = (TradingStrategyListener) listener;
                if (buySignal) {
                    tradingListener.onBuySignal(asset, price);
                } else {
                    tradingListener.onSellSignal(asset, price);
                }

            } else {
                listener.onSignal(signal, this);
            }
        }
    }

    @Override
    public void setListener(StrategyListener listener) {
        this.listener = listener;
    }

    public void setMarketClient(MarketClient marketClient) {
        if (marketClient == null) {
            throw new NullPointerException("marketClient can not be null");
        }
        this.marketClient = marketClient;
    }

}
