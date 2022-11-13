package ru.rsreu.labs.sync;

import ru.rsreu.labs.ExchangeTest;

class SyncExchangeTest extends ExchangeTest {
    public SyncExchangeTest() {
        super(new SyncExchangeCreator());
    }
}