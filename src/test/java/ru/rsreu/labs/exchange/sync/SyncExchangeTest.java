package ru.rsreu.labs.exchange.sync;

import org.junit.jupiter.api.*;
import ru.rsreu.labs.exchange.ExchangeTest;

@Tag("exchange")
class SyncExchangeTest extends ExchangeTest {
    public SyncExchangeTest() {
        super(new SyncExchangeFactory(), "SyncExchangeTest:");
    }
}