package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Balance;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;

import java.math.BigDecimal;

public interface ClientBalanceRepository {
    void addClient(Client client);

    Balance getClientBalance(Client client);

    void pushMoney(Client client, Currency currency, BigDecimal value);

    boolean tryTakeMoney(Client client, Currency currency, BigDecimal value);

    Balance getGeneralClientsBalance();
}
