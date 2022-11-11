package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.OrderInfo;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@NotThreadSafe
public class OrderRepository {
    private final Map<CurrencyPair, List<OrderInfo>> orders;

    public OrderRepository() {
        this.orders = new HashMap<>();
        for (Currency sourceCurrency : Currency.values()) {
            for (Currency targetCurrency : Currency.values()) {
                orders.put(new CurrencyPair(sourceCurrency, targetCurrency), new LinkedList<>());
            }
        }
    }

    public Map<CurrencyPair, List<OrderInfo>> getOrders() {
        return orders;
    }

    public List<OrderInfo> getOrdersByCurrencyPair(CurrencyPair currencyPair) {
        return orders.get(currencyPair);
    }
}