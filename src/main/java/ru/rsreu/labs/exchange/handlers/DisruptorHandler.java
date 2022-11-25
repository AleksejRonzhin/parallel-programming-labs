package ru.rsreu.labs.exchange.handlers;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.disruptor.CreateOrderEvent;
import ru.rsreu.labs.exchange.queue.requests.CreateOrderRequest;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.function.Consumer;
import java.util.function.Function;

public class DisruptorHandler implements CreateOrderRequestHandler {
    private final static int RING_BUFFER_SIZE = 16;
    private final Disruptor<CreateOrderEvent> disruptor;

    public DisruptorHandler() {
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
