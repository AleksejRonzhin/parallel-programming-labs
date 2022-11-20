package ru.rsreu.labs;

import ru.rsreu.labs.exceptions.ClientNotFoundException;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.*;

import java.math.BigDecimal;
import java.util.List;

public interface Exchange {
    Client createClient();

    void pushMoney(Client client, Currency currency, BigDecimal value) throws ClientNotFoundException;

    default void pushMoney(Client client, Currency currency, int value) throws ClientNotFoundException {
        pushMoney(client, currency, BigDecimal.valueOf(value));
    }

    default void pushMoney(Client client, Currency currency, String value) throws ClientNotFoundException {
        pushMoney(client, currency, new BigDecimal(value));
    }


    void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException, ClientNotFoundException;

    default void takeMoney(Client client, Currency currency, int value) throws NotEnoughMoneyException, ClientNotFoundException {
        takeMoney(client, currency, new BigDecimal(value));
    }

    ResponseStatus createOrder(Order order);

    List<Order> getOpenOrders();

    Balance getClientBalance(Client client) throws ClientNotFoundException;

    Balance getGeneralBalance();

    long getCoverCount();
}