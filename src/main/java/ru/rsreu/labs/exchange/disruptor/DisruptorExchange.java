package ru.rsreu.labs.exchange.disruptor;

import ru.rsreu.labs.exchange.AbstractExchange;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DisruptorExchange extends AbstractExchange {

    protected DisruptorExchange(boolean withCommission) {
        super(withCommission);
    }

    @Override
    public Map<Currency, BigDecimal> getBank() {
        return null;
    }

    @Override
    protected void incrementCoverCount() {

    }

    @Override
    public ResponseStatus createOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> getOpenOrders() {
        return null;
    }

    @Override
    public long getCoverCount() {
        return 0;
    }
}
