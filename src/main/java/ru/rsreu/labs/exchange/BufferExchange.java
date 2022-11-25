package ru.rsreu.labs.exchange;

import ru.rsreu.labs.exchange.handlers.CreateOrderRequestHandler;
import ru.rsreu.labs.exchange.queue.requests.CreateOrderRequest;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.List;

public class BufferExchange extends AbstractExchange {
    private final CreateOrderRequestHandler handler;

    protected BufferExchange(boolean withCommission, CreateOrderRequestHandler handler) {
        super(withCommission, new UnsafeHelper());
        this.handler = handler;
        handler.start(this::handle);
    }

    public void handle(CreateOrderRequest request){
        Order order = request.getOrder();
        request.setResult(unsafeCreateOrder(order));
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(order);
        try {
            handler.push(createOrderRequest);
            return createOrderRequest.awaitResult();
        } catch (InterruptedException e) {
            return ResponseStatus.ERROR;
        }
    }

    @Override
    public List<Order> getOpenOrders() {
        return unsafeGetOpenOrders();
    }
}
