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
package com.presinal.tradingbot.market.client.enums;

/**
 * Time frame constants for candlestick
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public enum TimeFrame {

    ONE_MINUTE(1, "1m", TimeFrame.TIME_UNIT_MINUTE, TimeFrame.TIME_UNIT_MINUTE_SECOND, TimeFrame.TIME_UNIT_MINUTE_MILLISECONDS),
    THREE_MINUTES(3, "3m", TimeFrame.TIME_UNIT_MINUTE, TimeFrame.TIME_UNIT_MINUTE_SECOND, TimeFrame.TIME_UNIT_MINUTE_MILLISECONDS),
    FIVE_MINUTES(5, "5m", TimeFrame.TIME_UNIT_MINUTE, TimeFrame.TIME_UNIT_MINUTE_SECOND, TimeFrame.TIME_UNIT_MINUTE_MILLISECONDS),
    FIFTEEN_MINUTES(15, "15m", TimeFrame.TIME_UNIT_MINUTE, TimeFrame.TIME_UNIT_MINUTE_SECOND, TimeFrame.TIME_UNIT_MINUTE_MILLISECONDS),
    THIRTY_MINUTES(30, "30m", TimeFrame.TIME_UNIT_MINUTE, TimeFrame.TIME_UNIT_MINUTE_SECOND, TimeFrame.TIME_UNIT_MINUTE_MILLISECONDS),
    ONE_HOUR(1, "1h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    TWO_HOURS(2, "2h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    FOUR_HOURS(4, "4h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    SIX_HOURS(6, "6h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    EIGHT_HOURS(8, "8h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    TWELFTH_HOURS(12, "12h", TimeFrame.TIME_UNIT_HR_MINUTE, TimeFrame.TIME_UNIT_HR_SECONDS, TimeFrame.TIME_UNIT_HR_MILLISECONDS),
    ONE_DAY(1, "1d", TimeFrame.TIME_UNIT_DAY_MINUTE, TimeFrame.TIME_UNIT_DAY_SECONDS, TimeFrame.TIME_UNIT_DAY_MILLISECONDS),
    THREE_DAYS(3, "1d", TimeFrame.TIME_UNIT_DAY_MINUTE, TimeFrame.TIME_UNIT_DAY_SECONDS, TimeFrame.TIME_UNIT_DAY_MILLISECONDS),
    ONE_WEEK(1, "1w", TimeFrame.TIME_UNIT_WEEK_MINUTE * 7, TimeFrame.TIME_UNIT_WEEK_SECONDS * 7, TimeFrame.TIME_UNIT_WEEK_MILLISECONDS),
    ONE_MONTH(1, "1M", TimeFrame.TIME_UNIT_MONTH_MINUTE, TimeFrame.TIME_UNIT_MONTH_SECONDS, TimeFrame.TIME_UNIT_MONTH_MILLISECONDS);

    private static final int TIME_UNIT_MINUTE = 1;
    private static final int TIME_UNIT_MINUTE_SECOND = 60;
    private static final int TIME_UNIT_MINUTE_MILLISECONDS = TIME_UNIT_MINUTE_SECOND * 1000;

    private static final int TIME_UNIT_HR_MINUTE = 60;
    private static final int TIME_UNIT_HR_SECONDS = TIME_UNIT_HR_MINUTE * 60;
    private static final int TIME_UNIT_HR_MILLISECONDS = TIME_UNIT_HR_SECONDS * 1000;

    private static final int TIME_UNIT_DAY_MINUTE = 24 * TIME_UNIT_HR_MINUTE;
    private static final int TIME_UNIT_DAY_SECONDS = TIME_UNIT_DAY_MINUTE * 60;
    private static final int TIME_UNIT_DAY_MILLISECONDS = TIME_UNIT_DAY_SECONDS * 1000;

    private static final int TIME_UNIT_WEEK_MINUTE = 7 * TIME_UNIT_DAY_MINUTE;
    private static final int TIME_UNIT_WEEK_SECONDS = TIME_UNIT_WEEK_MINUTE * 60;
    private static final int TIME_UNIT_WEEK_MILLISECONDS = TIME_UNIT_WEEK_SECONDS * 1000;

    private static final int TIME_UNIT_MONTH_MINUTE = 4 * TIME_UNIT_WEEK_MINUTE;
    private static final int TIME_UNIT_MONTH_SECONDS = TIME_UNIT_MONTH_MINUTE * 60;
    private static final int TIME_UNIT_MONTH_MILLISECONDS = TIME_UNIT_MONTH_SECONDS * 1000;

    public static final long UNIX_FACTOR = 1000;

    private int number;
    private String timeLabeled;

    int minuteFactor;
    int secondFactor;
    int millisecondUnitFactor;

    private long inMinutes, inSeconds, inMilliseconds;

    private TimeFrame(int number, String timeLabeled, int minuteFactor, int secondFactor, int millisecondUnitFactor) {
        this.number = number;
        this.timeLabeled = timeLabeled;
        this.minuteFactor = minuteFactor;
        this.secondFactor = secondFactor;
        this.millisecondUnitFactor = millisecondUnitFactor;

        inMinutes = number * minuteFactor;
        inSeconds = number * secondFactor;
        inMilliseconds = number * millisecondUnitFactor;
    }

    /**
     * This method determine how many time lower time frame can fit in the higher
     * time frame. The order of the time frame does not matter, it is determined in run time
     *
     * @param a
     * @param b
     * @return
     */
    public static long howManyTimeFit(TimeFrame a, TimeFrame b) {        
        if (a == b) {
            return 1;
        }
        
        long ma = a.toMinute();
        long mb = b.toMinute();

        return ma > mb? (ma / mb) : (mb / ma);
    }    
    
    public int getNumber() {
        return number;
    }

    public String getTimeLabeled() {
        return timeLabeled;
    }

    public int getMinuteFactor() {
        return minuteFactor;
    }

    public int getSecondFactor() {
        return secondFactor;
    }

    public int getMilisecondUnitFactor() {
        return millisecondUnitFactor;
    }

    public long toMinute() {
        return inMinutes;
    }

    public long toSecond() {
        return inSeconds;
    }

    public long toMilliSecond() {
        return inMilliseconds;
    }
}
