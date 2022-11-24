package ru.rsreu.labs.queue.single_thread_handle;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.queue.tasks.CreateOrderTask;
import ru.rsreu.labs.queue.tasks.GetOpenOrdersTask;
import ru.rsreu.labs.queue.QueueExchange;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@ThreadSafe
public class SingleThreadQueueExchange extends QueueExchange {
    private final BlockingQueue<GetOpenOrdersTask> getOpenOrdersQueue = new LinkedBlockingQueue<>();
    private final Map<Currency, BigDecimal> bank = new HashMap<>();
    private long coverCount = 0;

    public SingleThreadQueueExchange(boolean withCommission) {
        super(withCommission);
    }

    @Override
    protected void queueHandle() throws InterruptedException {
        handleCreateOrderQueue();
        handleGetOrdersQueue();
    }

    @Override
    protected void handleCreateOrderQueue() throws InterruptedException {
        if (!createOrderQueue.isEmpty()) {
            CreateOrderTask createOrderTask = createOrderQueue.take();
            Order order = createOrderTask.getOrder();
            createOrderTask.setResult(unsafeCreateOrder(order));
        }
    }

    private void handleGetOrdersQueue() throws InterruptedException {
        if (!getOpenOrdersQueue.isEmpty()) {
            GetOpenOrdersTask getOpenOrdersTask = getOpenOrdersQueue.take();
            getOpenOrdersTask.setResult(unsafeGetOpenOrders());
        }
    }

    @Override
    public List<Order> getOpenOrders() {
        try {
            GetOpenOrdersTask task = new GetOpenOrdersTask();
            getOpenOrdersQueue.put(task);
            return task.awaitResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getCoverCount() {
        return coverCount;
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    protected void incrementCoverCount() {
        coverCount++;
    }
}
