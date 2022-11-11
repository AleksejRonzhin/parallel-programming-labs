package ru.rsreu.labs.models;

import java.math.BigDecimal;

public class OrderInfo {
    private final BigDecimal targetValue;
    private final BigDecimal rate; // source/target
    private final Client client;

    public OrderInfo(BigDecimal targetValue, BigDecimal rate, Client client) {
        this.targetValue = targetValue;
        this.rate = rate;
        this.client = client;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public Client getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderInfo orderInfo = (OrderInfo) o;

        if (!targetValue.equals(orderInfo.targetValue)) return false;
        if (!rate.equals(orderInfo.rate)) return false;
        return client.equals(orderInfo.client);
    }

    @Override
    public int hashCode() {
        int result = targetValue.hashCode();
        result = 31 * result + rate.hashCode();
        result = 31 * result + client.hashCode();
        return result;
    }
}
