package ru.rsreu.labs.exchange.buffer.models.requests;

import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

public class CreateOrderRequest extends ExchangeRequest<ResponseStatus> {
    private final Order order;

    public CreateOrderRequest(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
