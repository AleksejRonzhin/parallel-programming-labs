package ru.rsreu.labs.exchange.buffer.disruptor;

import ru.rsreu.labs.exchange.buffer.BufferExchange;

public class DisruptorExchange extends BufferExchange {
    public DisruptorExchange(boolean withCommission) {
        super(withCommission, new DisruptorBuffer());
    }
}
