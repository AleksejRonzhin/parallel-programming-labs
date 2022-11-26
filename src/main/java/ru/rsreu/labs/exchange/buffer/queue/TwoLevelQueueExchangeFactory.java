package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeFactory;

public class TwoLevelQueueExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new TwoLevelQueueExchange(withCommission);
    }
}
