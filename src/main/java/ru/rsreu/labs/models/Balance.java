package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Immutable
public class Balance {
    private final Map<Currency, BigDecimal> map;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        return map.equals(balance.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public Balance(Map<Currency, BigDecimal> map) {
        this.map = new HashMap<>(map);
    }

    public BigDecimal get(Currency currency) {
        BigDecimal result = map.get(currency);
        if(result == null) return BigDecimal.ZERO;
        return result;
    }

    public Balance add(Balance ordersBalance) {
        ConcurrentHashMap<Currency, BigDecimal> result = new ConcurrentHashMap<>();
        for (Currency currency : Currency.values()) {
            result.put(currency, this.map.getOrDefault(currency, BigDecimal.ZERO)
                    .add(ordersBalance.map.getOrDefault(currency, BigDecimal.ZERO)));
        }
        return new Balance(result);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}