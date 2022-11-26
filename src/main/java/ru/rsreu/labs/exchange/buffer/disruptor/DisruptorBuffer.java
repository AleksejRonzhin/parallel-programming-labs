package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.disruptor.models.CreateOrderEvent;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class DisruptorBuffer implements RequestBuffer {
    private final static int RING_BUFFER_SIZE = 16;
    private final Disruptor<CreateOrderEvent> disruptor;

    public DisruptorBuffer(Consumer<CreateOrderRequest> consumer) {
        disruptor = createDisruptor();
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

    private Disruptor<CreateOrderEvent> createDisruptor() {
        return new Disruptor<>(CreateOrderEvent.EVENT_FACTORY, RING_BUFFER_SIZE, DaemonThreadFactory.INSTANCE);
    }
}
