package ru.rsreu.labs.exchange.creators;

import ru.rsreu.labs.exchange.Exchange;

public interface ExchangeFactory {
    Exchange create(boolean withCommission);
}