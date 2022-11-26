package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

public interface RequestBuffer {
    void addRequest(CreateOrderRequest request) throws InterruptedException;
}
