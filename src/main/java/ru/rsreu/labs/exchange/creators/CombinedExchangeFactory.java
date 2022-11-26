package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.buffer.CombinedExchange;
import ru.rsreu.labs.exchange.Exchange;

public class CombinedExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new CombinedExchange(withCommission);
    }
}
