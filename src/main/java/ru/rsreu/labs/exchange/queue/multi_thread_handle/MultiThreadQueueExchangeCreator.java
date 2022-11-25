package ru.rsreu.labs.exchange.queue.multi_thread_handle;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.ExchangeCreator;

public class MultiThreadQueueExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new MultiThreadQueueExchange(withCommission);
    }
}
