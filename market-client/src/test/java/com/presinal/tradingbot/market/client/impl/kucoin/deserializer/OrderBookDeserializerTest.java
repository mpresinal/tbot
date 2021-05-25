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

package com.presinal.tradingbot.market.client.impl.kucoin.deserializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.presinal.tradingbot.market.client.types.OrderBook;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class OrderBookDeserializerTest {

    private static Gson gson;
    private static final Path JSON_DIR_PATH = Paths.get("src", "test", "resources", "kucoin");
    private static final String CLASS_NAME = OrderBookDeserializerTest.class.getSimpleName();
    
    @BeforeClass
    public static void setUp() throws Exception {
        final String METHOD = CLASS_NAME+ ".setUp() :: ";
        System.out.println(METHOD + "Enter");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(OrderBook.class, new OrderBookDeserializer());
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
    public void testDeserialize_notNull() {
        final String METHOD = CLASS_NAME+ ".testDeserialize_notNull() :: ";
        System.out.println(METHOD + "Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("order_book.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);
            System.out.println("***el = " + el);
            OrderBook orderBook = gson.fromJson(el.getAsJsonObject().get("data"), OrderBook.class);
            System.out.println("orderBook = " + orderBook);
            assertNotNull("The test has failed.", orderBook);            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println(METHOD + "End");
        }
    }
    
    @Test
    public void testDeserialize_emptySubArray() {
        final String METHOD = CLASS_NAME+ ".testDeserialize_emptySubArray() :: ";
        System.out.println(METHOD + "Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("order_book_empty-subarray.json"));
            OrderBook orderBook = gson.fromJson(reader, OrderBook.class);
            System.out.println("orderBook = " + orderBook);
            assertNotNull("The test has failed.", orderBook);          
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println(METHOD + "End");
        }
    }
}
