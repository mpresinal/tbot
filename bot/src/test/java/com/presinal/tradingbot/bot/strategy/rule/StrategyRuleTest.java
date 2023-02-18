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

package com.presinal.tradingbot.bot.strategy.rule;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.indicator.SMA;
import com.presinal.tradingbot.indicator.VolumeMovingAverage;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class StrategyRuleTest {

    private List<Candlestick> data;
    
    public StrategyRuleTest() {
    }
    
    @Before
    public void loadData(){
        data = new ArrayList<>();
        data.add(new Candlestick(0.00001143, 0.00001250, 0.000011, 0.00001250, 1_0000, Instant.EPOCH));
    }
   
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
        
        VolumeMovingAverage volume = new VolumeMovingAverage();
        volume.setPeriod(1);
        volume.evaluate(data);

        System.out.println("evaluate.volume avg = "+volume.getResult());
        
        StrategyRule instance = new StrategyRule();
        instance.setLeftOperand(new IndicatorOperandValue(volume));
        instance.setRightOperand(new DefaultOperandValue(new BigDecimal(1_0000)));
        instance.setComparisonOperator(ComparisonOperator.GREATER_EQUAL_THAN);
        
        boolean expResult = true;
        boolean result = instance.evaluate();
        assertEquals("The test has failed. ",expResult, result);        
    }
    
    
    @Test
    public void testEvaluate_condition_greater_than() {
        System.out.println("testEvaluate_condition_greater_than");
        
        VolumeMovingAverage volume = new VolumeMovingAverage();
        volume.setPeriod(1);
        volume.evaluate(data);

        System.out.println("testEvaluate_condition_greater_than.volume avg = "+volume.getResult());
        
        StrategyRule instance = new StrategyRule();
        instance.setLeftOperand(new IndicatorOperandValue(volume));
        instance.setRightOperand(new DefaultOperandValue(new BigDecimal(1_0000)));
        instance.setComparisonOperator(ComparisonOperator.GREATER_THAN);
        
        boolean expResult = false;
        boolean result = instance.evaluate();
        assertEquals("The test has failed. ",expResult, result);        
    }
    
    @Test
    public void testEvaluate_condition_less_than() {
        System.out.println("testEvaluate_condition_less_than");
        
        VolumeMovingAverage volume = new VolumeMovingAverage();
        volume.setPeriod(1);
        volume.evaluate(data);

        System.out.println("testEvaluate_condition_less_than.volume avg = "+volume.getResult());
        
        StrategyRule instance = new StrategyRule();
        instance.setLeftOperand(new IndicatorOperandValue(volume));
        instance.setRightOperand(new DefaultOperandValue(new BigDecimal(10_0000)));        
        instance.setComparisonOperator(ComparisonOperator.LESS_THAN);
        
        boolean expResult = true;
        boolean result = instance.evaluate();
        assertEquals("The test has failed. ",expResult, result);        
    }
    
    @Test
    public void testEvaluate_idicator_SMA_condition_greater_than() {
        System.out.println("testEvaluate_idicator_SMA_condition_greater_than");
        
        List<Candlestick> data2 = new ArrayList<>();
        data2.add(new Candlestick(0.1, 0.1, 0.0, 0.0, 50_0000, Instant.EPOCH));
        
        SMA sma5 = new SMA(1, TimeFrame.EIGHT_HOURS);        
        SMA sma8 = new SMA(1, TimeFrame.EIGHT_HOURS);  
        sma5.evaluate(data2);
        sma8.evaluate(data);

        System.out.println("testEvaluate_idicator_SMA_condition_less_than.sma(5) = "+sma5.getResult());
        System.out.println("testEvaluate_idicator_SMA_condition_less_than.sma(8) = "+sma8.getResult());        
        
        StrategyRule instance = new StrategyRule();
        instance.setLeftOperand(new IndicatorOperandValue(sma5));
        instance.setRightOperand(new IndicatorOperandValue(sma8));        
        instance.setComparisonOperator(ComparisonOperator.GREATER_THAN);
        
        boolean expResult = true;
        boolean result = instance.evaluate();
        assertEquals("The test has failed. ",expResult, result);        
    }
    
    @Test
    public void testEvaluate_idicator_SMA_condition_less_than_false() {
        System.out.println("testEvaluate_idicator_SMA_condition_less_than_false");
        
        List<Candlestick> data2 = new ArrayList<>();
        data2.add(new Candlestick(0.1, 0.1, 0.0, 0.0, 50_0000, Instant.EPOCH));
        
        SMA sma5 = new SMA(1, TimeFrame.EIGHT_HOURS);        
        SMA sma8 = new SMA(1, TimeFrame.EIGHT_HOURS);  
        sma5.evaluate(data2);
        sma8.evaluate(data);

        System.out.println("testEvaluate_idicator_SMA_condition_less_than.sma(5) = "+sma5.getResult());
        System.out.println("testEvaluate_idicator_SMA_condition_less_than.sma(8) = "+sma8.getResult());
        
        StrategyRule instance = new StrategyRule();
        instance.setLeftOperand(new IndicatorOperandValue(sma5));
        instance.setRightOperand(new IndicatorOperandValue(sma8));
        instance.setComparisonOperator(ComparisonOperator.LESS_THAN);
        
        boolean expResult = false;
        boolean result = instance.evaluate();
        assertEquals("The test has failed. ",expResult, result);        
    }
}