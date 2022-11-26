package ru.rsreu.labs.exchange.buffer.disruptor;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.TwoLevelDisruptorExchangeFactory;

@Order(5)
@Tag("exchange")
class TwoLevelDisruptorExchangeTest extends ExchangeTest {

    protected TwoLevelDisruptorExchangeTest() {
        super(new TwoLevelDisruptorExchangeFactory(), "TwoLevelDisruptorExchangeTest:");
    }
}