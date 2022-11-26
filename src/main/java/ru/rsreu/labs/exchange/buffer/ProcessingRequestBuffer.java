package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public interface ProcessingRequestBuffer {
    void start(Consumer<CreateOrderRequest> function);

    void push(CreateOrderRequest order) throws InterruptedException;
}
