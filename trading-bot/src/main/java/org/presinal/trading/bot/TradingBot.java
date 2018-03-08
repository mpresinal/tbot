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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.presinal.market.client.MarketClient;
import org.presinal.trading.bot.action.BotAction;
import org.presinal.trading.bot.action.BotActionContext;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public abstract class TradingBot {
    private MarketClient marketClient;
    private Set<BotAction> actions;
    private String botName;
    private String version;

    private BotActionContext context;
    
    public TradingBot(MarketClient marketClient, String botName, String version) {
        this.marketClient = marketClient;
        this.botName = botName;
        this.version = version;
        
        context = new BotActionContext();
        actions = new HashSet<>();
    }    
    
    
    public abstract void init();
    

    public void start() {        
        actions.stream().forEach((BotAction action) ->  action.performeAction() );
    }
    
    public void addBotAction(BotAction action) {
        
        if(action != null) {
            action.setContext(context);
            actions.add(action);
        }
    }
    
    public void reactOnChangeOf(BotAction actionToReact, BotAction source) {
        
        if(Objects.nonNull(actionToReact) && Objects.nonNull(source)) {
            // This Lambda Expression will generate an implementation of BotActionListener
            source.addListener((BotAction saction, BotActionContext context_) -> actionToReact.notifySignal());            
        }
        
    }
    
    public MarketClient getMarketClient() {
        return marketClient;
    }

    public String getBotName() {
        return botName;
    }

    public String getVersion() {
        return version;
    }

}
