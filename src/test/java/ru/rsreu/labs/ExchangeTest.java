package ru.rsreu.labs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Money;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangeTest {
    private final ExchangeCreator exchangeCreator;

    protected ExchangeTest(ExchangeCreator exchangeCreator) {
        this.exchangeCreator = exchangeCreator;
    }

    @Test
    public void pushMoneyTest() {
        Exchange exchange = exchangeCreator.create();

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
        Exchange exchange = exchangeCreator.create();

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
        Exchange exchange = exchangeCreator.create();

        Client client = exchange.createClient();
        Currency takenCurrency = Currency.RUB;

        Money beforeClientMoney = exchange.getClientMoney(client);
        BigDecimal beforeValue = beforeClientMoney.get(takenCurrency);
        BigDecimal takenValue = beforeValue.add(BigDecimal.ONE);

        Assertions.assertThrows(NotEnoughMoneyException.class, () ->
                exchange.takeMoney(client, takenCurrency, takenValue));
    }

    @Test
    public void addOrderTest() throws NotEnoughMoneyException {
        Exchange exchange = exchangeCreator.create();
        Client client = exchange.createClient();

        exchange.pushMoney(client, Currency.USD, 10);
        Order order = new Order(Currency.USD, Currency.RUB, new BigDecimal(130), BigDecimal.valueOf(1.0 / 65), client);
        exchange.createOrder(order);

        List<Order> orders = exchange.getOpenOrders();
        Assertions.assertTrue(orders.contains(order));
    }

    @Test
    public void coveringOrdersTest() throws NotEnoughMoneyException {
        Exchange exchange = exchangeCreator.create();
        Client firstClient = exchange.createClient();
        Client secondClient = exchange.createClient();
        exchange.pushMoney(firstClient, Currency.RUB, 1000);
        exchange.pushMoney(secondClient, Currency.USD, 100);

        Order secondOrder = new Order(Currency.USD, Currency.RUB, BigDecimal.valueOf(130), BigDecimalUtils.getRate(BigDecimal.valueOf(1), BigDecimal.valueOf(65)), secondClient);
        Order firstOrder = new Order(Currency.RUB, Currency.USD, BigDecimal.valueOf(1), BigDecimal.valueOf(70), firstClient);
        exchange.createOrder(firstOrder);
        exchange.createOrder(secondOrder);

        BigDecimal newFirstClientTargetValue = exchange.getClientMoney(firstClient)
                .get(Currency.USD);
        BigDecimal newSecondClientTargetValue = exchange.getClientMoney(secondClient)
                .get(Currency.RUB);

        Assertions.assertTrue(BigDecimalUtils.compare(BigDecimal.valueOf(1), newFirstClientTargetValue));
        Assertions.assertTrue(BigDecimalUtils.compare(BigDecimal.valueOf(70), newSecondClientTargetValue));
    }

    @Test
    public void stressTest() throws InterruptedException {
        Exchange exchange = exchangeCreator.create();
        int clientCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);
        Runnable target = getTarget(exchange);
        for(int i = 0; i < 10; i++){
            executorService.submit(target);
        }
        executorService.shutdownNow();
        executorService.awaitTermination(300, TimeUnit.SECONDS);

        System.out.println(exchange.getOpenOrders().size());

        System.out.println(exchange.getExchangeAndClientsMoney());
    }

    private Runnable getTarget(Exchange exchange){
        return () -> {
            Client client = exchange.createClient();
            exchange.pushMoney(client, Currency.USD, 100000);
            exchange.pushMoney(client, Currency.RUB, 100000);
            Order order = new Order(Currency.RUB, Currency.USD, BigDecimal.valueOf(1), BigDecimal.valueOf(70), client);
            try {
                for(int i = 0; i < 1000; i++){
                    exchange.createOrder(order);

                }
            } catch (NotEnoughMoneyException e) {
                throw new RuntimeException(e);
            }
        };
    }
}