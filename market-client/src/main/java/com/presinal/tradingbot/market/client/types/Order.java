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

package com.presinal.tradingbot.market.client.types;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import com.presinal.tradingbot.market.client.enums.OrderStatus;
import com.presinal.tradingbot.market.client.enums.OrderType;

/**
 *
 * @author Miguel Presinal<presinal378@gmail.com>
 * @since 1.0
 */
public class Order implements Serializable {
    
    public static final String SIDE_BUY = "BUY";
    public static final String SIDE_SELL = "SELL";

    private AssetPair assetPair;
    private String orderId;
    private String clientOrderId;
    private Date transactionTime;
    private double price;
    private double quantity;
    private double executedQty;
    private OrderStatus status;
    private OrderType type; 
    private String side;

    public AssetPair getAssetPair() {
        return assetPair;
    }

    public void setAssetPair(AssetPair assetPair) {
        this.assetPair = assetPair;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientOrderId() {
        return clientOrderId;
    }

    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getExecutedQty() {
        return executedQty;
    }

    public void setExecutedQty(double executedQty) {
        this.executedQty = executedQty;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.assetPair);
        hash = 89 * hash + Objects.hashCode(this.orderId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (!Objects.equals(this.assetPair, other.assetPair)) {
            return false;
        }
        if (this.orderId != other.orderId) {
            return false;
        }
        if (!Objects.equals(this.clientOrderId, other.clientOrderId)) {
            return false;
        }
        if (!Objects.equals(this.transactionTime, other.transactionTime)) {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (Double.doubleToLongBits(this.quantity) != Double.doubleToLongBits(other.quantity)) {
            return false;
        }
        if (Double.doubleToLongBits(this.executedQty) != Double.doubleToLongBits(other.executedQty)) {
            return false;
        }
        if (this.status != other.status) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.side, other.side)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "assetPair=" + assetPair 
                + ", orderId=" + orderId 
                + ", clientOrderId=" + clientOrderId 
                + ", transactionTime=" + transactionTime 
                + ", price=" + price 
                + ", quantity=" + quantity 
                + ", executedQty=" + executedQty 
                + ", status=" + status 
                + ", type=" + type 
                + ", side=" + side + "}";
    }
    
    
}
