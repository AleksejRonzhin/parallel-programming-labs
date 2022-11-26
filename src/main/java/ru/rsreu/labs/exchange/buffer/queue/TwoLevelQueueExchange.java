package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.BufferExchange;
import ru.rsreu.labs.exchange.buffer.TwoLevelBufferFactory;
import ru.rsreu.labs.exchange.helper.SafeHelper;

public class TwoLevelQueueExchange extends BufferExchange {
    public TwoLevelQueueExchange(boolean withCommission) {
        super(withCommission, new SafeHelper(),
                new TwoLevelBufferFactory(new QueueBufferFactory()));
    }
}