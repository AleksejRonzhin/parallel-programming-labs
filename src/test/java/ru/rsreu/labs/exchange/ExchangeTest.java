package ru.rsreu.labs.exchange;

import org.junit.jupiter.api.*;
import ru.rsreu.labs.OrderGenerator;
import ru.rsreu.labs.exceptions.ClientNotFoundException;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.exchange.creators.ExchangeFactory;
import ru.rsreu.labs.models.*;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import static ru.rsreu.labs.models.Currency.RUB;
import static ru.rsreu.labs.models.Currency.USD;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExchangeTest {
    private final ExchangeFactory exchangeFactory;
    private final AtomicLong successOrderCreatingCount = new AtomicLong(0L);
    private final String name;

    protected ExchangeTest(ExchangeFactory exchangeFactory, String name) {
        this.exchangeFactory = exchangeFactory;
        this.name = name;
    }

    @BeforeAll
    public void printName(){
        System.out.println(name);
    }

    @Test
    public void pushMoneyTest() throws ClientNotFoundException {
        Exchange exchange = exchangeFactory.create(false);

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
    public void takeMoneyTest() throws NotEnoughMoneyException, ClientNotFoundException {
        Exchange exchange = exchangeFactory.create(false);

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
    public void takeTooManyMoneyTest() throws ClientNotFoundException {
        Exchange exchange = exchangeFactory.create(false);

        Client client = exchange.createClient();
        Currency takenCurrency = RUB;

        Balance beforeClientBalance = exchange.getClientBalance(client);
        BigDecimal beforeValue = beforeClientBalance.get(takenCurrency);
        BigDecimal takenValue = beforeValue.add(BigDecimal.ONE);

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> exchange.takeMoney(client, takenCurrency, takenValue));
    }

    @Test
    public void addOrderTest() throws ClientNotFoundException {
        Exchange exchange = exchangeFactory.create(false);
        Client client = exchange.createClient();

        exchange.pushMoney(client, USD, 10);
        Order order = Order.builder(client).buy(130, RUB).selling(2, USD);
        exchange.createOrder(order);

        List<Order> orders = exchange.getOpenOrders();
        Assertions.assertTrue(orders.contains(order));
    }

    @Test
    public void coveringOrdersTest() throws ClientNotFoundException {
        Exchange exchange = exchangeFactory.create(false);
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
        Exchange exchange = exchangeFactory.create(true);
        int clientCount = 10;
        int clientOrderCount = 30000;

        ExecutorService executorService = Executors.newFixedThreadPool(clientCount);
        Collection<Client> clients = initExchange(exchange, clientCount, executorService);

        Balance startGeneralBalance = exchange.getGeneralBalance();
        long startTime = System.nanoTime();
        awaitAddingOrdersTasks(exchange, executorService, clients, clientOrderCount);
        long time = System.nanoTime() - startTime;
        Balance endGeneralBalance = exchange.getGeneralBalance();

        long expectedOrderCount = successOrderCreatingCount.get();

        long openOrderCount = exchange.getOpenOrders().size();
        long coverCount = exchange.getCoverCount();
        long actualOrderCount = openOrderCount + coverCount * 2;

        System.out.println("Orders: " + actualOrderCount);
        System.out.println("Open order count: " + openOrderCount);
        System.out.println("Cover count: " + coverCount);
        System.out.println("time: " + time * 1E-9 + " sec");
        int orderPerSec = (int) (actualOrderCount / (time * 1E-9));
        System.out.println("orders per sec: " + orderPerSec);

        Assertions.assertEquals(startGeneralBalance, endGeneralBalance);
        Assertions.assertEquals(expectedOrderCount, actualOrderCount);
        System.out.println();
    }

    public Collection<Client> initExchange(Exchange exchange, int clientCount, ExecutorService service) throws InterruptedException, ExecutionException {
        Collection<Callable<Client>> tasks = new ArrayList<>(clientCount);
        Callable<Client> clientInit = () -> {
            Client client = exchange.createClient();
            for (Currency currency : Currency.values()) {
                try {
                    exchange.pushMoney(client, currency, Integer.MAX_VALUE);
                } catch (ClientNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            return client;
        };
        for (int i = 0; i < clientCount; i++) {
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
        for (Future<T> future : futures) {
            result.add(future.get());
        }
        return result;
    }

    private Callable<Void> getTarget(Exchange exchange, Client client, int orderCount) {
        return () -> {
            for (int i = 0; i < orderCount; i++) {
                Order order = OrderGenerator.generate(client);
                ResponseStatus status = exchange.createOrder(order);
                if (status != ResponseStatus.ERROR) successOrderCreatingCount.incrementAndGet();
            }
            return null;
        };
    }
}