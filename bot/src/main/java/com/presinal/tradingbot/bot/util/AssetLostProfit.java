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
package com.presinal.tradingbot.bot.util;

import java.io.Serializable;
import java.util.Date;
import com.presinal.tradingbot.market.client.types.AssetPair;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class AssetLostProfit implements Serializable {

    private double buyPrice;
    private double sellPrice;
    private AssetPair asset;
    private Date buyDate;
    private Date sellDate;

    private double profit = -1;
    private double profitPercentage;

    public void computeProfits() {
        profit = (sellPrice - buyPrice);
        profitPercentage = (profit / buyPrice) * 100.0;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public AssetPair getAsset() {
        return asset;
    }

    public void setAsset(AssetPair asset) {
        this.asset = asset;
    }

    public Date getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public Date getSellDate() {
        return sellDate;
    }

    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    public void setProfitPercentage(double profitPercentage) {
        this.profitPercentage = profitPercentage;
    }

    
    public boolean IsLoose() {
        return profit < 0;
    }

    public boolean isProfit() {
        return !IsLoose();
    }    

    @Override
    public String toString() {
        return "AssetLostProfit{buyPrice=" + buyPrice 
                + ", sellPrice=" + sellPrice 
                + ", asset=" + asset 
                + ", buyDate=" + buyDate 
                + ", sellDate=" + sellDate 
                + ", profit=" + profit 
                + ", profitPercentage=" + profitPercentage 
                + "}";
    }

   
}
