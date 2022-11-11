package ru.rsreu.labs;

import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface Exchange {
    Client createClient();

    void putMoney(Client client, Currency currency, BigDecimal value);

    void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException;

    void createOrder(Order order) throws NotEnoughMoneyException;

    List<Order> getOpenOrders();

    Map<Currency, BigDecimal> getClientMoney(Client client);

    Map<Currency, BigDecimal> getExchangeAndClientsMoney();
}