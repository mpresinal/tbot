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

package com.presinal.tradingbot.market.client.impl.binance;

import java.time.Instant;
import java.util.List;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import org.junit.Test;
import com.presinal.tradingbot.market.client.enums.TimeFrame;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.Candlestick;
import org.junit.Ignore;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
@Ignore
public class BinanceMarketClientTest {

    @Test
    public void testLoadAssetsPriceChange() throws Exception {
        
        System.out.println("loadAssetsPriceChange() Start");
        BinanceMarketClient instance = new BinanceMarketClient(BinanceMarketClient.API_URL, null, "testing");         
        
        List<Candlestick> result = instance.loadCandlestick(new AssetPair("ETH", "BTC"), TimeFrame.ONE_DAY, (Instant)null, null, 100);
        System.out.println("testLoadAssetsPriceChange() result = "+result); 
        
        assertNotNull("The test has failed. result is null ", result);
        assertFalse("The test has failed. ask list is empty.", result.isEmpty());
       
        System.out.println("loadAssetsPriceChange() End");
    }
}
