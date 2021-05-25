/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.indicator.result.FibonacciRetracementResult;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class FibonacciRetracementTest {

    @Test
    public void testUptrend() {
        
        List<Candlestick> data = new ArrayList<>();
        data.add(new Candlestick(00.00, 00.00, 0.27970, 0.30474, 10.00, Instant.now()));
        data.add(new Candlestick(00.00, 00.00, 0.63286, 0.78126, 15.00, Instant.now()));
        
        FibonacciRetracement fiboIndicator = new FibonacciRetracement();
        fiboIndicator.evaluate(data);
        FibonacciRetracementResult result = fiboIndicator.getResult();        
        System.out.println(result);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.uptrend);
        Assert.assertEquals(result.level23, new BigDecimal("0.66289184"));
        Assert.assertEquals(result.level38, new BigDecimal("0.58966408"));
        Assert.assertEquals(result.level50, new BigDecimal("0.530480"));
        Assert.assertEquals(result.level61, new BigDecimal("0.47129592"));
        Assert.assertEquals(result.level78, new BigDecimal("0.38703384"));
    }
    
    
    @Test
    public void testDonwtrend() {
        
        List<Candlestick> data = new ArrayList<>();
        data.add(new Candlestick(00.00, 00.00, 0.63286, 0.78126, 15.00, Instant.now()));
        data.add(new Candlestick(00.00, 00.00, 0.27970, 0.30474, 10.00, Instant.now()));
        
        
        FibonacciRetracement fiboIndicator = new FibonacciRetracement();
        fiboIndicator.evaluate(data);
        FibonacciRetracementResult result = fiboIndicator.getResult();        
        System.out.println(result);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.uptrend);
        Assert.assertEquals(result.level23, new BigDecimal("0.39806816"));
        Assert.assertEquals(result.level38, new BigDecimal("0.47129592"));
        Assert.assertEquals(result.level50, new BigDecimal("0.530480"));
        Assert.assertEquals(result.level61, new BigDecimal("0.58966408"));
        Assert.assertEquals(result.level78, new BigDecimal("0.67392616"));
    }
}
