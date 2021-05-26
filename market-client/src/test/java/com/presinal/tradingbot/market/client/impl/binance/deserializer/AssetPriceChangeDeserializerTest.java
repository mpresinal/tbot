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

package com.presinal.tradingbot.market.client.impl.binance.deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.presinal.tradingbot.market.client.types.AssetPriceChange;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetPriceChangeDeserializerTest {

    private static Gson gson;    
    private static final String CLASS_NAME = AssetPriceChangeDeserializerTest.class.getSimpleName();
    
    private static final Path JSON_FILE_ASSETS_PRICE_CHANGE = Paths.get("src", "test", "resources", "binance", "assets_price_change.json");
    private static final Path JSON_FILE_ASSETS_PRICE_CHANGE_R_BTC = Paths.get("src", "test", "resources", "binance", "assets_price_change_LTC-BTC.json");

    //assets_price_change-symbol_R-BTC
    
    @BeforeClass
    public static void setUp() throws Exception {
        final String METHOD = CLASS_NAME+ ".setUp() :: ";
        System.out.println(METHOD + "Enter");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AssetPriceChange.class, new com.presinal.tradingbot.market.client.impl.kucoin.deserializer.AssetPriceChangeDeserializer());
        gson = builder.create();
        System.out.println(METHOD + "Exit");
    }

    @Before
    public void beforeTest(){
        System.out.println("\n-----------------------------------------------------------------------\n");
    }
    
    @After
    public void afterTest(){
        System.out.println("\n-----------------------------------------------------------------------\n");
    }
    
    /**
     * Test of deserialize method, of class OrderBookDeserializer.
     */
    @Test
    public void testDeserialize_Array() {
        final String METHOD = CLASS_NAME+ ".testDeserialize_Array() :: ";
        System.out.println(METHOD + "Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_FILE_ASSETS_PRICE_CHANGE);
            JsonElement el = gson.fromJson(reader, JsonElement.class);
            
            System.out.println("\n\n\n");            
            
            AssetPriceChange[] assetsPriceChange = gson.fromJson(el.getAsJsonArray(), AssetPriceChange[].class);
            
            System.out.println("assetsPriceChange.length = " + assetsPriceChange.length);
            System.out.println("assetsPriceChange = " + Arrays.toString(assetsPriceChange));
            
            List<AssetPriceChange> list = Arrays.asList(assetsPriceChange);
            
            assertNotNull("The test has failed. candlesticks is null", assetsPriceChange);  
            
            int expected = 293;
            assertEquals("The test has failed. ", expected, assetsPriceChange.length);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println(METHOD + "End");
        }
    }
    
    @Test
    public void testDeserialize_singleObject() {
        final String METHOD = CLASS_NAME+ ".testDeserialize_singleObject() :: ";
        System.out.println(METHOD + "Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_FILE_ASSETS_PRICE_CHANGE_R_BTC);
            JsonElement el = gson.fromJson(reader, JsonElement.class);
            System.out.println("***el = " + el);
            AssetPriceChange assetPriceChange = gson.fromJson(el.getAsJsonObject(), AssetPriceChange.class);            
            System.out.println("assetPriceChange = " + assetPriceChange);
            
            assertNotNull("The test has failed. assetPriceChange is null", assetPriceChange);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println(METHOD + "End");
        }
    }
    

}