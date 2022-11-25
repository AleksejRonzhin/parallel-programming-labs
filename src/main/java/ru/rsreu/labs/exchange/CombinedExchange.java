package ru.rsreu.labs.exchange;

import ru.rsreu.labs.exchange.handlers.CreateOrderRequestHandler;
import ru.rsreu.labs.exchange.handlers.CreateOrderRequestHandlerFactory;
import ru.rsreu.labs.exchange.handlers.DisruptorHandlerFactory;
import ru.rsreu.labs.exchange.handlers.QueueHandler;

public class CombinedExchange extends Buffer2Exchange{
    public CombinedExchange(boolean withCommission) {
        super(withCommission, new QueueHandler(), new DisruptorHandlerFactory());
    }
}
