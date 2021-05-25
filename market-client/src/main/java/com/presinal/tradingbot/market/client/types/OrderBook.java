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
package com.presinal.tradingbot.market.client.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class OrderBook implements Serializable {

    private AssetPair assetPair;
    private List<OrderBookEntry> bids;
    private List<OrderBookEntry> asks;

    public OrderBook() {
    }

    public OrderBook(AssetPair assetPair) {
        this.assetPair = assetPair;
        bids = new ArrayList<>();
        asks = new ArrayList<>();
    }

    public void addBidOrder(double price, double quantity) {
        bids.add(new OrderBookEntry(price, quantity));
    }

    public void addAskOrder(double price, double quantity) {
        asks.add(new OrderBookEntry(price, quantity));
    }

    public AssetPair getAssetPair() {
        return assetPair;
    }

    public void setAssetPair(AssetPair assetPair) {
        this.assetPair = assetPair;
    }

    public List<OrderBookEntry> getBids() {
        return bids;
    }

    public List<OrderBookEntry> getAsks() {
        return asks;
    }

    public static class OrderBookEntry {

        public final double price;
        public final double quantity;

        public OrderBookEntry(double price, double quantity) {
            this.price = price;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "OrderBookEntry{" + "price=" + price + ", quantity=" + quantity + '}';
        }       
    }

    @Override
    public String toString() {
        return "OrderBook{" + "assetPair=" + assetPair + ", bids=" + bids + ", asks=" + asks + '}';
    }

}
