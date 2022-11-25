package ru.rsreu.labs.exchange.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;
import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.exchange.queue.tasks.CreateOrderTask;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

public class DisruptorExchange extends AbstractExchange {

    private final Disruptor<Event> disruptor;
    ByteBuffer byteBuffer = ByteBuffer.allocate(8);
    private long covers = 0;

    protected DisruptorExchange(boolean withCommission) {
        super(withCommission);
        disruptor = new Disruptor<>(Event::new, 1024, DaemonThreadFactory.INSTANCE);
        disruptor.handleEventsWith((event, seq, endOfBatch) -> {
            CreateOrderTask task = event.getTask();
            Order order = task.getOrder();
            task.setResult(unsafeCreateOrder(order));
        });
    }

    public static class Event{
        private CreateOrderTask task;

        public void setTask(CreateOrderTask task){
            this.task = task;
        }

        public CreateOrderTask getTask(){
            return task;
        }
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return null;
    }

    @Override
    protected void incrementCoverCount() {
        covers++;
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> getOpenOrders() {
        return null;
    }

    @Override
    public long getCoverCount() {
        return 0;
    }
}
