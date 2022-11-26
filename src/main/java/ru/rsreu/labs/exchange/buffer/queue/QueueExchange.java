package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.BufferExchange;
import ru.rsreu.labs.exchange.helper.UnsafeHelper;

public class QueueExchange extends BufferExchange {
    public QueueExchange(boolean withCommission) {
        super(withCommission, new UnsafeHelper(), new QueueBufferFactory());
    }
}