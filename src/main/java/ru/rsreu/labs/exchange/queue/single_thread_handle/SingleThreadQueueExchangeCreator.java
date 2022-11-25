package ru.rsreu.labs.exchange.queue.single_thread_handle;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;

public class SingleThreadQueueExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new SingleThreadQueueExchange(withCommission);
    }
}