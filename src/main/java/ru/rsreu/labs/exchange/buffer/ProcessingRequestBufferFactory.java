package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.requests.CreateOrderRequest;
import java.util.function.Consumer;

public interface ProcessingRequestBufferFactory {
    RequestBuffer create(Consumer<CreateOrderRequest> consumer);
}
