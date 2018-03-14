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
import org.presinal.market.client.types.AssetPair;
import org.presinal.trading.bot.action.AbstractBotAction;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetSelectionAction extends AbstractBotAction {

    public static final String KEY = AssetSelectionAction.class.getSimpleName();
    private String name = KEY;

    public AssetSelectionAction() {
        super(1);
    }

    @Override
    public String getContextKey() {
        return name;
    }

    @Override
    public void run() {
        System.out.println(name + " :: performeAction() Enter");
        System.out.println(name + " :: performeAction() Executing task");

        getContext().put(KEY, new AssetPair("PRL", "BTC"));

        try {
            Thread.sleep(10 * 1000L);
        } catch (InterruptedException ex) {
            Logger.getLogger(AssetSelectionAction.class.getName()).log(Level.SEVERE, null, ex);
        }

        notifyListener();
    }

    @Override
    public void notifySignal() {
        System.out.println(name + " :: update() Enter");
    }

}
