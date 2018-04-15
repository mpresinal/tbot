
import org.presinal.trading.bot.strategy.BasedRuleStrategy;
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
public class TestSpringDI {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        
        //DIBasedRuleStrategyFactoryImpl
        
        StrategyFactory strategyFactory = context.getBean("strategyFactory", StrategyFactory.class);
        
        System.out.println("strategyFactory = "+strategyFactory);
        
        if(strategyFactory instanceof DIBasedRuleStrategyFactoryImpl){
            ((DIBasedRuleStrategyFactoryImpl)strategyFactory).setSpringContext(context);
        }
        print(strategyFactory);
        print(strategyFactory);
        print(strategyFactory);
        print(strategyFactory);        
        
        /*
        MarketClient client = context.getBean("binanceMarket", MarketClient.class);
        System.out.println("client = "+client);
        
        AssetSelectionAction action = context.getBean("binanceAssetSelecctionAction", AssetSelectionAction.class);
        
        System.out.println("action = "+action);
        System.out.println("action.client = "+action.getClient());
        System.out.println("actionContextKey = "+action.getContextKey());
        System.out.println("quoteAsset = "+ action.getQuoteAsset() );
        System.out.println("excludedAssets = "+ action.getExcludedAssets() );
        System.out.println("maxAssetsToSelect = "+ action.getMaxAssetsToSelect() );
        System.out.println("minAssetVolume = "+ action.getMinAssetVolume() 
        */
    }
    
    private static void print(StrategyFactory strategyFactory){
        BasedRuleStrategy strategy = (BasedRuleStrategy) strategyFactory.newStrategy();
        System.out.println("---------------------------------------------");
        System.out.println("strategy.client = "+strategy.getClient());
        System.out.println("strategy.indicators = "+strategy.getIndicators());
        System.out.println("strategy.buyRule = "+strategy.getBuyRule());
        System.out.println("strategy.sellRule = "+strategy.getSellRule());        
        System.out.println("---------------------------------------------");
        
    }
}

