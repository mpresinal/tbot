
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.impl.binance.BinanceMarketClient;
import org.presinal.market.client.impl.kucoin.KucoinMarketClient;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.indicator.EMA;
import org.presinal.trading.indicator.RSI;
import org.presinal.trading.indicator.SMA;
import org.presinal.trading.indicator.datareader.PeriodIndicatorDataReader;

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

    private static void computeDataReaderDateRange(PeriodIndicatorDataReader dataReader) {

        long perioTimestamp = dataReader.getTimeFrame().toMilliSecond() * dataReader.getPeriod();

        Calendar cal = Calendar.getInstance();
        Instant endDate = Instant.now();

        // remove second to avoid invalid date range
        cal.setTimeInMillis(endDate.toEpochMilli());
        endDate = Instant.ofEpochMilli(endDate.toEpochMilli() - (cal.get(Calendar.SECOND) * 1000));
        Instant startDate = Instant.ofEpochMilli(endDate.toEpochMilli() - perioTimestamp);
        dataReader.setDateRange(startDate, endDate);
    }
    
    private static List<Candlestick> loadData() throws MarketClientException{
        MarketClient client = new KucoinMarketClient(KucoinMarketClient.API_URL, "test", "test");
        AssetPair asset = new AssetPair("DRGN", "BTC");
        PeriodIndicatorDataReader dataReader = new PeriodIndicatorDataReader(asset, 15, TimeFrame.ONE_HOUR);
        dataReader.setMarketClient(client);
        computeDataReaderDateRange(dataReader);
        
        System.out.printf("Start Date(%s): %s %n", dataReader.getStartDate().getEpochSecond(), dataReader.getStartDate());
        System.out.printf("End Date(%s): %s %n", dataReader.getStartDate().getEpochSecond(), dataReader.getStartDate());
        
        return dataReader.readData();
    }
    
    private static List<Candlestick> loadDataFromBinance() throws MarketClientException{
        MarketClient client = new BinanceMarketClient(BinanceMarketClient.API_URL, "test", "xpo");
        int period = 200;
        TimeFrame timeFrame = TimeFrame.FIFTEEN_MINUTES;
        
        //https://kitchen-3.kucoin.com/v1/open/chart/history?symbol=XRB-BTC&resolution=480&from=1479321029&to=1520793089
        PeriodIndicatorDataReader dataReader = new PeriodIndicatorDataReader(new AssetPair("XVG", "BTC"), period, timeFrame);
        dataReader.setMarketClient(client);
        return dataReader.readData();
    }
    
    public static void main(String[] args) throws MarketClientException {

        List<Candlestick> list = loadDataFromBinance();//loadData();
        
        //list.stream().forEach(c -> System.out.println(c.closePrice));
        
        RSI rsi = new RSI();
        rsi.setPeriod(14);
        rsi.evaluate(list);
        
        SMA ema = new SMA();
        ema.setPeriod(5);
        ema.evaluate(list);
        
        SMA smaFaster = new SMA();
        smaFaster.setPeriod(8);
        smaFaster.evaluate(list);
        
        System.out.println("rsi = " + rsi.getResult()); // expected result = 84.7458
        System.out.println("sma = " + ema.getResult()); // expected result = 5.556428571
        System.out.println("smaFaster = " + smaFaster.getResult()); // expected result = 84.7458
        
        System.out.println("isOverBought = " + rsi.isOverBought()); // expected result = 84.7458
        System.out.println("isOverSold = " + rsi.isOverSold()); // expected result = 84.7458
        System.out.println("isNormal = " + rsi.isNormal()); // expected result = 84.7458

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
