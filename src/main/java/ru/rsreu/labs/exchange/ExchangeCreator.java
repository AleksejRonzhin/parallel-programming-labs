package ru.rsreu.labs.exchange;

public interface ExchangeCreator {
    Exchange create(boolean withCommission);
}