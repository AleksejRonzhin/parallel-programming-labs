package ru.rsreu.labs.queue;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.ExchangeCreator;

public class QueueExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new QueueExchange();
    }
}
