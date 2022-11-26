package ru.rsreu.labs.exchange.buffer.queue;

import ru.rsreu.labs.exchange.buffer.TwoLevelBufferExchange;

public class TwoLevelQueueExchangeTwoLevel extends TwoLevelBufferExchange {

    public TwoLevelQueueExchangeTwoLevel(boolean withCommission) {
        super(withCommission, new QueueBuffer(), new QueueBufferCreator());
    }
}
