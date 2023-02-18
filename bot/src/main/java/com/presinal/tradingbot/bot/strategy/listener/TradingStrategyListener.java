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
package com.presinal.tradingbot.bot.strategy.listener;

import com.presinal.tradingbot.bot.strategy.BuySellSignal;
import com.presinal.tradingbot.bot.strategy.Signal;
import com.presinal.tradingbot.bot.strategy.Strategy;
import com.presinal.tradingbot.market.client.types.AssetPair;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public interface TradingStrategyListener extends StrategyListener<BuySellSignal> {

    void onBuySignal(AssetPair asset, double price);

    void onSellSignal(AssetPair asset, double price);

    public default void onSignal(BuySellSignal signal, Strategy source) {
        if (signal.isBuySignal()) {
            onBuySignal(signal.getAsset(), signal.getPrice());
        } else {
            onSellSignal(signal.getAsset(), signal.getPrice());
        }
    }
}
