package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.dsl.ProducerType;
import ru.rsreu.labs.exchange.buffer.BufferExchange;
import ru.rsreu.labs.exchange.helper.UnsafeHelper;

public class DisruptorExchange extends BufferExchange {
    public DisruptorExchange(boolean withCommission) {
        super(withCommission, new UnsafeHelper(), new DisruptorBufferFactory(ProducerType.MULTI));
    }
}