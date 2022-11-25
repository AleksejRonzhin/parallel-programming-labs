package ru.rsreu.labs.exchange.queue;

import ru.rsreu.labs.exchange.Buffer2Exchange;
import ru.rsreu.labs.exchange.handlers.QueueHandler;
import ru.rsreu.labs.exchange.handlers.QueueHandlerCreator;

public class MultiQueueExchange extends Buffer2Exchange {

    public MultiQueueExchange(boolean withCommission) {
        super(withCommission, new QueueHandler(), new QueueHandlerCreator());
    }
}
