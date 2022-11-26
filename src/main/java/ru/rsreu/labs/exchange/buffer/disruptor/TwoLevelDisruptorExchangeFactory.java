package ru.rsreu.labs.exchange.buffer.disruptor;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeFactory;

public class TwoLevelDisruptorExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new TwoLevelDisruptorExchange(withCommission);
    }
}
