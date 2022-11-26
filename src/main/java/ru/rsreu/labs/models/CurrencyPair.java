package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Immutable
public class CurrencyPair {
    private final static Collection<CurrencyPair> pairs;

    public static Collection<CurrencyPair> getPairs(){
        return Collections.unmodifiableCollection(pairs);
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
                "firstCurrency=" + firstCurrency +
                ", secondCurrency=" + secondCurrency +
                '}';
    }

    static {
        pairs = new ArrayList<>();
        Currency[] currencies = Currency.values();
        for (int i = 0; i < currencies.length; i++) {
            for (int j = i; j < currencies.length; j++) {
                pairs.add(new CurrencyPair(currencies[i], currencies[j]));
            }
        }
    }

    private final Currency firstCurrency;
    private final Currency secondCurrency;

    private CurrencyPair(Currency firstCurrency, Currency secondCurrency) {
        this.firstCurrency = firstCurrency;
        this.secondCurrency = secondCurrency;
    }

    public static CurrencyPair create(Currency firstCurrency, Currency secondCurrency) {
        for (CurrencyPair pair : pairs) {
            if (pair.firstCurrency == firstCurrency && pair.secondCurrency == secondCurrency) {
                return pair;
            }

            if (pair.firstCurrency == secondCurrency && pair.secondCurrency == firstCurrency) {
                return pair;
            }
        }
        throw new RuntimeException();
    }

    public Currency getSecondCurrency() {
        return secondCurrency;
    }

    public Currency getFirstCurrency() {
        return firstCurrency;
    }
}
