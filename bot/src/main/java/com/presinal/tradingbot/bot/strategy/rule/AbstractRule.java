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

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public abstract class AbstractRule<T extends Comparable> implements Rule{

    private ComparisonOperator comparisonOperator;
    private OperandValue<T> leftOperand;
    private OperandValue<T> rightOperand;
    
    protected boolean doEvalLogic(int comparisonResult ){
         switch(comparisonOperator) {
            case EQUAL:
                return comparisonResult == 0;
                
            case LESS_EQUAL_THAN:                
                return comparisonResult <= 0;
                
            case LESS_THAN:                
                return comparisonResult < 0;
                
            case GREATER_EQUAL_THAN:                
                return comparisonResult >= 0;
                
            case GREATER_THAN:
                return comparisonResult > 0;
                
            default:
                return false;                
        }
    }
 
    
    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(ComparisonOperator comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }    

    public OperandValue<T> getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(OperandValue<T> leftOperand) {
        this.leftOperand = leftOperand;
    }

    public OperandValue<T> getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(OperandValue<T> rightOperand) {
        this.rightOperand = rightOperand;
    }
    
    @Override
    public String toString() {
        return operandValueToString(leftOperand)+" "+comparisonOperator.toStringOperator()+" "+operandValueToString(rightOperand);
    }
    
    private String operandValueToString(OperandValue operandVal) {
        if (operandVal != null) {
            return operandVal.readValue().toString();
        } else {
            return null;
        }        
    }
}
