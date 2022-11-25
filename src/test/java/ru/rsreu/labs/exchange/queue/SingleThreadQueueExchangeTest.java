package ru.rsreu.labs.exchange.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.SingleThreadQueueExchangeCreator;

@Tag("exchange")
class SingleThreadQueueExchangeTest extends ExchangeTest {
    protected SingleThreadQueueExchangeTest() {
        super(new SingleThreadQueueExchangeCreator(), "SINGLE THREAD QUEUE");
    }
}