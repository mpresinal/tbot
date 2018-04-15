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
import java.util.Set;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.bot.strategy.listener.StrategyListener;
import org.presinal.trading.bot.strategy.rule.Rule;
import org.presinal.trading.indicator.Indicator;
import org.presinal.trading.indicator.datareader.PeriodIndicatorDataReader;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class BasedRuleStrategy implements Strategy {

    private static final Logger logger = Logger.getLogger(BasedRuleStrategy.class.getName());
    private static final long RETRIEVE_DATA_EVERY_TEN_SECONDS = 20 * 1000;
    
   
    private StrategyListener listener;
    private MarketClient client;
    private AssetPair asset;    
    private PeriodIndicatorDataReader dataReader;
    
    private Rule buyRule;
    private Rule sellRule;
    
    private Set<Indicator> indicators;
    
    private boolean buySignalGenerated = false;
    private boolean sellSignalGenerated = false;
    
    @Override
    public Strategy getImpl() {
        return this;
    }

    @Override
    public void run() {
        boolean running = true;
        List<Candlestick> data;
        Candlestick currentCandlestick;
        
        dataReader.setMarketClient(client);
        dataReader.setAsset(asset);
        
        boolean buyRuleCondSatisfied = false;
        boolean sellRuleCondSatisfied = false;
        while (running) {
            
            computeDataReaderDateRange(dataReader);
            data = dataReader.readData();

            if (data != null && !data.isEmpty()) {
                
                // Get current candlestick which is the last one in the list
                currentCandlestick = data.get(data.size() - 1);

                if (currentCandlestick.closePrice > 0) {
                    
                    for(Indicator ind : indicators) {
                        ind.evaluate(data);
                    }
                    
                    logger.info("-----------------------------------------------------------------");
                    logger.info("*** asset = " + asset.toSymbol());
                    logger.info("*** current price = " + currentCandlestick.closePrice);
                    logger.info("*** current volume = " + currentCandlestick.volume);                                        
                    
                    indicators.forEach( ind -> {
                        logger.info("*** "+ind);
                    });
                    
                    logger.info("*** buyRule = " + buyRule);  
                    buyRuleCondSatisfied = buyRule.evaluate();
                    logger.info("*** sellRule = " + sellRule);                     
                    sellRuleCondSatisfied = sellRule.evaluate();                   
                    
                    logger.info("*** buyRuleCondSatisfied = " + buyRuleCondSatisfied);   
                    logger.info("*** sellRuleCondSatisfied = " + sellRuleCondSatisfied);  
                    
                    logger.info("*** buySignalGenerated = " + buySignalGenerated);  
                    logger.info("*** sellSignalGenerated = " + sellSignalGenerated);  
                    
                    if (!buySignalGenerated && buyRuleCondSatisfied) {
                        logger.info(" Buy signal detected at price: "+currentCandlestick.closePrice);
                        // Notify lister with a buy signal
                        notifySignal(currentCandlestick.closePrice, true);
                        buySignalGenerated = true;
                        sellSignalGenerated = false;

                        //continue;

                    } else if( (!sellSignalGenerated && buySignalGenerated) && sellRuleCondSatisfied) {
                        
                        logger.info(" Sell signal detected at price: "+currentCandlestick.closePrice);
                        notifySignal(currentCandlestick.closePrice, false);
                        buySignalGenerated = false;
                        sellSignalGenerated = true;
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
     
    @Override
    public void setListener(StrategyListener listener) {
        this.listener = listener;
    }

    public MarketClient getClient() {
        return client;
    }

    @Override
    public void setClient(MarketClient client) {
        this.client = client;
    }

    public AssetPair getAsset() {
        return asset;
    }

    @Override
    public void setAsset(AssetPair asset) {
        this.asset = asset;
    }

    public PeriodIndicatorDataReader getDataReader() {
        return dataReader;
    }

    public void setDataReader(PeriodIndicatorDataReader dataReader) {
        this.dataReader = dataReader;
    }

    public Rule getBuyRule() {
        return buyRule;
    }

    public void setBuyRule(Rule buyRule) {
        this.buyRule = buyRule;
    }

    public Rule getSellRule() {
        return sellRule;
    }

    public void setSellRule(Rule sellRule) {
        this.sellRule = sellRule;
    }

    public Set<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(Set<Indicator> indicators) {
        this.indicators = indicators;
    }
    
}
