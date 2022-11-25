package ru.rsreu.labs.exchange.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.MultiThreadQueueExchangeCreator;

@Tag("exchange")
public class MultiThreadQueueExchangeTest extends ExchangeTest {
    protected MultiThreadQueueExchangeTest() {
        super(new MultiThreadQueueExchangeCreator(), "MULTI THREAD QUEUE");
    }
}
