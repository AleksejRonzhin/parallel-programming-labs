package ru.rsreu.labs.exchange.sync;

import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.exchange.SafeHelper;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

@ThreadSafe
public class SyncExchange extends AbstractExchange {

    public SyncExchange(boolean withCommission) {
        super(withCommission, new SafeHelper());
    }

    @Override
    public ResponseStatus createOrder(Order order) {
        return safeCreateOrder(order, (orderListPair, target) -> {
            synchronized (orderListPair) {
                return target.get();
            }
        });
    }

    @Override
    public List<Order> getOpenOrders() {
        return safeGetOpenOrders((currencyPair, target) -> {
            synchronized (currencyPair) {
                target.run();
            }
        });
    }
}