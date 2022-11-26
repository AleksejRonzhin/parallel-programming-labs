package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.BufferExchange;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class QueueExchange extends BufferExchange {
    public QueueExchange(boolean withCommission) {
        super(withCommission, new QueueBuffer());
    }
}
