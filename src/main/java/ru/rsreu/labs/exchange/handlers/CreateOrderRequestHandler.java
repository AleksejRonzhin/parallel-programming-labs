package ru.rsreu.labs.exchange.handlers;

import ru.rsreu.labs.exchange.queue.requests.CreateOrderRequest;

import java.util.function.Consumer;

public interface CreateOrderRequestHandler {

    void start(Consumer<CreateOrderRequest> function);

    void push(CreateOrderRequest order) throws InterruptedException;
}
