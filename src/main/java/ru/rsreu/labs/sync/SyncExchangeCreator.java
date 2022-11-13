package ru.rsreu.labs.sync;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.ExchangeCreator;

public class SyncExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new SyncExchange(withCommission);
    }
}
