package ru.rsreu.labs.exchange.queue.requests;

import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

public class CreateOrderRequest extends QueueRequest<ResponseStatus> {
    private final Order order;

    public CreateOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
