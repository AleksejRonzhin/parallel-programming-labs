package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.CurrencyPair;
import ru.rsreu.labs.models.OrderInfo;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;

@NotThreadSafe
public class OrderRepository {
    private final Map<CurrencyPair, OrderListPair> orders;

    public OrderRepository() {
        this.orders = new HashMap<>();
        System.out.println(CurrencyPair.getPairs());
        for(CurrencyPair pair: CurrencyPair.getPairs()){
            orders.put(pair, new OrderListPair());
        }
    }

    public Map<CurrencyPair, OrderListPair> getOrders() {
        return orders;
    }

    public OrderListPair getOrdersByCurrencyPair(CurrencyPair currencyPair) {
        return orders.get(currencyPair);
    }

    public static class OrderListPair{
        private final List<OrderInfo> sellOrders = new LinkedList<>();
        private final List<OrderInfo> buyOrders = new LinkedList<>();

        public List<OrderInfo> getSellOrders() {
            return sellOrders;
        }

        public List<OrderInfo> getBuyOrders() {
            return buyOrders;
        }
    }
}