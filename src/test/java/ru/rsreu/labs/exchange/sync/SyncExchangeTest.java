package ru.rsreu.labs.exchange.sync;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.SyncExchangeCreator;

@Tag("exchange")
class SyncExchangeTest extends ExchangeTest {
    public SyncExchangeTest() {
        super(new SyncExchangeCreator(), "SYNC");
    }
}