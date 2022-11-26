package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.RequestBufferFactory;
import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class QueueBufferFactory implements RequestBufferFactory {
    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new QueueBuffer(consumer);
    }
}
