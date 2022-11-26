package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;
import ru.rsreu.labs.exchange.helper.ExchangeHelper;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.List;

public class BufferExchange extends AbstractExchange {
    private final RequestBuffer requestBuffer;

    protected BufferExchange(boolean withCommission, ExchangeHelper helper, RequestBufferFactory bufferFactory) {
        super(withCommission, helper);
        this.requestBuffer = bufferFactory.create(this::handle);
    }

    public void handle(CreateOrderRequest request) {
        Order order = request.getOrder();
        request.setResult(unsafeCreateOrder(order));
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(order);
        try {
            requestBuffer.addRequest(createOrderRequest);
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
