package ru.rsreu.labs.queue;

import org.junit.jupiter.api.Tag;
import ru.rsreu.labs.ExchangeTest;

@Tag("exchange")
class QueueExchangeTest extends ExchangeTest {

    protected QueueExchangeTest() {
        super(new QueueExchangeCreator());
    }
}