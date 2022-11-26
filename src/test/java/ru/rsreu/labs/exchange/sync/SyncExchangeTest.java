package ru.rsreu.labs.exchange.sync;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.exchange.ExchangeTest;
import ru.rsreu.labs.exchange.creators.SyncExchangeFactory;

@Tag("exchange")
class SyncExchangeTest extends ExchangeTest {
    public SyncExchangeTest() {
        super(new SyncExchangeFactory(), "SyncExchangeTest:");
    }
}