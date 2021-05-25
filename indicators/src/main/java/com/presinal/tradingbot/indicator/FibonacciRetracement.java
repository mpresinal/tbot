/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.presinal.tradingbot.indicator;

import com.presinal.tradingbot.indicator.result.FibonacciRetracementResult;
import com.presinal.tradingbot.market.client.types.Candlestick;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class FibonacciRetracement extends AbstractIndicator<FibonacciRetracementResult> {

    public FibonacciRetracement() {
        super("Fibonacci Retracement");
    }
    
    /**
     * Formula:
     * In the uptrend
     * <pre>
     * price_rise = higher_price - lower_price
     * fibo_level = higher_price - (price_rise * fibo_percent)
     * </pre>
     * 
     * In the downtrend
     * <pre>
     * price_rise = higher_price - lower_price
     * fibo_level = lower_price + (price_rise * fibo_percent)
     * </pre>
     * 
     * Example:
     * <pre>
     * lower_price = 10
     * higher_price = 15
     * price_rise = 15 - 10 = 5
     * fibo_level(0.236) = 15 - (5 * 0.236) = 13.82
     * </pre>
     * 
     * The commons fibonacci levels are: 23.6%, 38.2%, 50%, 61.8%, and 78.6%.
     * 
     * Read more on: 
     * https://www.investopedia.com/terms/f/fibonacciretracement.asp
     * https://www.forexfibonacci.com/calculate_fibonacci_levels/04/
     * 
     * @param data a list with two Candlestick. If the first candlestick's high price is lower than 
     * the second candlestick's high price, the fibonacci levels will be calculated using the formula for uptrend.
     * otherwise the formula for downtrend will be used.
     */
    @Override
    public void evaluate(List<Candlestick> data) {
        
        if (data != null && data.size() >= 2) {
            Candlestick price1 = data.get(0);
            Candlestick price2 = data.get(1);
            
            BigDecimal priceIncrease;
            BigDecimal basePrice;
            
            boolean uptrend = price1.highPrice < price2.highPrice;
            if (uptrend) {                                
                basePrice = BigDecimal.valueOf(price2.highPrice);                
                priceIncrease = basePrice.subtract(BigDecimal.valueOf(price1.lowPrice));
                
            } else {
                basePrice = BigDecimal.valueOf(price2.lowPrice);                
                priceIncrease = BigDecimal.valueOf(price1.highPrice).subtract(basePrice) ;
                
            }
            
            setResult(
                    new FibonacciRetracementResult (
                            calLevel(0.236, priceIncrease, basePrice, uptrend),
                            calLevel(0.382, priceIncrease, basePrice, uptrend),
                            calLevel(0.5, priceIncrease, basePrice, uptrend),
                            calLevel(0.618, priceIncrease, basePrice, uptrend),
                            calLevel(0.786, priceIncrease, basePrice, uptrend),
                            uptrend
                    )
            );
            
        }
    }
    
    private BigDecimal calLevel(double level, BigDecimal priceIncrease, BigDecimal basePrice, boolean uptrend) {
        
        BigDecimal r = BigDecimal.valueOf(level).multiply(priceIncrease);        

        if (uptrend) {
            return basePrice.subtract(r);
            
        } else {
            return basePrice.add(r);
            
        }
    } 
}
