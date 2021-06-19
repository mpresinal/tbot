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

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public interface BotAction extends Serializable {

    String getContextKey();
    
    /**
     * Return a list of actions that must be executed ahead of this action
     * 
     * @return A list of the dependency action.
     */
    Collection<BotAction> getDependencyActions();
    
    void setContext(BotActionContext context);
    
    void performeAction();
    
    void notifySignal();
    
    void setSignalDataProducerKey(String signalDataContextKey);
    
    String getSignalDataProducerKey();
    
    void addListener(BotActionListener listener);
    
    void endAction();
    
    boolean isActionEnded();
}
