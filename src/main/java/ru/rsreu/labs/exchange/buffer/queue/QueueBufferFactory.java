package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.ProcessingRequestBufferFactory;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class QueueBufferFactory implements ProcessingRequestBufferFactory {
    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new QueueBuffer(consumer);
    }
}
