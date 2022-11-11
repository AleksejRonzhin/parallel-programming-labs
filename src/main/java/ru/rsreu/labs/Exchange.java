package ru.rsreu.labs;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;
import java.util.Map;

public interface Exchange {
    Client createClient();

    void putMoney(Client client, Currency currency, BigDecimal value);

    void takeMoney(Client client, Currency currency, BigDecimal value);

    void createOrder(Order order);

    void getOpenOrders();

    Map<Currency, BigDecimal> getClientMoney(Client client);
}