package ru.rsreu.labs.models;

public class Order {
    private final Currency sourceCurrency;
    private final Currency targetCurrency;
    private final double value;
    private final double rate;

    public Order(Currency sourceCurrency, Currency targetCurrency, double value, double rate) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.value = value;
        this.rate = rate;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public double getValue() {
        return value;
    }

    public double getRate() {
        return rate;
    }
}
