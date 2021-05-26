/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class EMATest {

    private static List<Candlestick> data;

    @Test
    public void testEMA12() {
        EMA ema = new EMA(12, TimeFrame.ONE_DAY);
        ema.evaluate(data.subList(0, 13));
        BigDecimal result = ema.getResult();
        BigDecimal expected = BigDecimal.valueOf(29.42949);
        System.out.println(ema);        
        Assert.assertNotNull(result);
        System.out.println(result.round(MathContext.DECIMAL32));
        Assert.assertEquals(expected, result.round(MathContext.DECIMAL32));
    }

    @Test
    public void testEMA26() {
        EMA ema = new EMA(26, TimeFrame.ONE_DAY);
        ema.evaluate(data.subList(0, 27));
        BigDecimal result = ema.getResult();
        BigDecimal expected = BigDecimal.valueOf(39.08974);
        System.out.println(ema);
        Assert.assertNotNull(result);        
        //System.out.println(result.setScale(2, RoundingMode.HALF_EVEN).round(new MathContext(8, RoundingMode.HALF_EVEN)));        
        System.out.println(result.round(MathContext.DECIMAL32));        
        Assert.assertEquals(expected, result.round(MathContext.DECIMAL32));
    }

    @BeforeClass
    public static void setup() {
        data = DataUtil.data;
    }
}
