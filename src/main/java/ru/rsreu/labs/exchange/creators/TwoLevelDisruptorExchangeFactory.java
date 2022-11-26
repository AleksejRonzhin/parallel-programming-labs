package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.buffer.disruptor.TwoLevelDisruptorExchange;

public class TwoLevelDisruptorExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new TwoLevelDisruptorExchange(withCommission);
    }
}
