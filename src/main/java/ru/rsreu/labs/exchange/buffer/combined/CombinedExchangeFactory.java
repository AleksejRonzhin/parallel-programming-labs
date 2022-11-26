package ru.rsreu.labs.exchange.buffer.combined;

import ru.rsreu.labs.exchange.ExchangeFactory;
import ru.rsreu.labs.exchange.Exchange;

public class CombinedExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new CombinedExchange(withCommission);
    }
}
