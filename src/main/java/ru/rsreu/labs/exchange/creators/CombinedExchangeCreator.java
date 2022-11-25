package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.CombinedExchange;
import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;
import ru.rsreu.labs.exchange.sync.SyncExchange;

public class CombinedExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new CombinedExchange(withCommission);
    }
}
