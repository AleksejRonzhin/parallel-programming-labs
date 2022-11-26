package ru.rsreu.labs.exchange.buffer.combined;

import com.lmax.disruptor.dsl.ProducerType;
import ru.rsreu.labs.exchange.buffer.BufferExchange;
import ru.rsreu.labs.exchange.buffer.TwoLevelBufferFactory;
import ru.rsreu.labs.exchange.buffer.disruptor.DisruptorBufferFactory;
import ru.rsreu.labs.exchange.buffer.queue.QueueBufferFactory;
import ru.rsreu.labs.exchange.helper.SafeHelper;

public class CombinedExchange extends BufferExchange {
    public CombinedExchange(boolean withCommission) {
        super(withCommission, new SafeHelper(),
                new TwoLevelBufferFactory(new QueueBufferFactory(),
                        new DisruptorBufferFactory(ProducerType.SINGLE)));
    }
}