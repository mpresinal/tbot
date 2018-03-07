
import java.util.Calendar;
import java.util.Date;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.TimeZone;
import org.presinal.market.client.MarketClient;
import org.presinal.market.client.MarketClientException;
import org.presinal.market.client.enums.TimeFrame;
import org.presinal.market.client.impl.kucoin.KucoinMarketClient;
import org.presinal.market.client.types.AssetPair;
import org.presinal.trading.indicator.Indicator;
import org.presinal.trading.indicator.IndicatorListener;
import org.presinal.trading.indicator.ResultType;
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
public class MainTest implements IndicatorListener {

    @Override
    public void onUpdate(Indicator indicator) {
        System.out.println("MainTest.onUpdate() Enter");
        System.out.println("MainTest.onUpdate() indicator = "+indicator);
        if(indicator.getResultType() == ResultType.SINGLE_RESULT) {
            System.out.println("MainTest.onUpdate() result = "+indicator.getSingleResult());
        } else {
            System.out.println("MainTest.onUpdate() result = "+indicator.getMultiResult());
        }
        
        System.out.println("MainTest.onUpdate() Exit");
    }
    
    public static void main(String[] args) throws InterruptedException, MarketClientException {
        MarketClient client = new KucoinMarketClient(KucoinMarketClient.API_URL, "test", "xpo");
        int period = 3;
        TimeFrame timeFrame = TimeFrame.FIVE_MINUTES;
        PeriodIndicatorDataReader dataReader = new PeriodIndicatorDataReader(new AssetPair("IHT", "BTC"), period, timeFrame);
        dataReader.setMarketClient(client);
        
        SMA sma = new SMA();
        sma.setDataReader(dataReader);
        sma.setPeriod(period);
        sma.setTimeFrame(timeFrame);
        sma.addListener(new MainTest());
        sma.run();        
    }
    
    public static void mainx(String[] args) throws InterruptedException {
//,,
        System.out.println("Time 1 = "+new Date(1520296500*1000));
        System.out.println("Time 2 = "+new Date(1520296800*1000));
        System.out.println("Time 3 = "+new Date(1520297100));
        
        System.out.println("TimeZone.getDefault() = "+TimeZone.getDefault());
        
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        //1520212800,//
          //1506050394000
        
        //1520296500,1520296800,1520297100
        
        System.out.println("Instant.ofEpochMilli = "+Instant.ofEpochMilli(1520213100*1000));
        System.out.println("Instant.ofEpochSecond = "+Instant.ofEpochSecond(1520296500));
        System.out.println("Instant.ofEpochSecond = "+Instant.ofEpochSecond(1520296800));
        System.out.println("Instant.ofEpochSecond = "+Instant.ofEpochSecond(1520297100));
        System.out.println("Instant.ofEpochSecond = "+new Date(Instant.ofEpochSecond(1520297100).toEpochMilli()));
        Instant.ofEpochMilli(new Date().getTime());
        Instant inta = Instant.now();
        System.out.println("inta = "+inta);
        
        Instant t = Instant.ofEpochSecond(inta.getEpochSecond() - 1520297100);
        System.out.println("Instant.ofEpochSecond = "+t);
        System.out.println("Instant.ofEpochSecond = "+t.toEpochMilli());
        System.out.println("Instant.ofEpochSecond = "+Instant.ofEpochMilli(t.toEpochMilli()));
        
        Instant it1 = Instant.ofEpochSecond(1520296800);
        Instant it2 = Instant.ofEpochSecond(1520297100);
        
        System.out.println("it1 = "+it1);
        System.out.println("it2 = "+it2);
        
        System.out.println("delta = "+(Instant.ofEpochMilli(it2.toEpochMilli() - it1.toEpochMilli())));
        
        printNextDateRange(3, TimeFrame.FIVE_MINUTES);        
        Thread.sleep(TimeFrame.FIVE_MINUTES.toMilliSecond());
        
        printNextDateRange(3, TimeFrame.FIVE_MINUTES);        
        Thread.sleep(TimeFrame.FIVE_MINUTES.toMilliSecond());
        
        printNextDateRange(3, TimeFrame.FIVE_MINUTES);        
        Thread.sleep(TimeFrame.FIVE_MINUTES.toMilliSecond());
        
        /*int period = 3;
        int time_frame = 60;
        
        long minute_in_miliseconds = 60*1000;
        
        long perioTimestamp = time_frame*minute_in_miliseconds*period;
        
        long perioTimestamp2 = TimeFrame.ONE_HOUR.toMilliSecond() * period;
        
        System.out.println("perioTimestamp = "+perioTimestamp);
        System.out.println("perioTimestamp2 = "+perioTimestamp2);
        
        System.out.println("perioTimestamp2 ==perioTimestamp?  "+(perioTimestamp2 == perioTimestamp2));
        
        Calendar cal = Calendar.getInstance();
        
        Date now = new Date();
        Date startDate = new Date(now.getTime() - perioTimestamp);
        
        System.out.println("from: "+startDate+"("+(startDate.getTime()/1000)+")");
        System.out.println("to: "+now+"("+(now.getTime()/1000)+")");
        
        System.out.println("tx: "+ new Date(1507479171));
        
        Instant inow = Instant.now();
        System.out.println("inow: "+ inow);
        System.out.println("toEpochMilli: "+ inow.toEpochMilli()); */
    }
    
    private static void printNextDateRange(int period, TimeFrame timeFrame) {
        System.out.println("printNextDateRange() Enter");
        System.out.println("printNextDateRange() Generating date range....");
        
        long perioTimestamp = timeFrame.toMilliSecond() * period;
        
        Instant now = Instant.now();
        Instant start = Instant.ofEpochMilli(now.toEpochMilli() - perioTimestamp); 
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime() - perioTimestamp);
        
        System.out.println("startDate: "+startDate+"("+(startDate.getTime()/1000)+")");
        System.out.println("endDate: "+endDate+"("+(endDate.getTime()/1000)+")");
        
        System.out.println("start: "+start+"("+start.getEpochSecond()+")");
        System.out.println("end: "+now+"("+now.getEpochSecond()+")");
        
        System.out.println(String.format("https://api.kucoin.com/v1/open/chart/history?symbol=R-BTC&resolution=%s&from=%s&to=%s",
                timeFrame.getNumber(), (startDate.getTime()/1000), (endDate.getTime()/1000)  ));
        
        System.out.println("printNextDateRange() Generating date range....OK\n");
        System.out.println("printNextDateRange() Exit\n");
        
        
    }

    
}
