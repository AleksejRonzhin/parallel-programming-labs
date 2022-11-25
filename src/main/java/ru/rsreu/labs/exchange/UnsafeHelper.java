package ru.rsreu.labs.exchange;

import ru.rsreu.labs.models.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UnsafeHelper implements ExchangeHelper {
    private final Map<Currency, BigDecimal> bank = new HashMap<>();
    private long coverCount = 0L;

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    public void incrementCoverCount() {
        coverCount++;
    }

    @Override
    public long getCoverCount() {
        return coverCount;
    }
}
