package ru.rsreu.labs.exchange.buffer.queue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.TwoLevelQueueExchangeFactory;

@Order(4)
@Tag("exchange")
public class TwoLevelQueueExchangeTest extends ExchangeTest {
    protected TwoLevelQueueExchangeTest() {
        super(new TwoLevelQueueExchangeFactory(), "TwoLevelQueueExchangeTest:");
    }
}
