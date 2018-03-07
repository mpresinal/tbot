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
package org.presinal.trading.indicator;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import static java.util.logging.Level.INFO;
import java.util.logging.Logger;
import org.presinal.market.client.types.Candlestick;
import org.presinal.trading.indicator.datareader.PeriodIndicatorDataReader;

/**
 * Simple Moving Average
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class SMA extends AbstractIndicator<Double, PeriodIndicatorDataReader> {

    private Logger logger = Logger.getLogger(SMA.class.getName());
    private static final String NAME = "Simple Moving Average";
    private double mean;

    private boolean started = false;
    private boolean running = false;

    public SMA() {
        super(NAME, ResultType.SINGLE_RESULT);
    }  

    public Double getSingleResult() {
        return mean;
    }

    public Collection<Double> getMultiResult() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        if (running) {
            return;
        }
        running = true;
        Thread thread = new Thread(new MovingAverageCalculator(this));
        thread.start();
    }

    @Override
    public void stop() {
        running = false;
    }

    private static class MovingAverageCalculator implements Runnable {

        private final String CLASS_NAME = MovingAverageCalculator.class.getSimpleName();
        
        private SMA sma;

        MovingAverageCalculator(SMA sma) {
            this.sma = sma;
        }

        @Override
        public void run() {
            final String METOD_NAME = "run";
            long perioTimestamp = sma.timeFrame.toMilliSecond() * sma.period;
            //Date endDate, startDate;
            Instant endDate, startDate;
            List<Candlestick> data;
            Instant openTime;
            long sleepTime=0;
            Instant now;
            long elapseTime;
            
            while (sma.running) {
                System.out.println("-----------------------------------------------\n");
                sleepTime = sma.timeFrame.toMilliSecond();
                
                endDate = Instant.now();//new Date();
                startDate = Instant.ofEpochMilli(endDate.toEpochMilli()-perioTimestamp); //new Date(endDate.getTime() - perioTimestamp);
                sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "startDate = "+startDate);
                sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "endDate = "+endDate);
                
                sma.dataReader.setDateRange(startDate, endDate);
                Instant poinx = Instant.now();
                System.out.println(poinx+" :: Getting data....");
                data = sma.dataReader.readData();
                Instant poinb = Instant.now();
                System.out.println(poinb+" :: Getting data....DONE");
                System.out.println(poinb+" :: Getting data :: elapse time: "+(poinb.toEpochMilli() - poinx.toEpochMilli()));
                
                if (data != null && !data.isEmpty()) {
                    
                    
                    System.out.println(" :: data == "+data);
                    
                    double total = 0.0;
                    int size = data.size();
                    openTime = data.get(size - 1).dateTime;
                    
                    for(int i = 1; i <= sma.period; i++){
                        total = total + data.get(size-i).closePrice;
                    }
                    
                    /*
                    for (Candlestick candlestick : data) {
                        total = total + candlestick.closePrice;
                    }*/

                    sma.mean = total / sma.period;
                    sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "average = "+sma.mean);
                    
                    sma.notifyListeners();
                    
                    // computing sleep time
                    now = Instant.now();
                    elapseTime = now.toEpochMilli() - openTime.toEpochMilli();
                    sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "elapseTime = "+elapseTime);
                    sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "timeFrame in milli = "+sma.timeFrame.toMilliSecond());
                    
                    if(elapseTime < sma.timeFrame.toMilliSecond()) {
                        sleepTime = sma.timeFrame.toMilliSecond() - elapseTime;
                    }                    
                }
                
                System.out.println("-----------------------------------------------\n");
                
                try {
                    sma.logger.logp(INFO,CLASS_NAME, METOD_NAME, "sleepTime = "+sleepTime);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    sma.logger.logp(Level.SEVERE, CLASS_NAME, METOD_NAME, "InterruptedException"+ex.getMessage());                    
                }

            }
        }
    }
}
