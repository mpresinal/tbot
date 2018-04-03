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

package org.presinal.trading.bot;

import org.presinal.market.client.MarketClient;
import org.presinal.trading.bot.action.common.AssetSelectionAction;
import org.presinal.trading.bot.action.common.BuySellAction;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class DefaultTradingBot extends TradingBot {
    
    public static final String NAME = "Mat";
    public static final String VERSION = "v1.0";
    
    public DefaultTradingBot(MarketClient marketClient) {
        super(marketClient, NAME, VERSION);
    }

    @Override
    public void init() {
        AssetSelectionAction assetSelection = new AssetSelectionAction(getMarketClient());        
        DefaultTradingAction tradingAction = new DefaultTradingAction(getMarketClient());        
        BuySellAction buyAction = new BuySellAction(getMarketClient(), tradingAction.getContextKey());
        
        assetSelection.setQuoteAsset(AssetSelectionAction.DEFAULT_QUOTEASSET);
        
        assetSelection.excludeAsset("ETH");
        
        tradingAction.setStopLostAtPercentage(2);
        tradingAction.setTakeProfitAtPercentage(3);
        
        addBotAction(assetSelection);
        addBotAction(buyAction);
        addBotAction(tradingAction);

        reactOnChangeOf(tradingAction, assetSelection);
        reactOnChangeOf(buyAction, tradingAction);
    }

}
