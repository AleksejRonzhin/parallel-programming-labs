package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;

@Immutable
public class CurrencyPair {
    private final Currency firstCurrency;
    private final Currency secondCurrency;

    public CurrencyPair(Currency firstCurrency, Currency secondCurrency) {
        this.firstCurrency = firstCurrency;
        this.secondCurrency = secondCurrency;
    }

    public Currency getSecondCurrency() {
        return secondCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyPair that = (CurrencyPair) o;

        if(firstCurrency == that.firstCurrency && secondCurrency == that.secondCurrency) return true;
        if(firstCurrency == that.secondCurrency && secondCurrency == that.firstCurrency) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int result = firstCurrency.hashCode() + secondCurrency.hashCode();
        return result;
    }

    public Currency getFirstCurrency() {
        return firstCurrency;
    }
}
