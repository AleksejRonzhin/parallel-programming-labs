package ru.rsreu.labs.repositories;

import ru.rsreu.labs.exceptions.ClientNotFoundException;
import ru.rsreu.labs.models.Balance;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;

import java.math.BigDecimal;

public interface ClientBalanceRepository {
    void addClient(Client client);

    Balance getClientBalance(Client client) throws ClientNotFoundException;

    void pushMoney(Client client, Currency currency, BigDecimal value) throws ClientNotFoundException;

    boolean tryTakeMoney(Client client, Currency currency, BigDecimal value) throws ClientNotFoundException;

    Balance getGeneralClientsBalance();
}
