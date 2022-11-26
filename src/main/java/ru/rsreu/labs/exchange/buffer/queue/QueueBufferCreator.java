package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.ProcessingRequestBuffer;
import ru.rsreu.labs.exchange.buffer.ProcessingRequestBufferFactory;

public class QueueBufferCreator implements ProcessingRequestBufferFactory {
    @Override
    public ProcessingRequestBuffer create() {
        return new QueueBuffer();
    }
}
