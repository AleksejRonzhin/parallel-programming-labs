package ru.rsreu.labs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Balance;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static ru.rsreu.labs.models.Currency.*;

public class ExchangeTest {
    private final ExchangeCreator exchangeCreator;

    protected ExchangeTest(ExchangeCreator exchangeCreator) {
        this.exchangeCreator = exchangeCreator;
    }

    @Test
    public void pushMoneyTest() {
        Exchange exchange = exchangeCreator.create();

        Client client = exchange.createClient();
        Currency pushedCurrency = USD;
        BigDecimal pushedValue = BigDecimal.valueOf(10);

        Balance oldClientBalance = exchange.getClientBalance(client);
        BigDecimal beforeValue = oldClientBalance.get(pushedCurrency);
        BigDecimal expectedValue = beforeValue.add(pushedValue);

        exchange.pushMoney(client, pushedCurrency, pushedValue);
        Balance newClientBalance = exchange.getClientBalance(client);
        BigDecimal actualValue = newClientBalance.get(pushedCurrency);

        Assertions.assertTrue(BigDecimalUtils.equals(expectedValue, actualValue));
    }

    @Test
    public void takeMoneyTest() throws NotEnoughMoneyException {
        Exchange exchange = exchangeCreator.create();

        Client client = exchange.createClient();
        Currency takenCurrency = RUB;
        BigDecimal takenValue = BigDecimal.valueOf(100);
        exchange.pushMoney(client, RUB, 1000);

        Balance oldClientBalance = exchange.getClientBalance(client);
        BigDecimal beforeValue = oldClientBalance.get(takenCurrency);
        BigDecimal expectedValue = beforeValue.subtract(takenValue);

        exchange.takeMoney(client, takenCurrency, takenValue);
        Balance newClientBalance = exchange.getClientBalance(client);
        BigDecimal actualValue = newClientBalance.get(takenCurrency);

        Assertions.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void takeTooManyMoneyTest() {
        Exchange exchange = exchangeCreator.create();

        Client client = exchange.createClient();
        Currency takenCurrency = RUB;

        Balance beforeClientBalance = exchange.getClientBalance(client);
        BigDecimal beforeValue = beforeClientBalance.get(takenCurrency);
        BigDecimal takenValue = beforeValue.add(BigDecimal.ONE);

        Assertions.assertThrows(NotEnoughMoneyException.class, () ->
                exchange.takeMoney(client, takenCurrency, takenValue));
    }

    @Test
    public void addOrderTest() throws NotEnoughMoneyException {
        Exchange exchange = exchangeCreator.create();
        Client client = exchange.createClient();

        exchange.pushMoney(client, USD, 10);
        Order order = Order.builder(client).buy(130, RUB).selling(2, USD);
        exchange.createOrder(order);

        List<Order> orders = exchange.getOpenOrders();
        Assertions.assertTrue(orders.contains(order));
    }

    @Test
    public void coveringOrdersTest() throws NotEnoughMoneyException {
        Exchange exchange = exchangeCreator.create();
        Client firstClient = exchange.createClient();
        Client secondClient = exchange.createClient();
        exchange.pushMoney(firstClient, RUB, 1000);
        exchange.pushMoney(secondClient, USD, 100);

        BigDecimal rubToUsdRate = BigDecimalUtils.getRate(BigDecimal.valueOf(1), BigDecimal.valueOf(65));
        Order secondOrder = Order.builder(secondClient).buy(130, RUB).at(rubToUsdRate, USD);
        Order firstOrder = Order.builder(firstClient).buy(1, USD).selling(65, RUB);
        exchange.createOrder(firstOrder);
        exchange.createOrder(secondOrder);

        BigDecimal newFirstClientTargetValue = exchange.getClientBalance(firstClient).get(USD);
        BigDecimal newSecondClientTargetValue = exchange.getClientBalance(secondClient).get(RUB);

        Assertions.assertTrue(BigDecimalUtils.equals(BigDecimal.valueOf(1), newFirstClientTargetValue));
        Assertions.assertTrue(BigDecimalUtils.equals(BigDecimal.valueOf(65), newSecondClientTargetValue));
    }

    @Test
    public void stressTest() throws InterruptedException, ExecutionException {
        Exchange exchange = exchangeCreator.create();
        int clientCount = 50;
        int clientOrderCount = 10000;

        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);
        Collection<Client> clients = initExchange(exchange, clientCount, executorService);

        Balance startGeneralBalance = exchange.getGeneralBalance();
        awaitAddingOrdersTasks(exchange, executorService, clients, clientOrderCount);
        Balance endGeneralBalance = exchange.getGeneralBalance();
        Assertions.assertEquals(startGeneralBalance, endGeneralBalance);

        long expectedOrderCount = clientCount * clientOrderCount;
        long openOrderCount = exchange.getOpenOrders().size();
        long coverCount = exchange.getCoverCount();
        long actualOrderCount = openOrderCount + coverCount * 2;
        Assertions.assertEquals(expectedOrderCount, actualOrderCount);
    }

    public Collection<Client> initExchange(Exchange exchange, int clientCount, ExecutorService service) throws InterruptedException, ExecutionException {
        Collection<Callable<Client>> tasks = new ArrayList<>(clientCount);
        Callable<Client> clientInit = () -> {
            Client client = exchange.createClient();
            exchange.pushMoney(client, USD, 100000);
            exchange.pushMoney(client, RUB, 100000);
            exchange.pushMoney(client, KZT, 100000);
            return client;
        };
        for(int i = 0; i < clientCount; i++){
            tasks.add(clientInit);
        }

        List<Future<Client>> futures = service.invokeAll(tasks);
        return waitFutures(futures);
    }

    public void awaitAddingOrdersTasks(Exchange exchange, ExecutorService executorService, Collection<Client> clients, int clientOrderCount) throws ExecutionException, InterruptedException {
        Collection<Future<Void>> futures = new ArrayList<>();
        clients.forEach(client -> futures.add(executorService.submit(getTarget(exchange, client, clientOrderCount))));
        waitFutures(futures);
    }

    private <T> Collection<T> waitFutures(Collection<Future<T>> futures) throws ExecutionException, InterruptedException {
        Collection<T> result = new ArrayList<>();
        for(Future<T> future: futures){
            result.add(future.get());
        }
        return result;
    }

    private Callable<Void> getTarget(Exchange exchange, Client client, int orderCount) {
        return () -> {
            try {
                for (int i = 0; i < orderCount; i++) {
                    exchange.pushMoney(client, USD, 1);
                    Order order = OrderGenerator.generate(client);
                    exchange.createOrder(order);
                    exchange.takeMoney(client, USD, 1);
                }
            } catch (NotEnoughMoneyException ignored) {

            }
            return null;
        };
    }
}