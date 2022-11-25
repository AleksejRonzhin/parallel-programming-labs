package ru.rsreu.labs.exchange;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.creators.CombinedExchangeCreator;


@Tag("exchange")
class CombinedExchangeTest extends ExchangeTest {

    protected CombinedExchangeTest() {
        super(new CombinedExchangeCreator(), "COMBINED");
    }
}