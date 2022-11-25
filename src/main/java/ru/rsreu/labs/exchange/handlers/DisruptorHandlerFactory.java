package ru.rsreu.labs.exchange.handlers;

public class DisruptorHandlerFactory implements CreateOrderRequestHandlerFactory {
    @Override
    public CreateOrderRequestHandler create() {
        return new DisruptorHandler();
    }
}
