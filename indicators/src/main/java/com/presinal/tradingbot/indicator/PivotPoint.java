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
package com.presinal.tradingbot.indicator;

import java.util.List;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.indicator.result.PivotPointResult;
import com.presinal.tradingbot.indicator.util.NumberUtil;
import java.math.BigDecimal;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PivotPoint extends AbstractIndicator<PivotPointResult> {

    public static enum PivotType {
        TRADITIONAL, FIBONACCI, CLASSIC
    }

    private static final String NAME = "Pivot Point";
    private final int LEVELS = 5;
    
    private final PivotType type;
    private BigDecimal[] resistances;
    private BigDecimal[] supports;

    public PivotPoint() {
        this(PivotType.TRADITIONAL);        
    }

    public PivotPoint(PivotType type) {
        super(NAME);
        resistances = new BigDecimal[LEVELS];
        supports = new BigDecimal[LEVELS];
        this.type = type;
    }

    @Override
    public void evaluate(List<Candlestick> data) {
        if (data != null && !data.isEmpty()) {
            evaluate(data.get(data.size() - 1));
        }
    }

    public void evaluate(Candlestick previousDay) {

        BigDecimal low = BigDecimal.valueOf(previousDay.lowPrice);
        BigDecimal high = BigDecimal.valueOf(previousDay.highPrice);
        BigDecimal diffHL = high.subtract(low);

        BigDecimal pp = high.add(low)
                .add(BigDecimal.valueOf(previousDay.closePrice))
                .divide(BigDecimal.valueOf(3), NumberUtil.MATHCONTEXT);

        switch (type) {

            case FIBONACCI:
                computeFibonacci(previousDay, pp, diffHL);
                break;

            case CLASSIC:
                computeClassic(previousDay, pp, high, low, diffHL);
                break;

            default:
                computeTraditional(previousDay, pp, high, low, diffHL);
                break;

        }

        setResult(new PivotPointResult(pp, supports, resistances));
        notifyListeners();
    }

    /**
     * Formula:
     * <pre>
     * PP = (HIGHprev + LOWprev + CLOSEprev) / 3
     * R1 = PP * 2 - LOWprev
     * S1 = PP * 2 - HIGHprev
     * R2 = PP + (HIGHprev - LOWprev)
     * S2 = PP - (HIGHprev - LOWprev)
     * R3 = PP * 2 + (HIGHprev - 2 * LOWprev)
     * S3 = PP * 2 - (2 * HIGHprev - LOWprev)
     * R4 = PP * 3 + (HIGHprev - 3 * LOWprev)
     * S4 = PP * 3 - (3 * HIGHprev - LOWprev)
     * R5 = PP * 4 + (HIGHprev - 4 * LOWprev)
     * S5 = PP * 4 - (4 * HIGHprev - LOWprev)
     * </pre><br/>
     * Read more on:
     * https://www.tradingview.com/support/solutions/43000521824-pivot-points-standard/
     */
    public void computeTraditional(Candlestick previousDay, BigDecimal pp, BigDecimal high, BigDecimal low, BigDecimal diffHL) {

        // R1-S1 and R2-S2
        doCommonComputation(pp, high, low, diffHL);

        // R3 and S3
        computeTraditionalLevels(3, pp, high, low);

        // R4 and S4
        computeTraditionalLevels(4, pp, high, low);

        // R5 and S5
        computeTraditionalLevels(5, pp, high, low);

    }

    /**
     * Formula:
     * <pre>
     * PP = (HIGHprev + LOWprev + CLOSEprev) / 3
     * R1 = PP + 0.382 * (HIGHprev - LOWprev)
     * S1 = PP - 0.382 * (HIGHprev - LOWprev)
     * R2 = PP + 0.618 * (HIGHprev - LOWprev)
     * S2 = PP - 0.618 * (HIGHprev - LOWprev)
     * R3 = PP + (HIGHprev - LOWprev)
     * S3 = PP - (HIGHprev - LOWprev)
     * </pre><br/>
     * Read more on:
     * https://www.tradingview.com/support/solutions/43000521824-pivot-points-standard/
     */
    public void computeFibonacci(Candlestick previousDay, BigDecimal pp, BigDecimal diffHL) {

        // R1-S1
        computeFibonacciLevels(1, BigDecimal.valueOf(0.382), pp, diffHL);
        
        // R2-S2
        computeFibonacciLevels(2, BigDecimal.valueOf(0.618), pp, diffHL);

        // R3 and S3
        resistances[2] = pp.add(diffHL);
        supports[2] = pp.subtract(diffHL);

    }

    /**
     * Formula:
     * <pre>
     * PP = (HIGHprev + LOWprev + CLOSEprev) / 3
     * R1 = 2 * PP - LOWprev
     * S1 = 2 * PP - HIGHprev
     * R2 = PP + (HIGHprev - LOWprev)
     * S2 = PP - (HIGHprev - LOWprev)
     * R3 = PP + 2 * (HIGHprev - LOWprev)
     * S3 = PP - 2 * (HIGHprev - LOWprev)
     * R4 = PP + 3 * (HIGHprev - LOWprev)
     * S4 = PP - 3 * (HIGHprev - LOWprev)
     * </pre><br/>
     * Read more on:
     * https://www.tradingview.com/support/solutions/43000521824-pivot-points-standard/
     */
    public void computeClassic(Candlestick previousDay, BigDecimal pp, BigDecimal high, BigDecimal low, BigDecimal diffHL) {

        // R1-S1 and R2-S2
        doCommonComputation(pp, high, low, diffHL);

        // R3 and S3
        computeClassicLevels(3, pp, high, low, diffHL);

        // R4 and S4
        computeClassicLevels(4, pp, high, low, diffHL);

        // R5 and S5
        computeClassicLevels(5, pp, high, low, diffHL);

    }

    private void doCommonComputation(BigDecimal pp, BigDecimal high, BigDecimal low, BigDecimal diffHL) {
        BigDecimal doubledPP = BigDecimal.valueOf(2).multiply(pp);

        // R1 and S1
        resistances[0] = doubledPP.subtract(low);
        supports[0] = doubledPP.subtract(high);

        // R2 and S2
        resistances[1] = pp.add(diffHL);
        supports[1] = pp.subtract(diffHL);
    }

    private void computeFibonacciLevels(int level, BigDecimal fibonacciPoint, BigDecimal pp, BigDecimal diffHL) {
        int idx = level - 1;
        BigDecimal r = fibonacciPoint.multiply(diffHL);
        resistances[idx] = pp.add(r);
        supports[idx] = pp.subtract(r);
    }
    
    private void computeTraditionalLevels(int level, BigDecimal pp, BigDecimal high, BigDecimal low) {
        int idx = level - 1;
        BigDecimal x = BigDecimal.valueOf(idx).multiply(pp);
        resistances[idx] = x.add(high.subtract(BigDecimal.valueOf(idx).multiply(low)));
        supports[idx] = x.subtract(high.multiply(BigDecimal.valueOf(idx)).subtract(low));
    }

    private void computeClassicLevels(int level, BigDecimal pp, BigDecimal high, BigDecimal low, BigDecimal diffHL) {
        int idx = level - 1;
        resistances[idx] = pp.add(BigDecimal.valueOf(idx).multiply(diffHL));
        supports[idx] = pp.subtract(BigDecimal.valueOf(idx).multiply(diffHL));
    }

    public PivotType getType() {
        return type;
    }
}
