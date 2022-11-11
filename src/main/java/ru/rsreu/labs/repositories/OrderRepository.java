package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.OrderInfo;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class OrderRepository {
    private final Map<CurrencyPair, Map<Currency, List<OrderInfo>>> orders;

    public OrderRepository() {
        this.orders = new HashMap<>();
        Currency[] currencies = Currency.values();
        for (int i = 0; i < currencies.length; i++) {
            for (int j = i + 1; j < currencies.length; j++) {
                Currency sourceCurrency = currencies[i];
                Currency targetCurrency = currencies[j];
                orders.put(new CurrencyPair(sourceCurrency, targetCurrency), createCurrentPairMap(sourceCurrency, targetCurrency));
            }
        }
    }

    private Map<Currency, List<OrderInfo>> createCurrentPairMap(Currency sourceCurrency, Currency targetCurrency) {
        Map<Currency, List<OrderInfo>> map = new HashMap<>();
        map.put(sourceCurrency, new LinkedList<>());
        map.put(targetCurrency, new LinkedList<>());
        return map;
    }

    public List<OrderInfo> getOrderInfosByCurrencyPair(CurrencyPair currencyPair, Currency targetCurrency) {
        return orders.get(currencyPair).get(targetCurrency);
    }
//
//    public void addOrder(Order order) {
//        Collection<OrderInfo> orderInfoList = getOrderInfosByCurrencyPair(order.getCurrencyPair(), order.getTargetCurrency());
//        orderInfoList.add(order.getOrderInfo());
//    }
//
//    public void removeOrder(CurrencyPair currencyPair, Currency targetCurrency, OrderInfo orderInfo) {
//        Collection<OrderInfo> orderInfoList = getOrderInfosByCurrencyPair(currencyPair, targetCurrency);
//        orderInfoList.remove(orderInfo);
//    }
}