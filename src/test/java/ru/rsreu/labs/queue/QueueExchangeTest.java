package ru.rsreu.labs.queue;

import ru.rsreu.labs.ExchangeTest;

class QueueExchangeTest extends ExchangeTest {

    protected QueueExchangeTest() {
        super(new QueueExchangeCreator());
    }
}