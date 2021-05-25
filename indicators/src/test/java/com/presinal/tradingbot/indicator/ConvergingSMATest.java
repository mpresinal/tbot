/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.indicator.result.ConvergingResult;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class ConvergingSMATest {

    @Test
    public void test() {
        List<Candlestick> data = new ArrayList<>();
        data.add(new Candlestick(5.00, 5.00, 5.00, 5.00, 5.00, Instant.now()));
        data.add(new Candlestick(5.00, 10.00, 5.00, 10.00, 10.00, Instant.now()));
        data.add(new Candlestick(10.00, 4.00, 4.00, 11.00, 11.00, Instant.now()));
        
        data.add(new Candlestick(5.00, 10.00, 5.00, 12.00, 5.00, Instant.now()));
        data.add(new Candlestick(10.00, 10.00, 10.00, 10.00, 10.00, Instant.now()));
        data.add(new Candlestick(10.00, 10.00, 10.00, 10.00, 10.00, Instant.now()));
        
        
        TimeFrame[] convergingTimeFrame = new TimeFrame[] {TimeFrame.THREE_MINUTES, TimeFrame.FIVE_MINUTES};
        ConvergingSMA ind = new ConvergingSMA(convergingTimeFrame, new int[] {2, 1});
        ind.setTimeFrame(TimeFrame.ONE_MINUTE);
        ind.evaluate(data);
        ConvergingResult result = ind.getResult();
    }
}
