package ru.rsreu.labs.exchange.buffer.disruptor;

import ru.rsreu.labs.exchange.buffer.ProcessingRequestBuffer;
import ru.rsreu.labs.exchange.buffer.ProcessingRequestBufferFactory;

public class DisruptorBufferFactory implements ProcessingRequestBufferFactory {
    @Override
    public ProcessingRequestBuffer create() {
        return new DisruptorBuffer();
    }
}