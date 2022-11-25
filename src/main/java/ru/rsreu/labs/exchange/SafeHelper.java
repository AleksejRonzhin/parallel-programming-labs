package ru.rsreu.labs.exchange;

import ru.rsreu.labs.models.Currency;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class SafeHelper implements ExchangeHelper{
    private final Map<Currency, BigDecimal> bank = new ConcurrentHashMap<>();
    private final AtomicLong coverCount = new AtomicLong(0L);

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    public void incrementCoverCount() {
        coverCount.incrementAndGet();
    }

    @Override
    public long getCoverCount() {
        return coverCount.get();
    }
}
