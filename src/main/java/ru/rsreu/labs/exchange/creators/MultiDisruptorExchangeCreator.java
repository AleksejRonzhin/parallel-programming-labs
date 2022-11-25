package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;
import ru.rsreu.labs.exchange.disruptor.MultiDisraptorExchange;
import ru.rsreu.labs.exchange.sync.SyncExchange;

public class MultiDisruptorExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new MultiDisraptorExchange(withCommission);
    }
}
