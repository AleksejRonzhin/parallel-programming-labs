package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;

@Immutable
public class Order {
    private final CurrencyPair currencyPair;
    private final OrderInfo orderInfo;

    public Order(CurrencyPair currencyPair, OrderInfo orderInfo) {
        this.currencyPair = currencyPair;
        this.orderInfo = orderInfo;
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public Currency getSourceCurrency() {
        return currencyPair.getSourceCurrency();
    }

    public Currency getTargetCurrency() {
        return currencyPair.getTargetCurrency();
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public BigDecimal getTargetValue() {
        return orderInfo.getTargetValue();
    }

    public BigDecimal getSourceValue() {
        return orderInfo.getSourceValue();
    }

    public BigDecimal getSourceToTargetRate() {
        return orderInfo.getSourceToTargetRate();
    }

    public BigDecimal getTargetToSourceRate() {
        return orderInfo.getTargetToSourceRate();
    }

    public Client getClient() {
        return orderInfo.getClient();
    }

    @Override
    public String toString() {
        return "Order{" + "sourceCurrency=" + currencyPair.getSourceCurrency() + ", targetCurrency=" + currencyPair.getTargetCurrency() + ", orderInfo=" + orderInfo;
    }
}
