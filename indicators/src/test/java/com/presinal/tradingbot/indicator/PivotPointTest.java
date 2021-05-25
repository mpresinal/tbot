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

import com.presinal.tradingbot.indicator.result.PivotPointResult;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class PivotPointTest {

    private static final String CLASS_NAME = PivotPointTest.class.getSimpleName();

    /* 
        Candlestick data for BTCUSDT from date 2020-12-12
        high = 18948.66
        Low = 18020.70
        Close = 18808.69
    */
    private static final Candlestick data = new Candlestick(0.00, 18808.69, 18020.70, 18948.66, 150.05, Instant.now());

    /**
     * Test of getResult method, of class PivotPoint.
     */
    @Test
    public void testTradictional() {
        final String METHOD = CLASS_NAME + ".testTradictional() :: ";

        System.out.println(METHOD + "Start");

        PivotPoint instance = new PivotPoint();        
        instance.evaluate(data);

        PivotPointResult result = instance.getResult();
        System.out.println(METHOD + "result = " + result);
        BigDecimal expectedPP = new BigDecimal("18592.68333333333");
        System.out.println(METHOD + "expectedPP = " + expectedPP);
        
        Assert.assertNotNull("The test has failed. Invalid response: result is null", result);
        assertEquals("The test has failed. No expected value", expectedPP, result.pivotPoint);

        System.out.println(METHOD + "End");
    }

    @Test
    public void testFibonacci() {
        final String METHOD = CLASS_NAME + ".testFibonacci() :: ";

        System.out.println(METHOD + "Start");
        
        PivotPoint instance = new PivotPoint(PivotPoint.PivotType.FIBONACCI);        
        instance.evaluate(data);

        PivotPointResult result = instance.getResult();
        System.out.println(METHOD + "result = " + result);
        
        /*
         pp value is the same for all type. For testing porpuse, the S1 wil be comprared
        */
        BigDecimal expectedS1 = new BigDecimal("18238.20261333333"); 
        
        Assert.assertNotNull("The test has failed. Invalid response: result is null", result);
        assertEquals("The test has failed. No expected value", expectedS1, result.supports[0]);

        System.out.println(METHOD + "End");
    }
}
