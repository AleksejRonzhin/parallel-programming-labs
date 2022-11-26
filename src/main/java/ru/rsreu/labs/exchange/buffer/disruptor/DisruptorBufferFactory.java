package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.dsl.ProducerType;
import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.RequestBufferFactory;
import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class DisruptorBufferFactory implements RequestBufferFactory {
    private final ProducerType producerType;

    public DisruptorBufferFactory(ProducerType producerType) {
        this.producerType = producerType;
    }

    @Override
    public RequestBuffer create(Consumer<CreateOrderRequest> consumer) {
        return new DisruptorBuffer(consumer, producerType);
    }
}