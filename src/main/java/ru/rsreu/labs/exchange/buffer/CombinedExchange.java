package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.disruptor.DisruptorBufferFactory;
import ru.rsreu.labs.exchange.buffer.queue.QueueBuffer;

public class CombinedExchange extends TwoLevelBufferExchange {
    public CombinedExchange(boolean withCommission) {
        super(withCommission, new QueueBuffer(), new DisruptorBufferFactory());
    }
}
