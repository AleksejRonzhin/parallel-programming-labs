package ru.rsreu.labs.exchange.buffer.disruptor;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;

@Tag("exchange")
class TwoLevelDisruptorExchangeTest extends ExchangeTest {

    protected TwoLevelDisruptorExchangeTest() {
        super(new TwoLevelDisruptorExchangeFactory(), "TwoLevelDisruptorExchangeTest:");
    }
}