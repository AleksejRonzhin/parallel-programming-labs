package ru.rsreu.labs.sync;

import ru.rsreu.labs.AbstractExchange;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ThreadSafe
public class SyncExchange extends AbstractExchange {
    private final AtomicLong coverCount = new AtomicLong(0);
    private final ConcurrentHashMap<Currency, BigDecimal> bank = new ConcurrentHashMap<>();

    public SyncExchange(boolean withCommission) {
        super(withCommission);
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return bank;
    }

    @Override
    public void incrementCoverCount() {
        coverCount.incrementAndGet();
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        return safeCreateOrder(order, (pair, target) -> {
            synchronized (pair) {
                return target.get();
            }
        });
    }

    @Override
    public List<Order> getOpenOrders() {
        return safeGetOpenOrders((pair, target) -> {
            synchronized (pair) {
                target.run();
            }
        });
    }

    @Override
    public long getCoverCount() {
        return coverCount.get();
    }
}