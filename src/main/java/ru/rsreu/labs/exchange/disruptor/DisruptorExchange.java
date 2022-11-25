package ru.rsreu.labs.exchange.disruptor;

import ru.rsreu.labs.exchange.BufferExchange;
import ru.rsreu.labs.exchange.handlers.DisruptorHandler;

public class DisruptorExchange extends BufferExchange {

    public DisruptorExchange(boolean withCommission) {
        super(withCommission, new DisruptorHandler());
    }
}
