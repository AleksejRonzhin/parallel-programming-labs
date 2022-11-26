package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class TwoLevelBufferFactory implements RequestBufferFactory {
    private final RequestBufferFactory mainBufferFactory;
    private final RequestBufferFactory twoLevelBufferFactory;

    public TwoLevelBufferFactory(RequestBufferFactory mainBufferFactory, RequestBufferFactory twoLevelBufferFactory) {
        this.mainBufferFactory = mainBufferFactory;
        this.twoLevelBufferFactory = twoLevelBufferFactory;
    }

    public TwoLevelBufferFactory(RequestBufferFactory bufferFactory) {
        this.mainBufferFactory = bufferFactory;
        this.twoLevelBufferFactory = bufferFactory;
    }

    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new TwoLevelBuffer(mainBufferFactory, twoLevelBufferFactory, consumer);
    }
}