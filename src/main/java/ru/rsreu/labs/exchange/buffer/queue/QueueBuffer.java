package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.RequestBuffer;
import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class QueueBuffer implements RequestBuffer {
    private final BlockingQueue<CreateOrderRequest> queue;

    protected QueueBuffer(Consumer<CreateOrderRequest> consumer) {
        queue = new LinkedBlockingQueue<>();

        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    CreateOrderRequest createOrderRequest = queue.take();
                    consumer.accept(createOrderRequest);
                } catch (InterruptedException e) {
                    System.out.println("Handle thread is interrupted");
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public void addRequest(CreateOrderRequest request) throws InterruptedException {
        queue.put(request);
    }
}
