package ru.rsreu.labs.exchange.buffer.disruptor;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;


@Tag("exchange")
public class DisruptorExchangeTest extends ExchangeTest {
    protected DisruptorExchangeTest() {
        super(new DisruptorExchangeFactory(), "DisruptorExchangeTest:");
    }
}
