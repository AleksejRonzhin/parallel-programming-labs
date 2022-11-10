package ru.rsreu.labs;

public interface Exchange {
    void createClient();

    void inputMoney();

    void outputMoney();

    void createOrder();

    void getOpenOrders();

    void getClientInfo();
}