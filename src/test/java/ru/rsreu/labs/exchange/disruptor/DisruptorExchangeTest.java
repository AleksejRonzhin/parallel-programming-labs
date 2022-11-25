package ru.rsreu.labs.exchange.disruptor;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.DisruptorExchangeCreator;

@Tag("exchange")
public class DisruptorExchangeTest extends ExchangeTest {
    protected DisruptorExchangeTest() {
        super(new DisruptorExchangeCreator(), "DISRUPTOR");
    }
}
