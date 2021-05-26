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

package com.presinal.tradingbot.market.client.impl.kucoin;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import com.presinal.tradingbot.market.client.types.AssetPair;
import com.presinal.tradingbot.market.client.types.AssetPriceChange;
import com.presinal.tradingbot.market.client.types.OrderBook;
import org.junit.Ignore;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
@Ignore
public class KucoinMarketClientTest {


    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
//
//    /**
//     * Test of registerTypeDeserializers method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testRegisterTypeDeserializers() {
//        System.out.println("registerTypeDeserializers");
//        GsonBuilder builder = null;
//        KucoinMarketClient instance = null;
//        instance.registerTypeDeserializers(builder);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of testConnection method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testTestConnection() throws Exception {
//        System.out.println("testConnection");
//        KucoinMarketClient instance = null;
//        boolean expResult = false;
//        boolean result = instance.testConnection();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of loadMarketInfo method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testLoadMarketInfo() {
//        System.out.println("loadMarketInfo");
//        KucoinMarketClient instance = null;
//        instance.loadMarketInfo();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of loadRecentTrades method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testLoadRecentTrades() {
//        System.out.println("loadRecentTrades");
//        KucoinMarketClient instance = null;
//        instance.loadRecentTrades();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of loadOrderBook method, of class KucoinMarketClient.
     */
    @Test
    public void testLoadOrderBook() throws Exception {
        System.out.println("loadOrderBook() Start");
        AssetPair assetPair = null;
        int limit = 0;
        KucoinMarketClient instance = new KucoinMarketClient(KucoinMarketClient.API_URL, null, "testing");        
        OrderBook result = instance.loadOrderBook(new AssetPair("R", "BTC"), limit);
        
        //System.out.println("loadOrderBook() result = "+result); 
        
        assertNotNull("The test has failed. result is null ", result);
        assertFalse("The test has failed. ask list is empty.", result.getAsks().isEmpty());

        System.out.println("loadOrderBook() End");
    }

//    /**
//     * Test of loadCandlestick method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testLoadCandlestick() throws Exception {
//        System.out.println("loadCandlestick");
//        AssetPair assetPair = null;
//        TimeFrame timeFrame = null;
//        Date startDate = null;
//        Date endDate = null;
//        int limit = 0;
//        KucoinMarketClient instance = null;
//        List<Candlestick> expResult = null;
//        List<Candlestick> result = instance.loadCandlestick(assetPair, timeFrame, startDate, endDate, limit);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
    /**
     * Test of loadAssetsPriceChange method, of class KucoinMarketClient.
     */
    @Test
    public void testLoadAssetsPriceChange() throws Exception {
        
        System.out.println("loadAssetsPriceChange() Start");
        KucoinMarketClient instance = new KucoinMarketClient(KucoinMarketClient.API_URL, null, "testing");         
        
        List<AssetPriceChange> result = instance.loadAssetsPriceChange();
        //System.out.println("loadAssetsPriceChange() result = "+result); 
        
        assertNotNull("The test has failed. result is null ", result);
        assertFalse("The test has failed. ask list is empty.", result.isEmpty());
       
        System.out.println("loadAssetsPriceChange() End");
    }

    /**
     * Test of getAssetPriceChange method, of class KucoinMarketClient.
     */
    @Test
    public void testGetAssetPriceChange() throws Exception {
        System.out.println("getAssetPriceChange() Enter");
        KucoinMarketClient instance = new KucoinMarketClient(KucoinMarketClient.API_URL, null, "testing"); 

        AssetPriceChange result = instance.getAssetPriceChange(new AssetPair("R", "BTC"));
        System.out.println("loadAssetPriceChange() result = "+result); 
        assertNotNull("The test has failed. result is null ", result);
        assertTrue("The test has failed. result ask price is null or empty ", result.getAskPrice() > -1);
        System.out.println("getAssetPriceChange() Enter");

    }
//
//    /**
//     * Test of getAssetPrice method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testGetAssetPrice() throws Exception {
//        System.out.println("getAssetPrice");
//        AssetPair asset = null;
//        KucoinMarketClient instance = null;
//        double expResult = 0.0;
//        double result = instance.getAssetPrice(asset);
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of placeBuyOrder method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testPlaceBuyOrder() throws Exception {
//        System.out.println("placeBuyOrder");
//        AssetPair asset = null;
//        double price = 0.0;
//        double quantity = 0.0;
//        OrderType type = null;
//        KucoinMarketClient instance = null;
//        Order expResult = null;
//        Order result = instance.placeBuyOrder(asset, price, quantity, type);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of placeSellOrder method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testPlaceSellOrder() throws Exception {
//        System.out.println("placeSellOrder");
//        AssetPair asset = null;
//        double price = 0.0;
//        double quantity = 0.0;
//        OrderType type = null;
//        KucoinMarketClient instance = null;
//        Order expResult = null;
//        Order result = instance.placeSellOrder(asset, price, quantity, type);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of getOrderStatus method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testGetOrderStatus() throws Exception {
//        System.out.println("getOrderStatus");
//        AssetPair asset = null;
//        String orderId = "";
//        KucoinMarketClient instance = null;
//        OrderStatus expResult = null;
//        OrderStatus result = instance.getOrderStatus(asset, orderId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of cancelOrder method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testCancelOrder() throws Exception {
//        System.out.println("cancelOrder");
//        String orderId = "";
//        KucoinMarketClient instance = null;
//        boolean expResult = false;
//        boolean result = instance.cancelOrder(orderId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of loadOpenedOrders method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testLoadOpenedOrders() throws Exception {
//        System.out.println("loadOpenedOrders");        
//        /*KucoinMarketClient instance = new KucoinMarketClient(KucoinMarketClient.API_URL, null, "testing");
//        OpenedOrder result = instance.loadOpenedOrders(new AssetPair("R", "BTC"));
//        System.out.println("loadOpenedOrders():: result = "+result);
//        assertNotNull("The test has failed. ", result);
//        assertFalse("The test has failed. buy list is empty.", result.getBuyOrders().isEmpty());*/
//        
//    }
//
//    /**
//     * Test of getAccountBalance method, of class KucoinMarketClient.
//     */
//    @Test
//    public void testGetAccountBalance() throws Exception {
//        System.out.println("getAccountBalance");
//        KucoinMarketClient instance = null;
//        AccountBalance expResult = null;
//        AccountBalance result = instance.getAccountBalance();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}