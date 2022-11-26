package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;
import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class TwoLevelBuffer implements RequestBuffer {
    private final RequestBuffer mainRequestBuffer;
    private final Map<CurrencyPair, RequestBuffer> twoLevelBuffers;

    public TwoLevelBuffer(RequestBufferFactory mainBufferFactory,
                          RequestBufferFactory twoLevelBufferFactory,
                          Consumer<CreateOrderRequest> consumer) {
        this.mainRequestBuffer = mainBufferFactory.create(this::handle);
        twoLevelBuffers = new HashMap<>();
        CurrencyPair.getPairs().forEach(currencyPair -> {
            RequestBuffer requestBuffer = twoLevelBufferFactory.create(consumer);
            twoLevelBuffers.put(currencyPair, requestBuffer);
        });
    }

    public void handle(CreateOrderRequest request) {
        RequestBuffer requestBuffer = twoLevelBuffers.get(request.getOrder().getCurrencyPair());
        try {
            requestBuffer.addRequest(request);
        } catch (InterruptedException e) {
            request.setResult(ResponseStatus.ERROR);
        }
    }

    @Override
    public void addRequest(CreateOrderRequest request) throws InterruptedException {
        mainRequestBuffer.addRequest(request);
    }
}
