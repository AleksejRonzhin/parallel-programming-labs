package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.buffer.disruptor.models.CreateOrderEvent;
import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class DisruptorBuffer implements RequestBuffer {
    private final static int RING_BUFFER_SIZE = 16;
    private final Disruptor<CreateOrderEvent> disruptor;

    public DisruptorBuffer(Consumer<CreateOrderRequest> consumer, ProducerType producerType) {
        disruptor = createDisruptor(producerType);
        disruptor.handleEventsWith((event, seq, endOfBatch) -> {
            CreateOrderRequest task = event.getTask();
            consumer.accept(task);
        });
        disruptor.start();
    }


    @Override
    public void addRequest(CreateOrderRequest request) {
        disruptor.getRingBuffer().publishEvent((event, sequence) -> event.setTask(request));
    }

    private Disruptor<CreateOrderEvent> createDisruptor(ProducerType producerType) {
        return new Disruptor<>(
                CreateOrderEvent.EVENT_FACTORY,
                RING_BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE,
                producerType,
                new BlockingWaitStrategy());
    }
}
