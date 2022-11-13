package ru.rsreu.labs;

import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Balance;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;
import java.util.List;

public interface Exchange {
    Client createClient();

    void pushMoney(Client client, Currency currency, BigDecimal value);

    default void pushMoney(Client client, Currency currency, int value) {
        pushMoney(client, currency, BigDecimal.valueOf(value));
    }

    default void pushMoney(Client client, Currency currency, String value) {
        pushMoney(client, currency, new BigDecimal(value));
    }


    void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException;

    default void takeMoney(Client client, Currency currency, int value) throws NotEnoughMoneyException{
        takeMoney(client, currency, new BigDecimal(value));
    }

    void createOrder(Order order) throws NotEnoughMoneyException;

    List<Order> getOpenOrders();

    Balance getClientBalance(Client client);

    Balance getGeneralBalance();

    long getCoverCount();
}