
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.presinal.trading.bot.TradingBot;
import org.presinal.trading.bot.action.AbstractBotAction;
import org.presinal.trading.bot.action.BotAction;
import org.presinal.trading.bot.action.BotActionContext;
import org.presinal.trading.bot.action.BotActionListener;

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
/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class TestBot {

    public static void main(String[] args) {
        System.out.println("Creating bot...");
        TradingBot bot = new TradingBot(null, "ScalpingBot", "v1.0") {
            @Override
            public void init() {
                System.out.println(getBotName() + ":: Init");
            }
        };
        System.out.println("Creating bot...OK");
        
        System.out.println("Adding action to bot...");
        AssetSelectionAction selection = new AssetSelectionAction();
        BuyAction buyAction = new BuyAction();
        ScalpingAction scalpingAction = new ScalpingAction();

        bot.addBotAction(selection);
        bot.addBotAction(buyAction);
        bot.addBotAction(scalpingAction);

        bot.reactOnChangeOf(scalpingAction, selection);
        bot.reactOnChangeOf(buyAction, scalpingAction);
        
        System.out.println("Adding action to bot...OK");
        
        System.out.println("Initializing bot...");
        bot.init();
        System.out.println("Initializing bot...OK");
        
        System.out.println("Starting bot...");
        bot.start();
        System.out.println("Starting bot...OK");
    }

    private static class AssetSelectionAction extends AbstractBotAction {

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
            
            try {
                Thread.sleep(30*1000L);
            } catch (InterruptedException ex) {
                Logger.getLogger(TestBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            notifyListener();
        }

        @Override
        public void update() {
            System.out.println(name + " :: update() Enter");
        }
    }

    private static class BuyAction extends AbstractBotAction {

        public static final String KEY = BuyAction.class.getSimpleName();        
        private String name = KEY;
        
        private boolean signalRecieved = false;

        public BuyAction() {
            super(2);
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
                            Logger.getLogger(TestBot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    signalRecieved = false;
                    Object signalData = getContext().get(ScalpingAction.KEY);
                    System.out.println(name + " :: performeAction() Executing task");
                    System.out.println(name + " :: performeAction() signalData = "+signalData);
                    notifyListener();
                }

            }

            System.out.println(name + " :: performeAction() Exit");
        }

        @Override
        public void update() {
            System.out.println(name + " :: update() Enter");
            synchronized (this) {
                notifyAll();
                signalRecieved = true;
            }
            System.out.println(name + " :: update() Exit");
        }
    }

    private static class ScalpingAction extends AbstractBotAction {
        
        public static final String KEY = ScalpingAction.class.getSimpleName();        
        private String name = KEY;
        
        private boolean signalRecieved = false;
        
        public ScalpingAction() {
            super(3);
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
                            Logger.getLogger(TestBot.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    signalRecieved = false;
                    Object signalData = getContext().get(AssetSelectionAction.KEY);
                    System.out.println(name + " :: performeAction() Executing task");
                    System.out.println(name + " :: performeAction() signalData = "+signalData);
                    notifyListener();
                }

            }

            System.out.println(name + " :: performeAction() Exit");
        }

        @Override
        public void update() {
            System.out.println(name + " :: update() Enter");
            synchronized (this) {
                notifyAll();
                signalRecieved = true;
            }
            System.out.println(name + " :: update() Exit");
        }
    }
}
