package ru.rsreu.labs.queue.single_thread_handle;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.ExchangeCreator;

public class SingleThreadQueueExchangeCreator implements ExchangeCreator {
    @Override
    public Exchange create(boolean withCommission) {
        return new SingleThreadQueueExchange(withCommission);
    }
}