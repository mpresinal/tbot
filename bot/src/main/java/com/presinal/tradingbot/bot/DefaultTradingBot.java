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

package com.presinal.tradingbot.bot;

import com.presinal.tradingbot.bot.action.ActionChangeReactionConfig;
import com.presinal.tradingbot.bot.action.BotAction;
import com.presinal.tradingbot.bot.action.BotActionContext;
import com.presinal.tradingbot.market.client.MarketClient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DefaultTradingBot implements TradingBot {
    
    private MarketClient marketClient;
    private Set<BotAction> actions;
    private String botName;
    private String version;

    private BotActionContext context;
    
    private Set<ActionChangeReactionConfig> actionReactionConfigs;
    
    public DefaultTradingBot(MarketClient marketClient, String botName, String version) {
        this.marketClient = marketClient;
        this.botName = botName;
        this.version = version;
        
        context = new BotActionContext();
        actions = new HashSet<>();
    }    
        
    @Override
    public void init() {
        
        // Set Action context
        getActions().forEach(action -> {
            action.setContext(getContext());
        });
        
        // configure action change listener
        getActionReactionConfigs().forEach( actRxConfig -> {
            reactOnChangeOf(actRxConfig.getActionToReact(), actRxConfig.getSourceAction());
        });        
    }

    @Override
    public void start() {        
        actions.stream().forEach((BotAction action) ->  action.performeAction() );
    }
    
    @Override
    public void addBotAction(BotAction action) {
        
        if(action != null) {
            action.setContext(context);
            actions.add(action);
        }
    }
    
    @Override
    public void reactOnChangeOf(BotAction actionToReact, BotAction source) {
        
        if(Objects.nonNull(actionToReact) && Objects.nonNull(source)) {
            // This Lambda Expression will generate an implementation of BotActionListener
            source.addListener((BotAction saction, BotActionContext context_) -> {
                actionToReact.setSignalDataProducerKey(saction.getContextKey());
                actionToReact.notifySignal();
            });
        }
        
    }
    
    public MarketClient getMarketClient() {
        return marketClient;
    }

    public String getBotName() {
        return botName;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public BotActionContext getContext() {
        return context;
    }
    
    public Set<BotAction> getActions() {
        
        if(actions == null){
            actions = new HashSet<>();
        }
        
        return actions;
    }

    public void setActions(Set<BotAction> actions) {
        this.actions = actions;
    }

    public Set<ActionChangeReactionConfig> getActionReactionConfigs() {
        if(actionReactionConfigs == null){
            actionReactionConfigs = new HashSet<>();
        }
        
        return actionReactionConfigs;
    }

    public void setActionReactionConfigs(Set<ActionChangeReactionConfig> actionReactionConfigs) {
        this.actionReactionConfigs = actionReactionConfigs;
    }    
}
