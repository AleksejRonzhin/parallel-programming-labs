package ru.rsreu.labs.exchange.buffer.disruptor;

import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.ProcessingRequestBufferFactory;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class DisruptorBufferFactory implements ProcessingRequestBufferFactory {
    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new DisruptorBuffer(consumer);
    }
}