package ru.rsreu.labs.exchange.buffer;

import ru.rsreu.labs.exchange.buffer.models.requests.CreateOrderRequest;
import java.util.function.Consumer;

public interface RequestBufferFactory {
    RequestBuffer create(Consumer<CreateOrderRequest> consumer);
}
