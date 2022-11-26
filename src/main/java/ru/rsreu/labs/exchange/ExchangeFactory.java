package ru.rsreu.labs.exchange;

public interface ExchangeFactory {
    Exchange create(boolean withCommission);
}