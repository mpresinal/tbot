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
package com.presinal.tradingbot.bot.strategy;

import com.presinal.tradingbot.bot.action.BotActionContext;
import java.util.List;
import java.util.logging.Logger;
import com.presinal.tradingbot.market.client.MarketClient;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.bot.strategy.listener.StrategyListener;
import com.presinal.tradingbot.indicator.RSI;
import com.presinal.tradingbot.indicator.SMA;
import com.presinal.tradingbot.indicator.VolumeMovingAverage;
import com.presinal.tradingbot.indicator.datareader.PeriodIndicatorDataReader;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DefaultStrategy implements Strategy {

    private static final Logger logger = Logger.getLogger(DefaultStrategy.class.getName());
    private static final int DATA_READER_PERIOD = 200;
    private static final int FAST_MA_PERIOD = 7;
    private static final int SLOW_MA_PERIOD = 25;
    private static final int VOLUME_PERIOD = 20;
    private static final int RSI_PERIOD = 14;

    private static final long RETRIEVE_DATA_EVERY_TEN_SECONDS = 20 * 1000;

    @Override
    public void setContext(BotActionContext context) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private enum StopLostTaketProfitResult {

        STOP_LOST, TAKE_PROFIT, NONE;
    }

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

    private int takeProfitPercentage = 5;
    private int stopLostPercentage = 5;

    private double buyPrice;

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

        //rsiInd = new RSI(RSI_PERIOD);
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
        StopLostTaketProfitResult stopLTakePResult = StopLostTaketProfitResult.NONE;

        while (running) {

            computeDataReaderDateRange(dataReader);
            data = dataReader.readData();

            if (data != null && !data.isEmpty()) {

                // Get current candlestick which is the last one in the list
                currentCandlestick = data.get(data.size() - 1);

                if (currentCandlestick.closePrice > 0) {

                    fastEMAInd.evaluate(data);
                    fastEmaValue = fastEMAInd.getResult().doubleValue();

                    slowEMAInd.evaluate(data);
                    slowEmaValue = slowEMAInd.getResult().doubleValue();

                    volumeInd.evaluate(data);
                    volumeAverageValue = volumeInd.getResult().doubleValue();

                    /*rsiInd.evaluate(data);
                     rsiValue = rsiInd.getResult();*/
                    logger.info("-----------------------------------------------------------------");
                    logger.info("*** asset = " + asset.toSymbol());
                    logger.info("*** current price = " + currentCandlestick.closePrice);
                    logger.info("*** current volume = " + currentCandlestick.volume);                    
                    logger.info("** fastEmaValue = " + fastEmaValue);
                    logger.info("** slowEmaValue = " + slowEmaValue);
                    logger.info("** volumeAverageValue = " + volumeAverageValue);
                    logger.info("** buyPrice = " + buyPrice);

                    if (buySignalGenerated) {
                        stopLTakePResult = computeStopLostOrTakeProfit(currentCandlestick.closePrice);
                    }

                    logger.info("** stopLostTakeProfitResult = " + stopLTakePResult);
                    logger.info("** takeProfitPercentage = " + takeProfitPercentage);
                    logger.info("** stopLostPercentage = " + stopLostPercentage);
                    //logger.info("** rsiValue = "+rsiValue);
                    logger.info("-----------------------------------------------------------------");

                    if (fastEmaValue > slowEmaValue /*&& (rsiInd.isOverSold() || rsiInd.isNormal())*/) {
                        if (currentCandlestick.volume >= volumeAverageValue) {
                            // Notify lister with a buy signal   
                            if (!buySignalGenerated) {
                                buyPrice = currentCandlestick.closePrice;
                                notifySignal(currentCandlestick.closePrice, true);
                                buySignalGenerated = true;
                                sellSignalGenerated = false;

                                continue;
                            }
                        }

                    } else if ((fastEmaValue < slowEmaValue) /*&& (rsiInd.isOverBought())*/) {
                        // Notify listener with a sell signal only if a previous buy signal was generated
                        if (!sellSignalGenerated && buySignalGenerated) {

                            notifySignal(currentCandlestick.closePrice, false);
                            buySignalGenerated = false;
                            sellSignalGenerated = true;
                            stopLTakePResult = StopLostTaketProfitResult.NONE;
                            continue;
                        }
                    }

                    // sell if taket profit or stop lost has been reached
                    if (stopLTakePResult != StopLostTaketProfitResult.NONE) {
                        logger.info("** notify sell signal");
                        notifySignal(currentCandlestick.closePrice, false);
                        buySignalGenerated = false;
                        sellSignalGenerated = true;
                        stopLTakePResult = StopLostTaketProfitResult.NONE;

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

    private StopLostTaketProfitResult computeStopLostOrTakeProfit(double currentPrice) {
        double delta = buyPrice - currentPrice;

        double priceChange = (delta / buyPrice) * 100;

        logger.info("** price change = " + delta);
        logger.info("** price change % = " + priceChange);

        priceChange = (priceChange < 0) ? priceChange * -1 : priceChange;

        if (currentPrice > buyPrice && priceChange >= takeProfitPercentage) {
            return StopLostTaketProfitResult.TAKE_PROFIT;

        } else if (currentPrice < buyPrice && priceChange >= stopLostPercentage) {
            return StopLostTaketProfitResult.STOP_LOST;

        }
        return StopLostTaketProfitResult.NONE;
    }

    protected void notifySignal(double price, boolean buySignal) {
        BuySellSignal signal = new BuySellSignal(asset, price, buySignal);
        notifySignal(signal, listener);
    }

    public void setTakeProfitPercentage(int takeProfitPercentage) {
        this.takeProfitPercentage = takeProfitPercentage;
    }

    public void setStopLostPercentage(int stopLostPercentage) {
        this.stopLostPercentage = stopLostPercentage;
    }

    @Override
    public void setClient(MarketClient client) {
        this.client=client;
    }

    @Override
    public void setAsset(AssetPair asset) {
        this.asset = asset;
    }
}
