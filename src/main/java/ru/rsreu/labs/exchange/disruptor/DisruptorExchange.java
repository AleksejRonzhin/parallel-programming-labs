package ru.rsreu.labs.exchange.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.exchange.queue.tasks.CreateOrderTask;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisruptorExchange extends AbstractExchange {
    private final static int RING_BUFFER_SIZE = 16;
    private final Map<Currency, BigDecimal> bank = new HashMap<>();
    private final RingBuffer<CreateOrderEvent> buffer;
    private long covers = 0;

    protected DisruptorExchange(boolean withCommission) {
        super(withCommission);

        Disruptor<CreateOrderEvent> disruptor = createDisruptor();
        disruptor.handleEventsWith(this::handle);
        buffer = disruptor.getRingBuffer();
        disruptor.start();
    }

    private void handle(CreateOrderEvent event, long seq, boolean endOfBatch
    ) {
        CreateOrderTask task = event.getTask();
        Order order = task.getOrder();
        task.setResult(unsafeCreateOrder(order));
    }

    private Disruptor<CreateOrderEvent> createDisruptor() {
        return new Disruptor<>(
                CreateOrderEvent.EVENT_FACTORY,
                RING_BUFFER_SIZE,
                DaemonThreadFactory.INSTANCE
        );
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        CreateOrderTask task = new CreateOrderTask(order);
        buffer.publishEvent((event, sequence) -> event.setTask(task));

        try {
            return task.awaitResult();
        } catch (InterruptedException e) {
            return ResponseStatus.ERROR;
        }
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    protected void incrementCoverCount() {
        covers++;
    }

    @Override
    public List<Order> getOpenOrders() {
        return unsafeGetOpenOrders();
    }

    @Override
    public long getCoverCount() {
        return covers;
    }
}
