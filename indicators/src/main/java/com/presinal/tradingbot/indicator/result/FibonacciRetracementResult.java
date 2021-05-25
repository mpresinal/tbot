/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator.result;

import java.math.BigDecimal;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class FibonacciRetracementResult implements Comparable<FibonacciRetracementResult> {

    public final BigDecimal level23;
    public final BigDecimal level38;
    public final BigDecimal level50;
    public final BigDecimal level61;
    public final BigDecimal level78;
    public final boolean uptrend;

    public FibonacciRetracementResult(BigDecimal level23, BigDecimal level38, BigDecimal level50, BigDecimal level61, BigDecimal level78, boolean uptrend) {
        this.level23 = level23;
        this.level38 = level38;
        this.level50 = level50;
        this.level61 = level61;
        this.level78 = level78;
        this.uptrend = uptrend;
    }

    @Override
    public String toString() {
        return "Price trend: " + (uptrend? "up" : "down") + ", 0.236(" + level23 + "), 0.382(" + level38 + "), 0.50(" + level50 + "), 0.618(" + level61 + "), 0.786(" + level78 + ')';
    } 

    @Override
    public int compareTo(FibonacciRetracementResult o) {
        return this.toString().compareTo(o.toString());
    }

}
