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

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import static junit.framework.TestCase.assertEquals;
import org.junit.Test;
import com.presinal.tradingbot.market.client.types.Candlestick;
import com.presinal.tradingbot.indicator.util.NumberUtil;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class PivotPointTest {
    
    private static final String CLASS_NAME = PivotPointTest.class.getSimpleName();
    

    /**
     * Test of getResult method, of class PivotPoint.
     */
    @Test
    public void testEvalueList_normalFlow() {
        final String METHOD = CLASS_NAME+ ".testEvalueList_normalFlow() :: ";
        
        System.out.println(METHOD + "Start");
        
        PivotPoint instance = new PivotPoint();
        List<Candlestick> list = new ArrayList<>();
        list.add(new Candlestick(0.000086440, 0.0000089130, 0.0000085730, 0.0000092130, 150.05, Instant.now()));
        instance.evaluate(list);        
       
        PivotPoint.PivotPointResult result = instance.getResult();
        System.out.println(METHOD + "result = "+result);
        
        double expectedPP = 10.666;
        System.out.println(METHOD + " expectedPP = "+NumberUtil.round(expectedPP,2));
        
        DecimalFormat df = new DecimalFormat("#.#########");
        System.out.println(METHOD + " result.pivotPoint = "+df.format(result.pivotPoint) );
        System.out.println(METHOD + " s3 = "+df.format(result.supports[2]) );
        System.out.println(METHOD + " result.pivotPoint = "+NumberUtil.round(result.pivotPoint,10));
        
        Assert.assertNotNull("The test has failed. Invalid response: result is null",result);
        //assertEquals("The test has failed. No expected value", expectedPP, result.pivotPoint);

        
        System.out.println(METHOD + "End");
    }

}
