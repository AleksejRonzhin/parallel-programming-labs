package ru.rsreu.labs.queue.multi_thread_handle;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.queue.QueueExchange;
import ru.rsreu.labs.queue.tasks.CreateOrderTask;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

@NotThreadSafe
public class MultiThreadQueueExchange extends QueueExchange {
    private final AtomicLong coverCount = new AtomicLong(0);
    private final ConcurrentHashMap<Currency, BigDecimal> bank = new ConcurrentHashMap<>();
    private final Map<CurrencyPair, BlockingQueue<CreateOrderTask>> blockingQueues = new HashMap<>();

    protected MultiThreadQueueExchange(boolean withCommission) {
        super(withCommission);

        CurrencyPair.getPairs().forEach(pair -> {
            BlockingQueue<CreateOrderTask> queue = createBlockingQueueForCurrencyPair();
            blockingQueues.put(pair, queue);
        });
    }

    private BlockingQueue<CreateOrderTask> createBlockingQueueForCurrencyPair() {
        BlockingQueue<CreateOrderTask> queue = new LinkedBlockingQueue<>();
        startHandler(getCreateOrderQueueHandler(queue));
        return queue;
    }

    @Override
    public void handleBaseQueue() throws InterruptedException{
        CreateOrderTask createOrderTask = createOrderQueue.take();
        Order order = createOrderTask.getOrder();
        BlockingQueue<CreateOrderTask> queue = blockingQueues.get(order.getCurrencyPair());
        queue.put(createOrderTask);
    }

    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    protected void incrementCoverCount() {
        coverCount.incrementAndGet();
    }

    @Override
    public long getCoverCount() {
        return coverCount.get();
    }
}
