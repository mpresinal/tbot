
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.impl.kucoin.KucoinMarketClient;
import org.presinal.market.client.types.AssetPair;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.indicator.EMA;
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
 * @author Miguel Presinal<mpresinal@gmail.com>
 * @since 1.0
 */
public class MovingAVGTest {

    public static void main(String[] args) throws MarketClientException {
        MarketClient client = new KucoinMarketClient(KucoinMarketClient.API_URL, "test", "xpo");
        int period = 200;
        TimeFrame timeFrame = TimeFrame.EIGHT_HOURS;
        
        //https://kitchen-3.kucoin.com/v1/open/chart/history?symbol=XRB-BTC&resolution=480&from=1479321029&to=1520793089
        PeriodIndicatorDataReader dataReader = new PeriodIndicatorDataReader(new AssetPair("BTC", "USDT"), period, timeFrame);
        dataReader.setMarketClient(client);
        /*
        dataReader.setDateRange(Instant.ofEpochSecond(1479321029), Instant.ofEpochSecond(1520793089));
        long startMill = System.currentTimeMillis();
        System.out.println("Get data Started at: "+startMill);
        List<Candlestick> data = dataReader.readData();
        long end = System.currentTimeMillis();
        System.out.println("Get data End at: "+startMill);
        System.out.println("get Data elapse time: "+ (end - startMill));
        
        
        startMill = System.currentTimeMillis();
        System.out.println("computing ema Started at: "+startMill);
        EMA ema = new EMA(13, timeFrame);
        ema.evaluate(data);
        end = System.currentTimeMillis();
        System.out.println("ema(13): "+ema.getSingleResult());
        System.out.println("computing ema End at: "+startMill);
        System.out.println("computing ema elapse time: "+ (end - startMill));
        
        startMill = System.currentTimeMillis();
        System.out.println("computing ema Started at: "+startMill);
        EMA ema2 = new EMA(34, timeFrame);
        ema2.evaluate(data);
        end = System.currentTimeMillis();
        System.out.println("ema(34): "+ema2.getSingleResult());
        System.out.println("computing ema End at: "+startMill);
        System.out.println("computing ema elapse time: "+ (end - startMill));
        
        
        startMill = System.currentTimeMillis();
        System.out.println("computing sma Started at: "+startMill);
        SMA sma = new SMA();
        sma.setPeriod(13);
        sma.setTimeFrame(timeFrame);
        sma.evaluate(data);
        end = System.currentTimeMillis();
        System.out.println("sma: "+sma.getSingleResult());
        System.out.println("computing sma End at: "+startMill);
        System.out.println("computing sma elapse time: "+ (end - startMill));
        */
        
        MovingAverageCalculator calculator = new MovingAverageCalculator(dataReader);
        calculator.period = period;
        calculator.timeFrame = timeFrame;
        new Thread(calculator).start();
        
    }
    
    
    public static class MovingAverageCalculator implements Runnable {

        private Logger logger = Logger.getLogger(SMA.class.getName());
        
        private final String CLASS_NAME = MovingAverageCalculator.class.getSimpleName();
        
        
        private PeriodIndicatorDataReader dataReader;
        
        public int period;
        public TimeFrame timeFrame;
        
        MovingAverageCalculator(PeriodIndicatorDataReader dataReader) {
            
            this.dataReader=dataReader;
        }

        @Override
        public void run() {
            final String METOD_NAME = "run";
            long perioTimestamp = timeFrame.toMilliSecond() * period;
            //Date endDate, startDate;
            Instant endDate, startDate;
            List<Candlestick> data;
            Instant openTime;
            long sleepTime=0;
            Instant now;
            long elapseTime;
            double mean;
            
            Calendar cal = Calendar.getInstance();
            
            cal.setTimeInMillis(Instant.now().toEpochMilli());
            System.out.println("hr = "+cal.get(Calendar.HOUR_OF_DAY));
            System.out.println("minut = "+cal.get(Calendar.MINUTE));
            System.out.println("second = "+cal.get(Calendar.SECOND));
            System.out.println("timeZone = "+cal.getTimeZone());
            
            
            while (true) {
                System.out.println("-----------------------------------------------\n");
                sleepTime = timeFrame.toMilliSecond();
                
                endDate = Instant.now();//new Date();
                System.out.println("****** endDate = "+endDate);
                // remove second to avoid invalid date range
                cal.setTimeInMillis(endDate.toEpochMilli());
                endDate = Instant.ofEpochMilli(endDate.toEpochMilli() - (cal.get(Calendar.SECOND) * 1000));
                
                //System.out.println("endDate.get(ChronoField.SECOND_OF_MINUTE) = "+endDate.getLong(ChronoField.SECOND_OF_MINUTE));
                startDate = Instant.ofEpochMilli(endDate.toEpochMilli()-perioTimestamp); //new Date(endDate.getTime() - perioTimestamp);
                logger.logp(INFO,CLASS_NAME, METOD_NAME, "startDate = "+startDate);
                logger.logp(INFO,CLASS_NAME, METOD_NAME, "endDate = "+endDate);
                
                dataReader.setDateRange(startDate, endDate);
                Instant poinx = Instant.now();
                System.out.println(poinx+" :: Getting data....");
                data = dataReader.readData();
                Instant poinb = Instant.now();
                System.out.println(poinb+" :: Getting data....DONE");
                System.out.println(poinb+" :: Getting data :: elapse time: "+(poinb.toEpochMilli() - poinx.toEpochMilli()));
                
                if (data != null && !data.isEmpty()) {
                    
                    
                    System.out.println(" :: data == "+data);
                    
                    double total = 0.0;
                    int size = data.size();
                    openTime = data.get(size - 1).dateTime;
                    
                    for(int i = 1; i <= period; i++){
                        total = total + data.get(size-i).closePrice;
                    }
                    
                    /*
                    for (Candlestick candlestick : data) {
                        total = total + candlestick.closePrice;
                    }*/

                    mean = total / period;
                    logger.logp(INFO,CLASS_NAME, METOD_NAME, "average = "+mean);
                    
                    
                    
                    // computing sleep time
                    now = Instant.now();
                    elapseTime = now.toEpochMilli() - openTime.toEpochMilli();
                    logger.logp(INFO,CLASS_NAME, METOD_NAME, "elapseTime = "+elapseTime);
                    logger.logp(INFO,CLASS_NAME, METOD_NAME, "timeFrame in milli = "+timeFrame.toMilliSecond());
                    
                    if(elapseTime < timeFrame.toMilliSecond()) {
                        sleepTime = timeFrame.toMilliSecond() - elapseTime;
                    }                    
                }
                
                System.out.println("-----------------------------------------------\n");
                
                try {
                    logger.logp(INFO,CLASS_NAME, METOD_NAME, "sleepTime = "+sleepTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    logger.logp(Level.SEVERE, CLASS_NAME, METOD_NAME, "InterruptedException"+ex.getMessage());                    
                }

            }
        }
    }
}
