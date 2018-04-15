
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClientException;
import org.presinal.trading.bot.BotLauncher;
import org.presinal.trading.bot.DefaultTradingBot;
import org.presinal.trading.bot.strategy.StrategyFactory;
import org.presinal.trading.bot.strategy.factory.impl.DIBasedRuleStrategyFactoryImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class Main {

    public static void main(String[] args) throws MarketClientException {
        
        try {
            LogManager.getLogManager().readConfiguration(Files.newInputStream(Paths.get("logging.properties"), StandardOpenOption.READ));
        } catch (IOException ex) {
            Logger.getLogger(DefaultTradingBotMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Loading Spring Application Context...");
        ApplicationContext appContext = new ClassPathXmlApplicationContext("application-context.xml");
        System.out.println("Loading Spring Application Context...OK");
        
        System.out.println("Getting BotLauncher bean instance from Spring Application Context...");
        BotLauncher launcher = appContext.getBean("botLauncher", BotLauncher.class);
        System.out.println("Getting BotLauncher bean instance from Spring Application Context...OK");
        
        System.out.println("Setting Spring Context to StrategyFactory...");
        StrategyFactory strategyFactory = appContext.getBean("strategyFactory", StrategyFactory.class);        
        System.out.println("strategyFactory = "+strategyFactory);        
        if(strategyFactory instanceof DIBasedRuleStrategyFactoryImpl){
            ((DIBasedRuleStrategyFactoryImpl)strategyFactory).setSpringContext(appContext);
        }
        System.out.println("Setting Spring Context to StrategyFactory...OK");
        
        System.out.println("Launching bots...");
        launcher.launchBots();
        System.out.println("Launching bots...OK");        
    }
}
