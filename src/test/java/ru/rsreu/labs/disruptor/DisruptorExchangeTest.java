package ru.rsreu.labs.disruptor;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.ExchangeTest;
import ru.rsreu.labs.exchange.disruptor.DisruptorExchangeCreator;

@Tag("exchange")
public class DisruptorExchangeTest extends ExchangeTest {
    protected DisruptorExchangeTest() {
        super(new DisruptorExchangeCreator(), "DISRUPTOR");
    }
}
