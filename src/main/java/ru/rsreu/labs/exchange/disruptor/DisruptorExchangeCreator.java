package ru.rsreu.labs.exchange.disruptor;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;

public class DisruptorExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new DisruptorExchange(withCommission);
    }
}
