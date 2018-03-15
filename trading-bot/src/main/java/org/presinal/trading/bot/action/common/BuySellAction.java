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
package org.presinal.trading.bot.action.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.presinal.trading.bot.action.AbstractBotAction;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class BuySellAction extends AbstractBotAction {

    public static final String KEY = BuySellAction.class.getSimpleName();
    private String name = KEY;

    private boolean signalRecieved = false;

    // Action context key of the action that generated buy/sell orders
    private String generatorOrderActionKey;

    public BuySellAction(String generatorOrderActionKey) {
        super();
        this.generatorOrderActionKey=generatorOrderActionKey;
    }
    

    @Override
    public String getContextKey() {
        return name;
    }

    @Override
    public void run() {
        System.out.println(name + " :: performeAction() Enter");

        while (!isActionEnded()) {

            synchronized (this) {
                System.out.println(name + " :: performeAction() Waiting for signal to place an order");

                while (!signalRecieved) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BuySellAction.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                signalRecieved = false;
                Object signalData = getContext().get(generatorOrderActionKey);
                System.out.println(name + " :: performeAction() Executing task");
                System.out.println(name + " :: performeAction() signalData = " + signalData);
                notifyListener();
            }

        }

        System.out.println(name + " :: performeAction() Exit");
    }

    @Override
    public void notifySignal() {
        System.out.println(name + " :: update() Enter");
        synchronized (this) {
            notifyAll();
            signalRecieved = true;
        }
        System.out.println(name + " :: update() Exit");
    }

}
