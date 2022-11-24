package ru.rsreu.labs.sync;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.ExchangeTest;

@Tag("exchange")
class SyncExchangeTest extends ExchangeTest {
    public SyncExchangeTest() {
        super(new SyncExchangeCreator());
    }
}