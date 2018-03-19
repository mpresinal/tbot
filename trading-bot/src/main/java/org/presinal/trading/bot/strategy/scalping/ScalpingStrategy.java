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
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
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

    private static final long RETRIEVE_DATA_EVERY_TEN_SECONDS = 10 * 1000;

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

    private void setupLogger(Logger logger) {
        
        logger.setLevel(Level.FINEST);
        
        try {            
            FileHandler fileHandler = new FileHandler(ScalpingStrategy.class.getSimpleName()+"_"+asset.getBaseAsset()+"-"+asset.getQuoteAsset()+".log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);            
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error adding File Handler. "+ex.getMessage(), ex);            
        }        
    }
    
    @Override
    public void run() {
        
        String CLASS_NAME = ScalpingStrategy.class.getName();
        Logger logger = Logger.getLogger(CLASS_NAME+"."+asset.getBaseAsset()+"."+asset.getQuoteAsset());
        setupLogger(logger);
        
        boolean running = true;        
        boolean trendUp = false;  
        
        logger.info("Running strategy with config: "+config);

        try {
            
            if(config.isIncludeTrendLineVerification()){
                logger.finest("Computing Trend line....");
                // compute current Trend
                if (trendAverage == null || trendAverage <= 0) {
                    computeTrendLine();
                }

                logger.finest("Trend line average = " + trendAverage);
                logger.finest("Computing Trend line....DONE!!");

                logger.finest("Computing current price....");

                // Compute current price
                if (currentPrice <= 0) {
                    currentPrice = getAssetCurrentPrice();
                }
                logger.finest("currentPrice = " + currentPrice);
                logger.finest("Computing current price....DONE!!");

                // Check if the asset trend line is upward.
                trendUp = trendAverage < currentPrice;

                logger.finest("is trend line up = " + trendUp);
                
                running = trendUp;                
            } 
            
            List<Candlestick> data;
            Double fastEmaValue, slowEmaValue, volumeAverageValue=0.0;
            Candlestick currentCandlestick;
            
            long iterationIdx = 0;
            
            boolean buySignalRequirementFullfiled = false;
            
            while (running) {
                logger.finest("\n\n -----------------------------------------------------------------------");
                iterationIdx += 1;
                logger.finest("iteration number: " + iterationIdx);
                
                logger.finest("Computing date range data reader....");
                // compute date range based on period and time frame to retrieve cancdlesticks
                computeDataReaderDateRange(dataReader);
                logger.finest("Computing date range data reader....DONE!!");

                logger.finest("Readeing data....");
                // read data candlesticks
                data = dataReader.readData();
                logger.finest("data.size = " + (data != null? data.size() : "null"));
                logger.finest("Readeing data....DONE!!");

                logger.finest("Procesing data....");
                if (data != null && !data.isEmpty()) {

                    // Get current candlestick which is the last one in the list
                    currentCandlestick = data.get(data.size() - 1);

                    logger.finest("** currentCandlestick.closePrice = " + currentCandlestick.closePrice);
                    logger.finest("** currentCandlestick.volume = " + currentCandlestick.volume);

                    if (currentCandlestick.closePrice > 0) {

                        // compute indicators 
                        fastEMAInd.evaluate(data);
                        fastEmaValue = fastEMAInd.getSingleResult();

                        logger.finest("** fastEmaValue = " + fastEmaValue);

                        slowEMAInd.evaluate(data);
                        slowEmaValue = slowEMAInd.getSingleResult();
                        logger.finest("** slowEmaValue = " + slowEmaValue);
                        
                        if(config.isIncludeVolumeAverageCondition()) {
                            volumeInd.evaluate(data);
                            volumeAverageValue = volumeInd.getSingleResult();
                            logger.finest("** volumeAverageValue = " + volumeAverageValue);
                        }
                        
                        // ###########################################################################################                        
                        // Signa logic starte here

                        // process indictors values
                        if (fastEmaValue > slowEmaValue) {
                            
                            logger.finest(" fastEma has crossesp up the slowEma");
                            
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
                            logger.finest("*** fastEma has crossesp down the slowEma");
                            
                            // Notify listener with a sell signal only if a previous buy signal was generated
                            if(!sellSignalGenerated && buySignalGenerated) {
                                notifySignal(new Signal<>(currentCandlestick.closePrice), currentCandlestick.closePrice, false);
                                buySignalGenerated = false;
                                sellSignalGenerated = true;
                            }
                        }                        
               
                        // Signal logic end here
                        // ###########################################################################################                        

                        logger.finest("Procesing data....DONE!!");

                        if(config.isIncludeTrendLineVerification()){
                            // check if it is still in up trend line. To do so the trend line must be recalculated                        
                            computeTrendLineMovement();
                            trendUp = trendAverage < currentCandlestick.closePrice;
                            running = trendUp;
                        }
                        
                    } // close price if
                    
                    if (running) {
                        try {
                            Thread.sleep(RETRIEVE_DATA_EVERY_TEN_SECONDS);
                        } catch (InterruptedException ex) {
                            logger.warning("Thread has been interrupted. Reason: " + ex.getMessage());
                        }
                    } // end if                   

                } // end data if                

                logger.finest("\n\n -----------------------------------------------------------------------");
            }

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error running the strategy. ", e);
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
