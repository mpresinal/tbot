/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.indicator.result.MACDResult;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class MACDTest {

    @Test
    public void test() {
        MACD indicator = new MACD();
        indicator.evaluate(DataUtil.data);
        MACDResult result = indicator.getResult();
        System.out.println(indicator);
        Assert.assertNotNull(result);        
    }
}
