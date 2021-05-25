/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.market.client.types.Candlestick;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DataUtil {

    public static final List<Candlestick> data;
    
    static {
        data = new ArrayList<>();
        data.add(new Candlestick(9.00, 10.00, 8.00, 10.00, 10.00, Instant.now()));
        data.add(new Candlestick(10.00, 5.00, 5.00, 10.00, 5.00, Instant.now()));
        data.add(new Candlestick(5.00, 30.00, 05.00, 30.00, 30.00, Instant.now()));
        data.add(new Candlestick(25.00, 25.00, 25.00, 25.00, 25.00, Instant.now()));
        data.add(new Candlestick(50.00, 50.00, 50.00, 50.00, 50.00, Instant.now()));
        data.add(new Candlestick(45.00, 45.00, 45.00, 45.00, 45.00, Instant.now()));
        data.add(new Candlestick(32.00, 32.00, 32.00, 32.00, 32.00, Instant.now()));
        data.add(new Candlestick(25.00, 25.00, 25.00, 25.00, 25.00, Instant.now()));
        data.add(new Candlestick(28.00, 28.00, 28.00, 28.00, 28.00, Instant.now()));
        data.add(new Candlestick(31.00, 31.00, 31.00, 31.00, 31.00, Instant.now()));
        data.add(new Candlestick(29.00, 29.00, 29.00, 29.00, 29.00, Instant.now()));
        data.add(new Candlestick(31.00, 31.00, 31.00, 31.00, 31.00, Instant.now()));
        data.add(new Candlestick(35.00, 35.00, 35.00, 35.00, 35.00, Instant.now()));
        data.add(new Candlestick(36.00, 36.00, 36.00, 36.00, 36.00, Instant.now()));
        data.add(new Candlestick(37.00, 37.00, 37.00, 37.00, 37.00, Instant.now()));
        data.add(new Candlestick(40.00, 40.00, 40.00, 40.00, 40.00, Instant.now()));
        data.add(new Candlestick(43.00, 43.00, 43.00, 43.00, 43.00, Instant.now()));
        data.add(new Candlestick(45.00, 45.00, 45.00, 45.00, 45.00, Instant.now()));
        data.add(new Candlestick(48.00, 48.00, 48.00, 48.00, 48.00, Instant.now()));
        data.add(new Candlestick(47.00, 47.00, 47.00, 47.00, 47.00, Instant.now()));
        data.add(new Candlestick(48.00, 48.00, 48.00, 48.00, 48.00, Instant.now()));
        data.add(new Candlestick(49.00, 49.00, 49.00, 49.00, 49.00, Instant.now()));
        data.add(new Candlestick(50.00, 50.00, 50.00, 50.00, 50.00, Instant.now()));
        data.add(new Candlestick(51.00, 51.00, 51.00, 51.00, 51.00, Instant.now()));
        data.add(new Candlestick(53.00, 53.00, 53.00, 53.00, 53.00, Instant.now()));
        data.add(new Candlestick(54.00, 54.00, 54.00, 54.00, 54.00, Instant.now()));
        data.add(new Candlestick(58, 58.00, 58.00, 58.00, 58.00, Instant.now()));
    }
}
