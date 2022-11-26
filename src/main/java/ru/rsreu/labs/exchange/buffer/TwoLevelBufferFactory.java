package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class TwoLevelBufferFactory implements ProcessingRequestBufferFactory {
    private final ProcessingRequestBufferFactory mainBufferFactory;
    private final ProcessingRequestBufferFactory twoLevelBufferFactory;

    public TwoLevelBufferFactory(ProcessingRequestBufferFactory mainBufferFactory, ProcessingRequestBufferFactory twoLevelBufferFactory) {
        this.mainBufferFactory = mainBufferFactory;
        this.twoLevelBufferFactory = twoLevelBufferFactory;
    }

    public TwoLevelBufferFactory(ProcessingRequestBufferFactory bufferFactory) {
        this.mainBufferFactory = bufferFactory;
        this.twoLevelBufferFactory = bufferFactory;
    }

    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new TwoLevelBuffer(mainBufferFactory, twoLevelBufferFactory, consumer);
    }
}