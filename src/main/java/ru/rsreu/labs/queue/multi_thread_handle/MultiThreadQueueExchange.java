package ru.rsreu.labs.queue.multi_thread_handle;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.queue.tasks.CreateOrderTask;
import ru.rsreu.labs.queue.QueueExchange;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@NotThreadSafe
public class MultiThreadQueueExchange extends QueueExchange {
    private final AtomicLong coverCount = new AtomicLong(0);
    private final ConcurrentHashMap<Currency, BigDecimal> bank = new ConcurrentHashMap<>();

    protected MultiThreadQueueExchange(boolean withCommission) {
        super(withCommission);
    }

    @Override
    protected void handleCreateOrderQueue() throws InterruptedException {
        // Здесь расскидываем по другим очередям вместо этого
        if (!createOrderQueue.isEmpty()) {
            CreateOrderTask createOrderTask = createOrderQueue.take();
            Order order = createOrderTask.getOrder();
            createOrderTask.setResult(unsafeCreateOrder(order));
        }
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    protected void incrementCoverCount() {
        coverCount.incrementAndGet();
    }

    @Override
    public List<Order> getOpenOrders() {
        return unsafeGetOpenOrders();
    }

    @Override
    public long getCoverCount() {
        return coverCount.get();
    }
}
