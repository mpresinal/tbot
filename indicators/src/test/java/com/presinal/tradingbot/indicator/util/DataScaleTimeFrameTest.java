/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator.util;

import com.presinal.tradingbot.indicator.DataUtil;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DataScaleTimeFrameTest {

    @Test(expected = IllegalArgumentException.class)
    public void testScaleToTimeFrameIllegalArgumentException() {
        DataScaleTimeFrame.scaleToTimeFrame(null, TimeFrame.FIVE_MINUTES, TimeFrame.FIFTEEN_MINUTES);
        fail("The test has failed. Invalid flow");
    }
    
    @Test(expected = NullPointerException.class)
    public void testScaleToTimeFrameNPE() {
        DataScaleTimeFrame.scaleToTimeFrame(DataUtil.data, null, TimeFrame.FIFTEEN_MINUTES);
        fail("The test has failed. Invalid flow");
    }
    
    @Test
    public void testScaleToTimeFrameFrom15To30() {
        List<Candlestick> list = DataScaleTimeFrame.scaleToTimeFrame(DataUtil.data, TimeFrame.FIFTEEN_MINUTES, TimeFrame.THIRTY_MINUTES);
        int expectedSize = (int) Math.round(DataUtil.data.size() / 2.0);
        double volume = DataUtil.data.get(0).volume + DataUtil.data.get(1).volume;
        double openPrice = DataUtil.data.get(0).openPrice;
        double closePrice = DataUtil.data.get(1).closePrice;
        double higher = DataUtil.data.get(0).highPrice;
        double lower = DataUtil.data.get(1).lowPrice;
        
        assertNotNull("The test has failed. list is null", list);
        assertEquals("The test has failed. The resulting list size is not equal to data list half size", list.size(), expectedSize);
        
        assertEquals("The test has failed. Not expected open Price value ", list.get(0).openPrice, openPrice);
        assertEquals("The test has failed. Not expected close Price value ", list.get(0).closePrice, closePrice);
        assertEquals("The test has failed. Not expected volume value ", list.get(0).volume, volume);        
        assertEquals("The test has failed. Not expected higer price value ", list.get(0).highPrice, higher);
        assertEquals("The test has failed. Not expected lower price value ", list.get(0).lowPrice, lower);
        
    }
    
    @Test
    public void testScaleToTimeFrameFrom5To15() {
        List<Candlestick> list = DataScaleTimeFrame.scaleToTimeFrame(DataUtil.data, TimeFrame.FIVE_MINUTES, TimeFrame.FIFTEEN_MINUTES);
        int expectedSize = (int) Math.round(DataUtil.data.size() / 3.0);
        double volume = DataUtil.data.get(0).volume + DataUtil.data.get(1).volume + DataUtil.data.get(2).volume;        
        double openPrice = DataUtil.data.get(0).openPrice;
        double closePrice = DataUtil.data.get(2).closePrice;
        double higher = DataUtil.data.get(2).highPrice;
        double lower = DataUtil.data.get(1).lowPrice;
        
        assertNotNull("The test has failed. list is null", list);
        assertEquals("The test has failed. The resulting list size is not equal to data list half size", list.size(), expectedSize);
        
        assertEquals("The test has failed. Not expected open Price value ", list.get(0).openPrice, openPrice);
        assertEquals("The test has failed. Not expected close Price value ", list.get(0).closePrice, closePrice);
        assertEquals("The test has failed. Not expected volume value ", list.get(0).volume, volume);        
        assertEquals("The test has failed. Not expected higer price value ", list.get(0).highPrice, higher);
        assertEquals("The test has failed. Not expected lower price value ", list.get(0).lowPrice, lower);
        
    }
}
