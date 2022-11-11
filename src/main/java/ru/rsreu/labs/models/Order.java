package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;

@Immutable
public class Order {
    private final Currency sourceCurrency;
    private final Currency targetCurrency;
    private final OrderInfo orderInfo;
    private final CurrencyPair currencyPair;

    public Order(Currency sourceCurrency, Currency targetCurrency, OrderInfo orderInfo) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.orderInfo = orderInfo;
        currencyPair = new CurrencyPair(sourceCurrency, targetCurrency);
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public CurrencyPair getCurrencyPair(){
        return currencyPair;
    }
}
