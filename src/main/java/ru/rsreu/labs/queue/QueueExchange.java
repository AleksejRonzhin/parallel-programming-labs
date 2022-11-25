package ru.rsreu.labs.queue;

import ru.rsreu.labs.AbstractExchange;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;
import ru.rsreu.labs.queue.tasks.CreateOrderTask;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@NotThreadSafe
public abstract class QueueExchange extends AbstractExchange {
    protected final BlockingQueue<CreateOrderTask> createOrderQueue = new LinkedBlockingQueue<>();

    protected QueueExchange(boolean withCommission){
        super(withCommission);
        startHandler(this::handleBaseQueue);
    }

    protected static void startHandler(QueueHandler handler) {
        Thread thread = new Thread(wrapHandler(handler));
        thread.setDaemon(true);
        thread.start();
    }

    private static Runnable wrapHandler(QueueHandler handler) {
        return () -> {
            while (true) {
                try {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    handler.handle();
                } catch (InterruptedException e) {
                    System.out.println("Handle thread is interrupted");
                }
            }
        };
    }

    protected void handleBaseQueue() throws InterruptedException {
        getCreateOrderQueueHandler(createOrderQueue).handle();
    }

    protected QueueHandler getCreateOrderQueueHandler(BlockingQueue<CreateOrderTask> queue) {
        return () -> {
            CreateOrderTask createOrderTask = queue.take();
            Order order = createOrderTask.getOrder();
            createOrderTask.setResult(unsafeCreateOrder(order));
        };
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
        return unsafeGetOpenOrders();
    }

    @FunctionalInterface
    public interface QueueHandler {
        void handle() throws InterruptedException;
    }
}
