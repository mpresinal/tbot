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
package org.presinal.trading.bot.strategy;

import java.util.List;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.bot.strategy.listener.StrategyListener;
import org.presinal.trading.indicator.RSI;
import org.presinal.trading.indicator.SMA;
import org.presinal.trading.indicator.VolumeMovingAverage;
import org.presinal.trading.indicator.datareader.PeriodIndicatorDataReader;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class DefaultStrategy implements Strategy {

    private static final Logger logger = Logger.getLogger(DefaultStrategy.class.getName());
    private static final int DATA_READER_PERIOD = 200;
    private static final int FAST_MA_PERIOD = 7;
    private static final int SLOW_MA_PERIOD = 25;
    private static final int VOLUME_PERIOD = 20;
    private static final int RSI_PERIOD = 14;
    
    private static final long RETRIEVE_DATA_EVERY_TEN_SECONDS = 10 * 1000;
    

    private StrategyListener listener;
    private MarketClient client;
    private AssetPair asset;

    // Indicators
    private SMA fastEMAInd;
    private SMA slowEMAInd;
    private VolumeMovingAverage volumeInd;
    private RSI rsiInd;

    private PeriodIndicatorDataReader dataReader;

    private boolean buySignalGenerated = false;
    private boolean sellSignalGenerated = false;

    private TimeFrame timeFrame;

    public DefaultStrategy(MarketClient client, AssetPair asset, TimeFrame timeFrame) {

        if (asset == null) {
            throw new NullPointerException("asset can not be null");
        }

        if (client == null) {
            throw new NullPointerException("client can not be null");
        }

        this.client = client;
        this.asset = asset;
        this.timeFrame = timeFrame;
    }

    public void init() {
        fastEMAInd = new SMA(FAST_MA_PERIOD, timeFrame);
        slowEMAInd = new SMA(SLOW_MA_PERIOD, timeFrame);

        volumeInd = new VolumeMovingAverage();
        volumeInd.setPeriod(VOLUME_PERIOD);

        rsiInd = new RSI(RSI_PERIOD);

        // use trend line period multiply by 3. I's much more better to use a big data set for ema calculaton
        dataReader = new PeriodIndicatorDataReader(asset, DATA_READER_PERIOD, timeFrame);
        dataReader.setMarketClient(client);
    }

    @Override
    public void setListener(StrategyListener listener) {
        this.listener = listener;
    }

    @Override
    public Strategy getImpl() {
        return this;
    }

    @Override
    public void run() {

        boolean running = true;

        List<Candlestick> data;
        Double fastEmaValue, slowEmaValue, volumeAverageValue = 0.0, rsiValue = 0.0;
        Candlestick currentCandlestick;

        while (running) {

            computeDataReaderDateRange(dataReader);
            data = dataReader.readData();
            
            if (data != null && !data.isEmpty()) {

                // Get current candlestick which is the last one in the list
                currentCandlestick = data.get(data.size() - 1);

                if (currentCandlestick.closePrice > 0) {

                    fastEMAInd.evaluate(data);
                    fastEmaValue = fastEMAInd.getSingleResult();

                    slowEMAInd.evaluate(data);
                    slowEmaValue = slowEMAInd.getSingleResult();

                    volumeInd.evaluate(data);
                    volumeAverageValue = volumeInd.getSingleResult();

                    rsiInd.evaluate(data);
                    rsiValue = rsiInd.getSingleResult();
                    
                    logger.info("-----------------------------------------------------------------");
                    logger.info("*** current price = "+currentCandlestick.closePrice);
                    logger.info("** fastEmaValue = "+fastEmaValue);
                    logger.info("** slowEmaValue = "+slowEmaValue);
                    logger.info("** volumeAverageValue = "+volumeAverageValue);
                    logger.info("** rsiValue = "+rsiValue);
                    logger.info("-----------------------------------------------------------------");
                    
                    if (fastEmaValue > slowEmaValue && (rsiInd.isOverSold() || rsiInd.isNormal())) {
                        if (currentCandlestick.volume > volumeAverageValue) {
                            // Notify lister with a buy signal   
                            if (!buySignalGenerated) {
                                notifySignal(currentCandlestick.closePrice, true);
                                buySignalGenerated = true;
                                sellSignalGenerated = false;
                            }
                        }

                    } else if ((fastEmaValue < slowEmaValue) && (rsiInd.isOverBought())) {
                        // Notify listener with a sell signal only if a previous buy signal was generated
                        if (!sellSignalGenerated && buySignalGenerated) {
                            notifySignal(currentCandlestick.closePrice, false);
                            buySignalGenerated = false;
                            sellSignalGenerated = true;
                        }
                    }

                }

            }

            if (running) {
                try {
                    Thread.sleep(RETRIEVE_DATA_EVERY_TEN_SECONDS);
                } catch (InterruptedException ex) {
                    logger.warning("Thread has been interrupted. Reason: " + ex.getMessage());
                }
            } // end if
        }

    }

    protected void notifySignal(double price, boolean buySignal) {
        BuySellSignal signal = new BuySellSignal(asset, price, buySignal);
        notifySignal(signal, listener);
    }
}
