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

package com.presinal.tradingbot.bot.strategy.rule.definitions;

import com.presinal.tradingbot.bot.strategy.rule.Rule;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
@Deprecated(forRemoval = true)
public class IndicatorStrategyRuleDefinition implements StrategyRuleDefinition {

    private String leftOpererandId;
    private String rightOpererandId;
    private String comparisonOperator;
    private String definitionFor;
    
    public String getLeftOpererandId() {
        return leftOpererandId;
    }

    public void setLeftOpererandId(String leftOpererandId) {
        this.leftOpererandId = leftOpererandId;
    }

    public String getRightOpererandId() {
        return rightOpererandId;
    }

    public void setRightOpererandId(String rightOpererandId) {
        this.rightOpererandId = rightOpererandId;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(String comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public String getDefinitionFor() {
        return definitionFor;
    }

    public void setDefinitionFor(String definitionFor) {
        this.definitionFor = definitionFor;
    }
}
