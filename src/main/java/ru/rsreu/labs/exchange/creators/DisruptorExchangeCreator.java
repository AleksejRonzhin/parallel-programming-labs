package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;
import ru.rsreu.labs.exchange.disruptor.DisruptorExchange;

public class DisruptorExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new DisruptorExchange(withCommission);
    }
}
