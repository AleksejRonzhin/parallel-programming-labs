package ru.rsreu.labs.queue;

import ru.rsreu.labs.AbstractExchange;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;
import ru.rsreu.labs.queue.tasks.CreateOrderTask;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@NotThreadSafe
public abstract class QueueExchange extends AbstractExchange {
    protected final BlockingQueue<CreateOrderTask> createOrderQueue = new LinkedBlockingQueue<>();

    {
        Thread thread = new Thread(this::handleThreadTarget);
        thread.setDaemon(true);
        thread.start();
    }

    protected QueueExchange(boolean withCommission) {
        super(withCommission);
    }

    private void handleThreadTarget() {
        while (true) {
            try {
                queueHandle();
            } catch (InterruptedException e) {
                System.out.println("Handle thread is interrupted");
            }
        }
    }

    protected void queueHandle() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        handleCreateOrderQueue();
    }

    protected abstract void handleCreateOrderQueue() throws InterruptedException;

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
}
