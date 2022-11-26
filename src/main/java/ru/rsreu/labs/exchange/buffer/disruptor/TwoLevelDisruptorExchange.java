package ru.rsreu.labs.exchange.buffer.disruptor;

import ru.rsreu.labs.exchange.buffer.TwoLevelBufferExchange;

public class TwoLevelDisruptorExchange extends TwoLevelBufferExchange {
    public TwoLevelDisruptorExchange(boolean withCommission) {
        super(withCommission, new DisruptorBuffer(), new DisruptorBufferFactory());
    }
}
