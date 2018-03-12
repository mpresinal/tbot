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

package org.presinal.trading.bot.action;

import org.presinal.trading.bot.strategy.Signal;
import org.presinal.trading.bot.strategy.Strategy;
import org.presinal.trading.bot.strategy.listener.StrategyListener;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class BotActionStrategy extends AbstractBotAction implements StrategyListener{

    public static final String CONTEXT_KEY = BotActionStrategy.class.getSimpleName();
    
    private Strategy strategy;
    private boolean running = false;
    
    public BotActionStrategy(int executionOrder, Strategy strategy) {
        super(executionOrder);
        this.strategy=strategy;
        strategy.setListener(this);
    }


    public String getContextKey() {
        return CONTEXT_KEY;
    }
    
    @Override
    public void run() {        
        
        if(!running) {
            new Thread(strategy).start();            
        }
        
    }

    @Override
    public void notifySignal() {
       
    }

    @Override
    public void onSignal(Signal signal, Strategy source) {
        getContext().put(CONTEXT_KEY, signal);
        notifyListener();
    }

}
