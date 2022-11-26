package ru.rsreu.labs.exchange.buffer;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.CombinedExchangeFactory;


@Tag("exchange")
class CombinedExchangeTest extends ExchangeTest {
    protected CombinedExchangeTest() {
        super(new CombinedExchangeFactory(), "CombinedExchangeTest:");
    }
}