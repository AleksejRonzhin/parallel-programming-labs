package ru.rsreu.labs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Money;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class ExchangeTest {
    private final Exchange exchange;
    private final Client firstClient;
    private final Client secondClient;

    protected ExchangeTest(Exchange exchange) {
        this.exchange = exchange;
        this.firstClient = exchange.createClient();
        this.secondClient = exchange.createClient();
    }

    @Test
    public void pushMoneyTest() {
        Client client = exchange.createClient();
        Currency pushedCurrency = Currency.USD;
        BigDecimal pushedValue = BigDecimal.valueOf(10);

        Money oldClientMoney = exchange.getClientMoney(client);
        BigDecimal beforeValue = oldClientMoney.get(pushedCurrency);
        BigDecimal expectedValue = beforeValue.add(pushedValue);

        exchange.pushMoney(client, pushedCurrency, pushedValue);
        Money newClientMoney = exchange.getClientMoney(client);
        BigDecimal actualValue = newClientMoney.get(pushedCurrency);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void takeMoneyTest() throws NotEnoughMoneyException {
        Client client = exchange.createClient();
        Currency takenCurrency = Currency.RUB;
        BigDecimal takenValue = BigDecimal.valueOf(100);
        exchange.pushMoney(client, Currency.RUB, 1000);

        Money oldClientMoney = exchange.getClientMoney(client);
        BigDecimal beforeValue = oldClientMoney.get(takenCurrency);
        BigDecimal expectedValue = beforeValue.subtract(takenValue);

        exchange.takeMoney(client, takenCurrency, takenValue);
        Money newClientMoney = exchange.getClientMoney(client);
        BigDecimal actualValue = newClientMoney.get(takenCurrency);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void takeTooManyMoneyTest() {
        Client client = exchange.createClient();
        Currency takenCurrency = Currency.RUB;

        Money beforeClientMoney = exchange.getClientMoney(client);
        BigDecimal beforeValue = beforeClientMoney.get(takenCurrency);
        BigDecimal takenValue = beforeValue.add(BigDecimal.ONE);

        Assertions.assertThrows(NotEnoughMoneyException.class, () ->
                exchange.takeMoney(firstClient, takenCurrency, takenValue));
    }

    @Test
    public void addOrderTest() throws NotEnoughMoneyException {
        exchange.pushMoney(firstClient, Currency.USD, 10);
        Order order = new Order(Currency.USD, Currency.RUB, 130, 1.0 / 65, firstClient);
        exchange.createOrder(order);

        List<Order> orders = exchange.getOpenOrders();
        Assertions.assertTrue(orders.contains(order));
    }

    @Test
    public void coveringOrdersTest() throws NotEnoughMoneyException {
        exchange.pushMoney(firstClient, Currency.RUB, 1000);
        exchange.pushMoney(secondClient, Currency.USD, 100);

        BigDecimal oldFirstClientTargetValue = exchange.getClientMoney(firstClient)
                .get(Currency.USD);
        BigDecimal oldSecondClientTargetValue = exchange.getClientMoney(secondClient)
                .get(Currency.RUB);

        Order secondOrder = new Order(Currency.USD, Currency.RUB, 130, 1.0 / 65, secondClient);
        Order firstOrder = new Order(Currency.RUB, Currency.USD, 1, 70, firstClient);
        exchange.createOrder(firstOrder);
        exchange.createOrder(secondOrder);

        BigDecimal newFirstClientTargetValue = exchange.getClientMoney(firstClient)
                .get(Currency.USD);
        BigDecimal newSecondClientTargetValue = exchange.getClientMoney(secondClient)
                .get(Currency.RUB);

        Assertions.assertEquals(0, oldFirstClientTargetValue.add(BigDecimal.valueOf(1)).setScale(3, RoundingMode.DOWN).compareTo(newFirstClientTargetValue.setScale(3, RoundingMode.DOWN)));
        Assertions.assertEquals(0, oldSecondClientTargetValue.add(BigDecimal.valueOf(70)).compareTo(newSecondClientTargetValue));
    }
}