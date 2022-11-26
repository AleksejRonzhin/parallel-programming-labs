package ru.rsreu.labs.exchange.buffer.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.buffer.ProcessingRequestBuffer;
import ru.rsreu.labs.exchange.buffer.disruptor.models.CreateOrderEvent;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.function.Consumer;

public class DisruptorBuffer implements ProcessingRequestBuffer {
    private final static int RING_BUFFER_SIZE = 16;
    private final Disruptor<CreateOrderEvent> disruptor;

    public DisruptorBuffer() {
        disruptor = createDisruptor();

    }

    @Override
    public void start(Consumer<CreateOrderRequest> function) {
        disruptor.handleEventsWith((event, seq, endOfBatch) -> {
            CreateOrderRequest task = event.getTask();
            function.accept(task);
        });
        disruptor.start();
    }

    @Override
    public void push(CreateOrderRequest order) {
        disruptor.getRingBuffer().publishEvent((event, sequence) -> event.setTask(order));
    }

    private Disruptor<CreateOrderEvent> createDisruptor() {
        return new Disruptor<>(
                CreateOrderEvent.EVENT_FACTORY,
                RING_BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE
        );
    }
}
