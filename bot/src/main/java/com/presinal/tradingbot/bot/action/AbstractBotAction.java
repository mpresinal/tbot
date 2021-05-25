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

package com.presinal.tradingbot.bot.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public abstract class AbstractBotAction extends Thread implements BotAction {

    private Set<BotAction> dependencyActions;
    private Set<BotActionListener> listeners;
    private BotActionContext context;
    
    private boolean started = false;
    
    private boolean actionEnded = false;
    
    private String signalDataContextKey;
    
    public AbstractBotAction() {        
        dependencyActions = new HashSet<>();
        this.listeners = new HashSet<>();
    }

    @Override
    public Collection<BotAction> getDependencyActions() {
        return dependencyActions;
    }

    @Override
    public void addListener(BotActionListener listener) {
        if(listener != null){
            listeners.add(listener);
        }
    }
    
    @Override
    public void endAction(){
        actionEnded = true;
    }
    
    @Override
    public boolean isActionEnded(){
        return actionEnded;
    }
        

    @Override
    public void setContext(BotActionContext context) {
        this.context = context;
    }

    @Override
    public final void performeAction() {
        
        if(!started) {
            this.start();
        }
    }
    
    protected void notifyListener(){
        for(BotActionListener listener : listeners) {
            try {
                listener.onPerformedAction(this, getContext());
            }catch(Exception e){ 
                // do nothing                
            }
        }
    }

    protected BotActionContext getContext() {
        return context;
    }

    protected Set<BotActionListener> getListeners() {
        return listeners;
    }

    @Override
    public void setSignalDataProducerKey(String signalDataContextKey) {
        this.signalDataContextKey=signalDataContextKey;
    }

    @Override
    public String getSignalDataProducerKey() {
        return signalDataContextKey;
    }
}
