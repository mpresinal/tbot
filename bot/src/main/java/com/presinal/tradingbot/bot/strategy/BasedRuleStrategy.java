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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.bot.strategy.rule.ComparisonOperator;
import com.presinal.tradingbot.bot.strategy.rule.ContextOperandValue;
import com.presinal.tradingbot.bot.strategy.rule.DefaultStrategyRule;
import com.presinal.tradingbot.bot.strategy.rule.IndicatorOperandValue;
import com.presinal.tradingbot.bot.strategy.rule.LogicalOperator;
import com.presinal.tradingbot.bot.strategy.rule.OperandValue;
import com.presinal.tradingbot.bot.strategy.rule.Rule;
import com.presinal.tradingbot.bot.strategy.rule.StrategyRuleGroup;
import com.presinal.tradingbot.bot.strategy.rule.definitions.DefaultStrategyRuleDefinition;
import com.presinal.tradingbot.bot.strategy.rule.definitions.StrategyRuleGroupDefinition;
import com.presinal.tradingbot.bot.strategy.rule.definitions.StrategyRuleDefinition;
import com.presinal.tradingbot.indicator.Indicator;
import com.presinal.tradingbot.indicator.datareader.PeriodIndicatorDataReader;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class BasedRuleStrategy extends AbstractStrategy {

    private static final Logger logger = Logger.getLogger(BasedRuleStrategy.class.getName());
    private static final long DEFAULT_DATA_READ_INTERVAL_SECONDS = 30;
    
    // Properties
    private PeriodIndicatorDataReader dataReader;
    
    private Set<Indicator> indicators;
    private long dataReadIntervalSeconds = DEFAULT_DATA_READ_INTERVAL_SECONDS;
    
    private Rule buyRule;
    private Rule sellRule;    
    
    // Buy or Sell Rule defition
    /*
    * This is usefull to create buy or sell rule using the current indicators.
    * Note: if buyRule or sellRule is alreade set then
    * buyRuleDefinition or sellRuleDefinition won't be taken
    * into consideration
    */
    private StrategyRuleDefinition buyRuleDefinition;
    private StrategyRuleDefinition sellRuleDefinition;
    
    // Internal
    private boolean buySignalGenerated = false;
    private boolean sellSignalGenerated = false;
    
    @Override
    public Strategy getImpl() {
        return this;
    }

    private DefaultStrategyRule createRule(DefaultStrategyRuleDefinition def, Map<String, Indicator> map) {        
        if (def.getLeftOperandValue() == null || def.getRightOperandValue() == null)  {
            return null;
        }
        ComparisonOperator cp = ComparisonOperator.valueOf(def.getComparisonOperator());        
        DefaultStrategyRule rule = new DefaultStrategyRule();
        rule.setLeftOperand(def.getLeftOperandValue());
        rule.setRightOperand(def.getRightOperandValue());        
        rule.setComparisonOperator(cp != null? cp : ComparisonOperator.EQUAL);
        
        setOperandValueRef(def.getLeftOperandValue(), map);
        setOperandValueRef(def.getRightOperandValue(), map);        
        
        return rule;
    }
    
    private void setOperandValueRef(OperandValue value, Map<String, Indicator> indicatorsMap) {
        if (value instanceof IndicatorOperandValue) {
            setIndicatorToOperandValue((IndicatorOperandValue) value, indicatorsMap);
        } else if (value instanceof ContextOperandValue) {
            ((ContextOperandValue) value).setContext(getContext());
        }
    }
    
    private void setIndicatorToOperandValue(IndicatorOperandValue operandValue, Map<String, Indicator> map) {
        Indicator ind = map.get(operandValue.getIndicatorId());
        if (ind != null) {
            operandValue.setIndicator(ind);
        }        
    }    
    
    private StrategyRuleGroup createRule(StrategyRuleGroupDefinition groupDef, Map<String, Indicator> map) {
        
        StrategyRuleGroup group = new StrategyRuleGroup();        
        LogicalOperator op = LogicalOperator.valueOf(groupDef.getLogicalOperator());                
        group.setLogicalOperator(op == null? LogicalOperator.AND : op);
        
        Set<DefaultStrategyRuleDefinition> list = groupDef.getRulesDefinition();        
        Rule rule;
        
        for (DefaultStrategyRuleDefinition idf : list) {
            rule = createRule(idf, map);
            if(rule == null){
                return null;
            }
            group.getRules().add(rule);
        }
        
        return group;
    }
    
    private Rule createRule(StrategyRuleDefinition def,  Map<String, Indicator> map) {
        
        Rule rule = null;
        
        if(def instanceof DefaultStrategyRuleDefinition) {
            rule = createRule((DefaultStrategyRuleDefinition) def, map);
            
        } else if(def instanceof StrategyRuleGroupDefinition) {
            
           rule = createRule((StrategyRuleGroupDefinition)def, map);
        }
        
        return rule;
    }
    
    private boolean createRuleFromDefinition() {
        Map<String, Indicator> map = new HashMap<>();
        
        getIndicators().forEach(ind -> map.put(ind.getId(), ind));
        
        if(buyRule == null && buyRuleDefinition != null) {
           buyRule = createRule(buyRuleDefinition, map);
        }
        
        if(sellRule == null && sellRuleDefinition != null){
           sellRule = createRule(sellRuleDefinition, map);
        }
        
        return buyRule != null && sellRule != null;
    }
    
    @Override
    public void run() {
        boolean running = true;
        List<Candlestick> data;
        Candlestick currentCandlestick;
        
        dataReader.setMarketClient(getClient());
        dataReader.setAsset(getAsset());
        
        long sleepTime = dataReadIntervalSeconds * 1000;
        
        boolean buyRuleCondSatisfied = false;
        boolean sellRuleCondSatisfied = false;
             
        if (getIndicators().isEmpty()) {
            logger.severe("Not indicators added to performa buy/sell signal");
            return;
        }
        
        // Create rule from definition
        if(buyRuleDefinition != null || sellRuleDefinition != null) {
            createRuleFromDefinition();            
        }        
        
        if(buyRule == null || sellRule == null) {
            logger.severe("Not buy or sell rule set to performa buy/sell signal");
            return;
        }
        
        while (running) {
    
            computeDataReaderDateRange(dataReader);
            data = dataReader.readData();

            if (data != null && !data.isEmpty()) {
                
                // Get current candlestick which is the last one in the list
                currentCandlestick = data.get(data.size() - 1);

                if (currentCandlestick.closePrice > 0) {
                    
                    logger.info("-----------------------------------------------------------------");
                    logger.info("*** asset = " + getAsset().toSymbol());
                    logger.info("*** current price = " + currentCandlestick.closePrice);
                    logger.info("*** current volume = " + currentCandlestick.volume);                   

                    for(Indicator ind : indicators) {
                        ind.evaluate(data);
                        logger.info("*** "+ind);
                    }
                    
                    logger.info("*** buyRule = " + buyRule);  
                    buyRuleCondSatisfied = buyRule.evaluate();
                    logger.info("*** sellRule = " + sellRule);                     
                    sellRuleCondSatisfied = sellRule.evaluate();                   
                    
                    logger.info("*** buyRuleCondSatisfied = " + buyRuleCondSatisfied);   
                    logger.info("*** buySignalGenerated = " + buySignalGenerated);
                    
                    logger.info("*** sellRuleCondSatisfied = " + sellRuleCondSatisfied);
                    logger.info("*** sellSignalGenerated = " + sellSignalGenerated);  
                    
                    if (!buySignalGenerated && buyRuleCondSatisfied) {
                        logger.info(" Buy signal detected at price: "+currentCandlestick.closePrice);
                        // Notify lister with a buy signal
                        notifySignal(currentCandlestick.closePrice, true);
                        buySignalGenerated = true;                       

                        //continue;

                    } else if( (!sellSignalGenerated && buySignalGenerated) && sellRuleCondSatisfied) {
                        
                        logger.info(" Sell signal detected at price: "+currentCandlestick.closePrice);
                        notifySignal(currentCandlestick.closePrice, false);                        
                        sellSignalGenerated = true;
                    }
                    
                    if(buySignalGenerated && sellSignalGenerated){
                        buySignalGenerated = false;
                        sellSignalGenerated = false;
                    }
                }
            }
            
            if (running) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    logger.warning("Thread has been interrupted. Reason: " + ex.getMessage());
                }
            } // end if
        }
    }
    
    protected void notifySignal(double price, boolean buySignal) {
        BuySellSignal signal = new BuySellSignal(getAsset(), price, buySignal);
        notifySignal(signal, getListener());
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

    public StrategyRuleDefinition getBuyRuleDefinition() {
        return buyRuleDefinition;
    }

    public void setBuyRuleDefinition(StrategyRuleDefinition buyRuleDefinition) {
        this.buyRuleDefinition = buyRuleDefinition;
    }

    public StrategyRuleDefinition getSellRuleDefinition() {
        return sellRuleDefinition;
    }

    public void setSellRuleDefinition(StrategyRuleDefinition sellRuleDefinition) {
        this.sellRuleDefinition = sellRuleDefinition;
    }
    
    public Set<Indicator> getIndicators() {
        if(indicators == null){
            indicators = new HashSet<>();
        }
        
        return indicators;
    }

    public void setIndicators(Set<Indicator> indicators) {
        this.indicators = indicators;
    }

    public long getDataReadIntervalSeconds() {
        return dataReadIntervalSeconds;
    }

    public void setDataReadIntervalSeconds(long dataReadIntervalSeconds) {
        this.dataReadIntervalSeconds = dataReadIntervalSeconds;
    }    
}
