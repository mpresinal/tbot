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

package com.presinal.tradingbot.bot.strategy.factory.impl;

import com.presinal.tradingbot.bot.strategy.Strategy;
import com.presinal.tradingbot.bot.strategy.StrategyFactory;
import com.presinal.tradingbot.bot.misc.SpringContextRequired;
import org.springframework.context.ApplicationContext;

/**
 * This class is bridge between based rule strategy and the bot action strategy. 
 * The strategy is built at run time and the bot action strategy does not know which
 * strategy to run, so here is when this class come to play. With a help from the
 * dependency injection system this class get strategy instance and make it available to
 * bot action strategy. 
 * 
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class DIBasedRuleStrategyFactoryImpl implements StrategyFactory, SpringContextRequired {

    private String prototypeBeanName;
    private ApplicationContext springContext;

    public DIBasedRuleStrategyFactoryImpl(ApplicationContext springContext) {
        this.springContext = springContext;
    }    
    
    @Override
    public Strategy newStrategy() {
        return springContext.getBean(prototypeBeanName, Strategy.class);
    }

    public String getPrototypeBeanName() {
        return prototypeBeanName;
    }

    public void setPrototypeBeanName(String prototypeBeanName) {
        this.prototypeBeanName = prototypeBeanName;
    }

    public ApplicationContext getSpringContext() {
        return springContext;
    }

    @Override
    public void setSpringContext(ApplicationContext springContext) {
        this.springContext = springContext;
    }
}
