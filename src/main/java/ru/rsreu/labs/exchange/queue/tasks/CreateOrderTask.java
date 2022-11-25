package ru.rsreu.labs.exchange.queue.tasks;

import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

public class CreateOrderTask extends QueueTask<ResponseStatus> {
    private final Order order;

    public CreateOrderTask(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
