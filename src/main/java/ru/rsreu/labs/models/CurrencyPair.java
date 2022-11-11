package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;

@Immutable
public class CurrencyPair {
    private final Currency sourceCurrency;
    private final Currency targetCurrency;

    public CurrencyPair(Currency sourceCurrency, Currency secondCurrency) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = secondCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public Currency getSourceCurrency() {
        return sourceCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrencyPair that = (CurrencyPair) o;

        if (sourceCurrency != that.sourceCurrency) return false;
        return targetCurrency == that.targetCurrency;
    }

    @Override
    public int hashCode() {
        int result = sourceCurrency.hashCode();
        result = 31 * result + targetCurrency.hashCode();
        return result;
    }

    public CurrencyPair inverse(){
        return new CurrencyPair(targetCurrency, sourceCurrency);
    }
}
