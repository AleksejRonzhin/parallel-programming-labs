package ru.rsreu.labs;

import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Money;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;
import java.util.List;

public interface Exchange {
    Client createClient();

    void pushMoney(Client client, Currency currency, BigDecimal value);

    default void pushMoney(Client client, Currency currency, double value) {
        pushMoney(client, currency, BigDecimal.valueOf(value));
    }

    void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException;

    default void takeMoney(Client client, Currency currency, double value) throws NotEnoughMoneyException {
        takeMoney(client, currency, BigDecimal.valueOf(value));
    }

    void createOrder(Order order) throws NotEnoughMoneyException;

    List<Order> getOpenOrders();

    Money getClientMoney(Client client);

    Money getExchangeAndClientsMoney();
}