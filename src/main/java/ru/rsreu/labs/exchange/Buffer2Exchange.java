package ru.rsreu.labs.exchange;

import ru.rsreu.labs.exchange.handlers.CreateOrderRequestHandler;
import ru.rsreu.labs.exchange.handlers.CreateOrderRequestHandlerFactory;
import ru.rsreu.labs.exchange.queue.requests.CreateOrderRequest;
import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Buffer2Exchange extends AbstractExchange{
    private final CreateOrderRequestHandler mainBuffer;
    private final Map<CurrencyPair, CreateOrderRequestHandler> buffers = new HashMap<>();

    protected Buffer2Exchange(boolean withCommission,
                              CreateOrderRequestHandler mainBuffer,
                              CreateOrderRequestHandlerFactory factory) {
        super(withCommission, new SafeHelper());
        this.mainBuffer = mainBuffer;
        mainBuffer.start(this::mainHandle);

        CurrencyPair.getPairs().forEach((currencyPair -> {
            CreateOrderRequestHandler handler = factory.create();
            handler.start(this::handle);
            buffers.put(currencyPair, handler);
        }));
    }

    public void mainHandle(CreateOrderRequest request){
        CurrencyPair currencyPair = request.getOrder().getCurrencyPair();
        CreateOrderRequestHandler buffer = buffers.get(currencyPair);
        try {
            buffer.push(request);
        } catch (InterruptedException e) {
            request.setResult(ResponseStatus.ERROR);
        }
    }

    public void handle(CreateOrderRequest request){
        Order order = request.getOrder();
        request.setResult(unsafeCreateOrder(order));
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(order);
        try {
            mainBuffer.push(createOrderRequest);
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
