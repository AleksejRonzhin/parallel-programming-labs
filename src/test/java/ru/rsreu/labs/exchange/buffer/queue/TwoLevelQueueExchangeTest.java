package ru.rsreu.labs.exchange.buffer.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;

@Tag("exchange")
public class TwoLevelQueueExchangeTest extends ExchangeTest {
    protected TwoLevelQueueExchangeTest() {
        super(new TwoLevelQueueExchangeFactory(), "TwoLevelQueueExchangeTest:");
    }
}
