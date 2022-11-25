package ru.rsreu.labs.exchange.disruptor;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.MultiDisruptorExchangeCreator;

@Tag("exchange")
class MultiDisruptorExchangeTest extends ExchangeTest {

    protected MultiDisruptorExchangeTest() {
        super(new MultiDisruptorExchangeCreator(), "MULTI DISRUPTOR");
    }
}