
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.indicator.RSI;
import org.presinal.trading.indicator.SMA;

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
public class TestRSIFor {

    public static void main(String[] args) {

        List<Candlestick> list = new ArrayList<>();
        list.add(new Candlestick(0, 5.24, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.44, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.42, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.44, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.43, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.45, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.5, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.57, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.66, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.69, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.63, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.63, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.64, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.64, 0, 0, 0, Instant.EPOCH));
        list.add(new Candlestick(0, 5.65, 0, 0, 0, Instant.EPOCH));
        
        RSI rsi = new RSI();
        rsi.setPeriod(14);
        rsi.evaluate(list);
        
        SMA sma = new SMA();
        sma.setPeriod(14);
        sma.evaluate(list);
        
        SMA smaFaster = new SMA();
        smaFaster.setPeriod(7);
        smaFaster.evaluate(list);
        
        System.out.println("rsi = " + rsi.getSingleResult()); // expected result = 84.7458
        System.out.println("sma = " + sma.getSingleResult()); // expected result = 5.556428571
        System.out.println("smaFaster = " + smaFaster.getSingleResult()); // expected result = 84.7458

        /*
        double[] data = {5.24, 5.44, 5.42, 5.44, 5.43, 4.98};
        int length = data.length;
        
        int period = 4;
        int start = length;
        int to = (length > period) ? period : length - 1;
        System.out.println("to = " + to);
        
        double prev = data[0];
        double current, delta;
        double upward = 0.0;
        double downward = 0.0;
        double rs;
        
        for (int i = start; i >=1 ; i++) {
            System.out.println("i = " + i);
            current = data[i];
            delta = Math.abs(current - prev);
            if (current > prev) {
                upward += delta;
            } else if (current < prev) {
                downward += delta;
            }

            prev = current;
        }
        
        rs = (upward/period) / (downward/period);
        
        double rsi = Math.round(100 - (100/(1+rs)));
        System.out.println("rsi = " + rsi);
         */
    }
}
