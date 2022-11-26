package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.Exchange;
import ru.rsreu.labs.exchange.buffer.queue.TwoLevelQueueExchangeTwoLevel;

public class TwoLevelQueueExchangeFactory implements ExchangeFactory {
    @Override
    public Exchange create(boolean withCommission) {
        return new TwoLevelQueueExchangeTwoLevel(withCommission);
    }
}
