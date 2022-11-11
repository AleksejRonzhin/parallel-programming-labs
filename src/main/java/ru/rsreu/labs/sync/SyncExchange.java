package ru.rsreu.labs.sync;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.*;
import ru.rsreu.labs.repositories.ClientMoneyRepository;
import ru.rsreu.labs.repositories.OrderRepository;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.*;

@ThreadSafe
public class SyncExchange implements Exchange {
    private final OrderRepository orderRepository = new OrderRepository();
    private final ClientMoneyRepository clientMoneyRepository = new ClientMoneyRepository();

    @Override
    public Client createClient() {
        Client client = new Client();
        clientMoneyRepository.addClient(client);
        return client;
    }

    @Override
    public void putMoney(Client client, Currency currency, BigDecimal value) {
        clientMoneyRepository.pushClientMoney(client, currency, value);
    }

    @Override
    public void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException {
        boolean result = clientMoneyRepository.takeClientMoney(client, currency, value);
        if (!result) {
            throw new NotEnoughMoneyException();
        }
    }

    @Override
    public void createOrder(Order order) throws NotEnoughMoneyException {
        takeMoney(order.getClient(), order.getSourceCurrency(), order.getSourceValue());
        boolean isCovered = tryFindAndCoverOrder(order);
        if (!isCovered) {
            List<OrderInfo> orders = orderRepository.getOrdersByCurrencyPair(order.getCurrencyPair());
            synchronized (orders) {
                orders.add(order.getOrderInfo());
            }
        }
    }

    private boolean tryFindAndCoverOrder(Order order) {
        List<OrderInfo> orders = orderRepository.getOrdersByCurrencyPair(order.getCurrencyPair().inverse());
        synchronized (orders) {
            Optional<OrderInfo> coverOrder = getCoveredOrder(order.getOrderInfo(), orders);
            if (coverOrder.isPresent()) {
                coverOrders(order.getSourceCurrency(), order.getTargetCurrency(), order.getOrderInfo(), coverOrder.get());
                orders.remove(coverOrder.get());
                return true;
            }
            return false;
        }
    }

    private Optional<OrderInfo> getCoveredOrder(OrderInfo newOrder, List<OrderInfo> orders) {
        if (orders.size() == 0) return Optional.empty();
        OrderInfo bestOrderInfo = orders.get(0);
        for (OrderInfo orderInfo : orders) {
            if (bestOrderInfo.getSourceToTargetRate().compareTo(orderInfo.getSourceToTargetRate()) <= 0) {
                bestOrderInfo = orderInfo;
            }
        }
        if (bestOrderInfo.getSourceToTargetRate().compareTo(newOrder.getTargetToSourceRate()) >= 0) {
            return Optional.of(bestOrderInfo);
        } else {
            return Optional.empty();
        }
    }

    private void coverOrders(Currency sourceCurrency, Currency targetCurrency, OrderInfo newOrderInfo, OrderInfo oldOrderInfo) {
        BigDecimal targetCurrencyOrderSum = oldOrderInfo.getSourceValue().min(newOrderInfo.getTargetValue());
        BigDecimal sourceCurrencyOrderSum = targetCurrencyOrderSum.multiply(oldOrderInfo.getTargetToSourceRate());

        BigDecimal newOrderCashback = newOrderInfo.getSourceValue().subtract(sourceCurrencyOrderSum);
        clientMoneyRepository.pushClientMoney(newOrderInfo.getClient(), sourceCurrency, newOrderCashback);
        clientMoneyRepository.pushClientMoney(newOrderInfo.getClient(), targetCurrency, targetCurrencyOrderSum);

        BigDecimal oldOrderCashback = oldOrderInfo.getSourceValue().subtract(targetCurrencyOrderSum);
        clientMoneyRepository.pushClientMoney(oldOrderInfo.getClient(), targetCurrency, oldOrderCashback);
        clientMoneyRepository.pushClientMoney(oldOrderInfo.getClient(), sourceCurrency, sourceCurrencyOrderSum);
    }

    @Override
    public List<Order> getOpenOrders() {
        List<Order> openOrders = new ArrayList<>();
        Map<CurrencyPair, List<OrderInfo>> ordersMap = orderRepository.getOrders();
        ordersMap.forEach((currencyPair, orders) -> {
            synchronized (orders) {
                orders.forEach(order -> openOrders.add(new Order(currencyPair, order)));
            }
        });
        return openOrders;
    }

    @Override
    public Map<Currency, BigDecimal> getClientMoney(Client client) {
        return clientMoneyRepository.getClientMoney(client);
    }

    @Override
    public Map<Currency, BigDecimal> getExchangeAndClientsMoney() {
        Map<Currency, BigDecimal> clientMoney = clientMoneyRepository.getAllMoney();
        Map<Currency, BigDecimal> ordersMoney = getOpenOrdersMoney();

        Map<Currency, BigDecimal> result = new HashMap<>();
        for (Currency currency : Currency.values()) {
            result.put(currency, clientMoney.getOrDefault(currency, BigDecimal.ZERO)
                    .add(ordersMoney.getOrDefault(currency, BigDecimal.ZERO)));
        }
        return result;
    }

    private Map<Currency, BigDecimal> getOpenOrdersMoney() {
        Map<Currency, BigDecimal> result = new HashMap<>();
        List<Order> openOrders = getOpenOrders();
        openOrders.forEach(order -> result.compute(order.getSourceCurrency(), (key, value) -> result.getOrDefault(key, BigDecimal.ZERO).add(order.getSourceValue())));
        return result;
    }
}