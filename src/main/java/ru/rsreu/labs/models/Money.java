package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Immutable
public class Money{
    private final Map<Currency, BigDecimal> map;

    public Money(Map<Currency, BigDecimal> map) {
        this.map = new HashMap<>(map);
    }

    public BigDecimal get(Currency currency) {
        BigDecimal result = map.get(currency);
        if(result == null) return BigDecimal.ZERO;
        return result;
    }

    public Money add(Money ordersMoney) {
        ConcurrentHashMap<Currency, BigDecimal> result = new ConcurrentHashMap<>();
        for (Currency currency : Currency.values()) {
            result.put(currency, this.map.getOrDefault(currency, BigDecimal.ZERO)
                    .add(ordersMoney.map.getOrDefault(currency, BigDecimal.ZERO)));
        }
        return new Money(result);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}