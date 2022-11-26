package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.dsl.ProducerType;
import ru.rsreu.labs.exchange.buffer.BufferExchange;
import ru.rsreu.labs.exchange.buffer.TwoLevelBufferFactory;
import ru.rsreu.labs.exchange.helper.SafeHelper;

public class TwoLevelDisruptorExchange extends BufferExchange {
    public TwoLevelDisruptorExchange(boolean withCommission) {
        super(withCommission, new SafeHelper(), new TwoLevelBufferFactory(new DisruptorBufferFactory(ProducerType.MULTI),
                new DisruptorBufferFactory(ProducerType.SINGLE)));
    }
}