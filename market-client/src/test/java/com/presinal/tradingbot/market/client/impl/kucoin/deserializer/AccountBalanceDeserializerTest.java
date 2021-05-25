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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.presinal.tradingbot.market.client.types.AccountBalance;
import com.presinal.tradingbot.market.client.types.AccountBalance.Balance;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AccountBalanceDeserializerTest {

    private static Gson gson;
    private static final Path JSON_DIR_PATH = Paths.get("src", "test", "resources", "kucoin");

    @BeforeClass
    public static void setUp() throws Exception {
        System.out.println("AccountBalanceDeserializerTest.setUp() :: Enter");
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(AccountBalance.class, new AccountBalanceDeserializer());
        gson = builder.create();
        System.out.println("AccountBalanceDeserializerTest.setUp() :: Exit");
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
     * Test of deserialize method, of class AccountBalanceDeserializer.
     */
    @Test
    public void testDeserialize_notNull() {
        System.out.println("testDeserialize_notNull() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balance.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);
            System.out.println("accountBalance = " + accountBalance);
            assertNotNull("The test has failed.", accountBalance);            
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println("testDeserialize_assetBalance() End");
        }
    }
    
    @Test
    public void testDeserialize_balanceListSize() {
        System.out.println("testDeserialize_balanceListSize() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balance.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);

            System.out.println("accountBalance = " + accountBalance);
            
            int expected = 1;
            int balanceSize = accountBalance.getAssetList().size();
            
            System.out.println("balanceSize = " + balanceSize);
            
            assertEquals("The test has failed. expected list size " + expected + " but got " + balanceSize, expected, balanceSize);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println("testDeserialize_assetBalance() End");
        }
    }
    
    @Test
    public void testDeserialize_btcBalance() {        
        System.out.println("testDeserialize_assetBalance() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balance.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);
            System.out.println("accountBalance = " + accountBalance);
            
            Balance btcBalance = accountBalance.getBalanceFor("BTC");
            
            System.out.println("btcBalance = " + btcBalance);
            
            assertNotNull("The test has failed.", btcBalance);
            
            double expected = 321321.55;
            
            System.out.println("btcBalance.reserved = " + btcBalance.reserved);

            assertEquals("The test has failed", expected, btcBalance.reserved);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println("testDeserialize_assetBalance() End");
        }
    }
    
    
    @Test
    public void testDeserialize_balanceListSize2() {
        System.out.println("testDeserialize_balanceListSize2() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balances.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);

            System.out.println("accountBalance = " + accountBalance);
            
            int expected = 2;
            int balanceSize = accountBalance.getAssetList().size();
            
            System.out.println("balanceSize = " + balanceSize);
            
            assertEquals("The test has failed.", expected, balanceSize);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println("testDeserialize_balanceListSize2() End");
        }
    }

    @Test
    public void testDeserialize_rBalance() {        
        System.out.println("testDeserialize_rBalance() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balances.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);
            System.out.println("accountBalance = " + accountBalance);
            
            Balance rBalance = accountBalance.getBalanceFor("R");
            
            System.out.println("rBalance = " + rBalance);
            
            assertNotNull("The test has failed.", rBalance);
            
            double expected = 12.5;
            
            System.out.println("btcBalance.reserved = " + rBalance.reserved);

            assertEquals("The test has failed", expected, rBalance.reserved);
            
        } catch (Exception e) {
            e.printStackTrace();
            fail("The test has failed. Invalid flow.");
        } finally{ 
            System.out.println("testDeserialize_assetBalance() End");
        }
    }
    
    @Test
    public void testDeserialize_invalidJson() throws Exception {        
        System.out.println("testDeserialize_invalidJson() Start");

        try {
            
            Reader reader = Files.newBufferedReader(JSON_DIR_PATH.resolve("account_balance_invalid.json"));
            JsonElement el = gson.fromJson(reader, JsonElement.class);            
            AccountBalance accountBalance = gson.fromJson(el.getAsJsonObject().get("data"), AccountBalance.class);
            System.out.println("accountBalance = " + accountBalance);
            
            assertNull("The test has failed. accountBalance must be null", accountBalance);
       
        } finally{ 
            System.out.println("testDeserialize_invalidJson() End");
        }
    }
    
}
