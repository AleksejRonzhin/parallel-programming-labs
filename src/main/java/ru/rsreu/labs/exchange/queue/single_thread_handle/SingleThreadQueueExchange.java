package ru.rsreu.labs.exchange.queue.single_thread_handle;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.exchange.queue.QueueExchange;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@NotThreadSafe
public class SingleThreadQueueExchange extends QueueExchange {

    public SingleThreadQueueExchange(boolean withCommission) {
        super(withCommission);
    }

    private final Map<Currency, BigDecimal> bank = new HashMap<>();
    private long coverCount = 0;

    @Override
    public long getCoverCount() {
        return coverCount;
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    protected void incrementCoverCount() {
        coverCount++;
    }
}
