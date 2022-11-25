package ru.rsreu.labs.exchange.queue.single_thread_handle;

import ru.rsreu.labs.exchange.BufferExchange;
import ru.rsreu.labs.exchange.handlers.QueueHandler;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class SingleThreadQueueExchange extends BufferExchange {
    public SingleThreadQueueExchange(boolean withCommission) {
        super(withCommission, new QueueHandler());
    }
}
