package ru.rsreu.labs.queue;

import ru.rsreu.labs.AbstractExchange;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

@ThreadSafe
public class QueueExchange extends AbstractExchange {
    private final BlockingQueue<CreateOrderTask> createOrderQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<GetOpenOrdersTask> getOpenOrdersQueue = new LinkedBlockingQueue<>();
    private final Map<Currency, BigDecimal> bank = new HashMap<>();

    private long coverCount = 0;

    {
        Thread thread = new Thread(this::queueHandle);
        thread.setDaemon(true);
        thread.start();
    }

    public QueueExchange(boolean withCommission) {
        super(withCommission);
    }

    private void queueHandle() {
        while (true) {
            try {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                handleCreateOrderQueue();
                handleGetOrdersQueue();
            } catch (InterruptedException e) {
                System.out.println("Handle thread is interrupted");
            }
        }
    }

    private void handleCreateOrderQueue() throws InterruptedException {
        if (!createOrderQueue.isEmpty()) {
            CreateOrderTask createOrderTask = createOrderQueue.take();
            Order order = createOrderTask.getOrder();
            createOrderTask.setResult(super.createOrder(order));
        }
    }

    private void handleGetOrdersQueue() throws InterruptedException {
        if (!getOpenOrdersQueue.isEmpty()) {
            GetOpenOrdersTask getOpenOrdersTask = getOpenOrdersQueue.take();
            getOpenOrdersTask.setResult(super.getOpenOrders());
        }
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        try {
            CreateOrderTask createOrderTask = new CreateOrderTask(order);
            createOrderQueue.put(createOrderTask);
            return createOrderTask.awaitResult();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
    public void incrementCoverCount() {
        coverCount++;
    }

    private static class CreateOrderTask extends QueueTask<ResponseStatus> {
        private final Order order;

        private CreateOrderTask(Order order) {
            this.order = order;
        }

        public Order getOrder() {
            return order;
        }
    }

    private static class GetOpenOrdersTask extends QueueTask<List<Order>> {

    }

    private static class QueueTask<T> {
        private final CountDownLatch latch = new CountDownLatch(1);
        private T result;

        public void setResult(T result) {
            this.result = result;
            latch.countDown();
        }

        public T awaitResult() throws InterruptedException {
            latch.await();
            return result;
        }
    }
}
