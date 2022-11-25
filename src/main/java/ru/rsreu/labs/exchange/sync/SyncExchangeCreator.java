package ru.rsreu.labs.exchange.sync;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;

public class SyncExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new SyncExchange(withCommission);
    }
}
