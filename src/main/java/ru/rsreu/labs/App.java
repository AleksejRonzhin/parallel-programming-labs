package ru.rsreu.labs;

import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.*;
import ru.rsreu.labs.sync.SyncExchange;

import java.math.BigDecimal;

public class App {

    public static void main(String[] args) {
        Exchange exchange = new SyncExchange();
        System.out.println(exchange.getExchangeAndClientsMoney());

        Client client = exchange.createClient();
        exchange.pushMoney(client, Currency.RUB, BigDecimal.valueOf(1000));

        Client client1 = exchange.createClient();
        exchange.pushMoney(client1, Currency.USD, BigDecimal.valueOf(100));
        System.out.println(exchange.getExchangeAndClientsMoney());

        Money clientMoney = exchange.getClientMoney(client);
        Money client1Money = exchange.getClientMoney(client1);
        System.out.println("client 1 " + clientMoney);
        System.out.println("client 2 " + client1Money);

        Order order = new Order(Currency.USD, Currency.RUB, BigDecimal.valueOf(130),
                BigDecimal.valueOf(1.0 / 65), client1);

        try {
            exchange.createOrder(order);
        } catch (NotEnoughMoneyException e) {
            System.out.println("NOT ENOUGH MONEY");
        }
        clientMoney = exchange.getClientMoney(client);
        client1Money = exchange.getClientMoney(client1);
        System.out.println("client 1 " + clientMoney);
        System.out.println("client 2 " + client1Money);

        Order order2 = new Order(Currency.RUB, Currency.USD, BigDecimal.valueOf(2), BigDecimal.valueOf(66), client);

        try {
            exchange.createOrder(order2);
        } catch (NotEnoughMoneyException e) {
            System.out.println("NOT ENOUGH MONEY");
        }

        clientMoney = exchange.getClientMoney(client);
        client1Money = exchange.getClientMoney(client1);
        System.out.println("client 1 " + clientMoney);
        System.out.println("client 2 " + client1Money);
        System.out.println(exchange.getOpenOrders());
        System.out.println(exchange.getExchangeAndClientsMoney());
    }
}
