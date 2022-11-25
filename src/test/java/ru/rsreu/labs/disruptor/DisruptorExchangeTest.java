package ru.rsreu.labs.disruptor;

import ru.rsreu.labs.ExchangeTest;
import ru.rsreu.labs.exchange.disruptor.DisruptorExchangeCreator;

public class DisruptorExchangeTest extends ExchangeTest {
    protected DisruptorExchangeTest() {
        super(new DisruptorExchangeCreator());
    }
}
