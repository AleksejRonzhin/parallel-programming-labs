package ru.rsreu.labs.exchange.helper;

import ru.rsreu.labs.models.Currency;

import java.math.BigDecimal;
import java.util.Map;

public interface ExchangeHelper {
    Map<Currency, BigDecimal> getBank();

    void incrementCoverCount();

    long getCoverCount();
}
