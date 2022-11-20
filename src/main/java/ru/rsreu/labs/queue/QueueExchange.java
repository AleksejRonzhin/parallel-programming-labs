package ru.rsreu.labs.queue;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.exceptions.ClientNotFoundException;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.*;

import java.math.BigDecimal;
import java.util.List;

public class QueueExchange implements Exchange {
    @Override
    public Client createClient() {
        return null;
    }

    @Override
    public void pushMoney(Client client, Currency currency, BigDecimal value) throws ClientNotFoundException {

    }

    @Override
    public void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException, ClientNotFoundException {

    }

    @Override
    public ResponseStatus createOrder(Order order) {
        return null;
    }

    @Override
    public List<Order> getOpenOrders() {
        return null;
    }

    @Override
    public Balance getClientBalance(Client client) throws ClientNotFoundException {
        return null;
    }

    @Override
    public Balance getGeneralBalance() {
        return null;
    }

    @Override
    public long getCoverCount() {
        return 0;
    }
}
