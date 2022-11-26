package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.exchange.helper.UnsafeHelper;
import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.List;

public class BufferExchange extends AbstractExchange {
    private final ProcessingRequestBuffer buffer;

    protected BufferExchange(boolean withCommission, ProcessingRequestBuffer buffer) {
        super(withCommission, new UnsafeHelper());
        this.buffer = buffer;
        buffer.start(this::handle);
    }

    public void handle(CreateOrderRequest request){
        Order order = request.getOrder();
        request.setResult(unsafeCreateOrder(order));
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(order);
        try {
            buffer.push(createOrderRequest);
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
