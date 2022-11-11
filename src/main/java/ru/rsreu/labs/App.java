package ru.rsreu.labs;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.OrderInfo;
import ru.rsreu.labs.sync.SyncExchange;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        Exchange exchange = new SyncExchange();

        Client client = exchange.createClient();
        exchange.putMoney(client, Currency.RUB, BigDecimal.valueOf(1000));

        Client client1 = exchange.createClient();
        exchange.putMoney(client1, Currency.USD, BigDecimal.valueOf(10));

        Order order = new Order(Currency.USD,
                Currency.RUB, new OrderInfo(BigDecimal.valueOf(80),
                BigDecimal.valueOf(3.0/80), client1));

        exchange.createOrder(order);

        Order order2 = new Order(Currency.RUB, Currency.USD, new OrderInfo(BigDecimal.valueOf(2), BigDecimal.valueOf(1), client));

        exchange.createOrder(order2);
        System.out.println();
    }
}
