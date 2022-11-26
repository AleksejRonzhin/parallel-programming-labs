package ru.rsreu.labs.exchange.sync;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeFactory;

public class SyncExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new SyncExchange(withCommission);
    }
}
