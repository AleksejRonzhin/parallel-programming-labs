package ru.rsreu.labs.exchange.buffer.disruptor;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.DisruptorExchangeFactory;


@Tag("exchange")
@Order(3)
public class DisruptorExchangeTest extends ExchangeTest {
    protected DisruptorExchangeTest() {
        super(new DisruptorExchangeFactory(), "DisruptorExchangeTest:");
    }
}
