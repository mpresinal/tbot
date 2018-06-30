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

package org.presinal.trading.bot.strategy.factory.impl;

import org.presinal.trading.bot.strategy.Strategy;
import org.presinal.trading.bot.strategy.StrategyFactory;
import org.presinal.trading.tbot.annotation.SpringContextRequire;
import org.presinal.trading.tbot.misc.SpringContextRequired;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
@SpringContextRequire(field = "springContext")
public class DIBasedRuleStrategyFactoryImpl implements StrategyFactory, SpringContextRequired {

    private String prototypeBeanName;
    private ApplicationContext springContext;
    
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
