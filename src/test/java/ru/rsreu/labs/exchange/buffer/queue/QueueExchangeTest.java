package ru.rsreu.labs.exchange.buffer.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;

@Tag("exchange")
class QueueExchangeTest extends ExchangeTest {
    protected QueueExchangeTest() {
        super(new QueueExchangeFactory(), "QueueExchangeTest:");
    }
}