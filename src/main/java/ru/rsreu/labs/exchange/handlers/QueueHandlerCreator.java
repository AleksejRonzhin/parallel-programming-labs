package ru.rsreu.labs.exchange.handlers;

public class QueueHandlerCreator implements CreateOrderRequestHandlerFactory {
    @Override
    public CreateOrderRequestHandler create() {
        return new QueueHandler();
    }
}
