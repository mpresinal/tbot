/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presinal.tradingbot.indicator.result;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PivotPointResult implements Comparable<PivotPointResult> {

    public final BigDecimal pivotPoint;
    public final BigDecimal[] supports;
    public final BigDecimal[] resistance;

    public PivotPointResult(BigDecimal pivotPoint, BigDecimal[] supports, BigDecimal[] resistance) {
        this.pivotPoint = pivotPoint;
        this.supports = supports;
        this.resistance = resistance;
    }

    @Override
    public String toString() {
        return "PivotPointResult{" + "pivotPoint=" + pivotPoint
                + ", supports=" + (supports != null ? Arrays.toString(supports) : null)
                + ", resistance=" + (resistance != null ? Arrays.toString(resistance) : null) + '}';
    }

    @Override
    public int compareTo(PivotPointResult o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}