package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.ProcessingRequestBuffer;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class QueueBuffer implements ProcessingRequestBuffer {
    private final BlockingQueue<CreateOrderRequest> queue = new LinkedBlockingQueue<>();

    @Override
    public void start(Consumer<CreateOrderRequest> function){
        Thread thread = new Thread(
                () -> {
                    while (true) {
                        try {
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            CreateOrderRequest createOrderRequest = queue.take();
                            function.accept(createOrderRequest);
                        } catch (InterruptedException e) {
                            System.out.println("Handle thread is interrupted");
                        }
                    }
                }
        );
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void push(CreateOrderRequest request) throws InterruptedException {
        queue.put(request);
    }
}
