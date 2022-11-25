package ru.rsreu.labs.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.ExchangeTest;
import ru.rsreu.labs.exchange.queue.multi_thread_handle.MultiThreadQueueExchangeCreator;

@Tag("exchange")
public class MultiThreadQueueExchangeTest extends ExchangeTest {
    protected MultiThreadQueueExchangeTest() {
        super(new MultiThreadQueueExchangeCreator());
    }
}
