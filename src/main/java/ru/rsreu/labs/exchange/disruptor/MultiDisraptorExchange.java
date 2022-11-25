package ru.rsreu.labs.exchange.disruptor;

import ru.rsreu.labs.exchange.Buffer2Exchange;
import ru.rsreu.labs.exchange.handlers.DisruptorHandler;
import ru.rsreu.labs.exchange.handlers.DisruptorHandlerFactory;

public class MultiDisraptorExchange extends Buffer2Exchange {
    public MultiDisraptorExchange(boolean withCommission) {
        super(withCommission, new DisruptorHandler(), new DisruptorHandlerFactory());
    }
}
