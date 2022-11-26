package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.disruptor.DisruptorBufferFactory;
import ru.rsreu.labs.exchange.buffer.queue.QueueBufferFactory;
import ru.rsreu.labs.exchange.helper.SafeHelper;

public class CombinedExchange extends BufferExchange {
    public CombinedExchange(boolean withCommission) {
        super(withCommission, new SafeHelper(),
                new TwoLevelBufferFactory(new QueueBufferFactory(), new DisruptorBufferFactory()));
    }
}