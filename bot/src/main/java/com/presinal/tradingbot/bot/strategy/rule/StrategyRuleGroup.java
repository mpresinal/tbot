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

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class StrategyRuleGroup implements Rule{

    private LogicalOperator logicalOperator;
    private Set<Rule> rules;

    @Override
    public boolean evaluate() {
        Boolean result = null;

        /*System.out.println("StrategyRuleGroup.logicalOperator = "+logicalOperator);
        System.out.println("StrategyRuleGroup.rules = "+rules);*/
        
        for (Rule rule : rules) {
            //System.out.println("StrategyRuleGroup.procesign rule = "+rule);
            
            if (result == null) {

                result = rule.evaluate();
                if (logicalOperator == LogicalOperator.OR && result) {
                    //System.out.println("StrategyRuleGroup. breakin loop result is true and operator is or");
                    break;
                }

            } else {

                if (logicalOperator == LogicalOperator.AND) {
                    /*System.out.println("StrategyRuleGroup.else: continue loop result is true and operator is and");
                    System.out.println("StrategyRuleGroup.else: result before = "+result);*/
                    result = rule.evaluate() && result;
                    //System.out.println("StrategyRuleGroup.else: result after = "+result);

                } else if (logicalOperator == LogicalOperator.OR && result) {
                    //System.out.println("StrategyRuleGroup.else: breakin loop result is true and operator is or");
                    break;
                }
            }

        }

        return result != null ? result : false;
    }

    private boolean isAndOperator(){
        return logicalOperator == LogicalOperator.AND;
    }
    
    private boolean isOrOperator(){
        return logicalOperator == LogicalOperator.OR;
    }
    
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    public void setLogicalOperator(LogicalOperator logicalOperator) {
        this.logicalOperator = logicalOperator;
    }

    public Set<Rule> getRules() {
        if(rules == null){
            rules = new HashSet<>();
        }
        
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public String toString() {       
        final StringBuilder buffer = new StringBuilder();
        String strLogiOp = " "+logicalOperator +" ";
        getRules().forEach(r -> {
            buffer.append(r).append(strLogiOp);
        });
        
        return (buffer.length() > 0)? buffer.substring(0, buffer.lastIndexOf(strLogiOp)) : super.toString();
    }
    
    
}
