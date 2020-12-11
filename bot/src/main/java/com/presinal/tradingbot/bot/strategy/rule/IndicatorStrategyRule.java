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

import java.util.Objects;
import com.presinal.tradingbot.indicator.AbstractIndicator;
import com.presinal.tradingbot.indicator.Indicator;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class IndicatorStrategyRule extends AbstractRule<AbstractIndicator> {

    @Override
    public boolean evaluate() throws IllegalStateException {
        AbstractIndicator leftOperand = getLeftOperand();
        AbstractIndicator rightOperand = getRightOperand();
        
        if(Objects.isNull(leftOperand) && Objects.isNull(rightOperand)) {
            return false;
        }      
        
        return doEvalLogic(leftOperand.compareTo(rightOperand));        
    }
    
    public boolean evaluatex() throws IllegalStateException {
        Comparable leftOperand = getLeftOperand();
        Comparable rightOperand = getRightOperand();
        
        if(Objects.isNull(leftOperand) && Objects.isNull(rightOperand)) {
            return false;
        }
        
        if( !(leftOperand instanceof Indicator) || !(rightOperand instanceof Indicator)) {
            throw new IllegalStateException("Both left operand and right operand must be an Indicator implementation");
        }        
        
        int result = 0;       
        
        AbstractIndicator indLeftOperand = (AbstractIndicator) leftOperand;
        AbstractIndicator indRightOperand = (AbstractIndicator) rightOperand;
        
       /* System.out.println("IndicatorStrategyRule.indLeftOperand = "+indLeftOperand);
        System.out.println("IndicatorStrategyRule.indRightOperand = "+indRightOperand);
        System.out.println("IndicatorStrategyRule.comparisonOperator = "+getComparisonOperator());*/
        
        switch(getComparisonOperator()) {
            case EQUAL:
                return indLeftOperand.compareTo(indRightOperand) == 0;
                
            case LESS_EQUAL_THAN:
                result = indLeftOperand.compareTo(indRightOperand);
                return result <= 0;
                
            case LESS_THAN:
                result = indLeftOperand.compareTo(indRightOperand);
                return result < 0;
                
            case GREATER_EQUAL_THAN:
                result = indLeftOperand.compareTo(indRightOperand);
                return result >= 0;
                
            case GREATER_THAN:
                result = indLeftOperand.compareTo(indRightOperand);
                return result > 0;
                
            default:
                return false;
                
        }
        
    }

}
