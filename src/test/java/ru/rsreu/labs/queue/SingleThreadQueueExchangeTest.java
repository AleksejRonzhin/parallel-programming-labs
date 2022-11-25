package ru.rsreu.labs.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.ExchangeTest;
import ru.rsreu.labs.queue.single_thread_handle.SingleThreadQueueExchangeCreator;

@Tag("exchange")
class SingleThreadQueueExchangeTest extends ExchangeTest {
    protected SingleThreadQueueExchangeTest() {
        super(new SingleThreadQueueExchangeCreator());
    }
}