/*
 * The MIT License
 *
 * Copyright 2023 Miguel Presinal <presinal378@gmail.com>.
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
package com.presinal.tradingbot.bot.strategy;

import com.presinal.tradingbot.bot.action.BotActionContext;
import com.presinal.tradingbot.bot.strategy.listener.StrategyListener;
import com.presinal.tradingbot.market.client.MarketClient;
import com.presinal.tradingbot.market.client.types.AssetPair;

/**
 *
 * @author Miguel Presinal <presinal378@gmail.com>
 */
public abstract class AbstractStrategy implements Strategy {
    
    // Properties
    private StrategyListener listener;
    private MarketClient client;
    private AssetPair asset;    
    private BotActionContext context;

    public StrategyListener getListener() {
        return listener;
    }

    @Override
    public void setListener(StrategyListener listener) {
        this.listener = listener;
    }

    public MarketClient getClient() {
        return client;
    }

    @Override
    public void setClient(MarketClient client) {
        this.client = client;
    }

    public AssetPair getAsset() {
        return asset;
    }

    @Override
    public void setAsset(AssetPair asset) {
        this.asset = asset;
    }

    public BotActionContext getContext() {
        return context;
    }

    @Override
    public void setContext(BotActionContext context) {
        this.context = context;
    }    
    
    
}
