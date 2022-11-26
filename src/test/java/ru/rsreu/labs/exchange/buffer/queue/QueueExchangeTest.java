package ru.rsreu.labs.exchange.buffer.queue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.QueueExchangeFactory;

@Order(2)
@Tag("exchange")
class QueueExchangeTest extends ExchangeTest {
    protected QueueExchangeTest() {
        super(new QueueExchangeFactory(), "QueueExchangeTest:");
    }
}