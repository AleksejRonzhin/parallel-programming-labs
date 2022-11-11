package ru.rsreu.labs.sync;

import ru.rsreu.labs.Exchange;
import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;
import ru.rsreu.labs.models.OrderInfo;
import ru.rsreu.labs.repositories.ClientMoneyRepository;
import ru.rsreu.labs.repositories.OrderRepository;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        clientMoneyRepository.putClientMoney(client, currency, value);
    }

    @Override
    public void takeMoney(Client client, Currency currency, BigDecimal value) {
        boolean result = clientMoneyRepository.takeClientMoney(client, currency, value);
        if (!result) {
            throw new RuntimeException();
        }
    }

    @Override
    public void createOrder(Order order) {
        takeMoney(order.getClient(), order.getSourceCurrency(), order.getSourceValue());
        boolean isCovered = tryFindAndCoverOrder(order);
        if (!isCovered) {
            List<OrderInfo> orders = orderRepository.getOrderInfosByCurrencyPair(order.getCurrencyPair(), order.getTargetCurrency());
            synchronized (orders) {
                orders.add(order.getOrderInfo());
            }
        }
    }

    private boolean tryFindAndCoverOrder(Order order) {
        List<OrderInfo> orders = orderRepository.getOrderInfosByCurrencyPair(order.getCurrencyPair(), order.getSourceCurrency());
        synchronized (orders) {
            Optional<OrderInfo> coverOrder = tryFindCoverOrder(order.getOrderInfo(), orders);
            if (coverOrder.isPresent()) {
                coverOrders(order.getSourceCurrency(), order.getTargetCurrency(), order.getOrderInfo(), coverOrder.get());
                orders.remove(coverOrder.get());
                return true;
            }
            return false;
        }
    }

    private Optional<OrderInfo> tryFindCoverOrder(OrderInfo newOrder, List<OrderInfo> orders) {
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
        clientMoneyRepository.putClientMoney(newOrderInfo.getClient(), sourceCurrency, newOrderCashback);
        clientMoneyRepository.putClientMoney(newOrderInfo.getClient(), targetCurrency, targetCurrencyOrderSum);

        BigDecimal oldOrderCashback = oldOrderInfo.getSourceValue().subtract(targetCurrencyOrderSum);
        clientMoneyRepository.putClientMoney(oldOrderInfo.getClient(), targetCurrency, oldOrderCashback);
        clientMoneyRepository.putClientMoney(oldOrderInfo.getClient(), sourceCurrency, sourceCurrencyOrderSum);
    }

    @Override
    public void getOpenOrders() {

    }

    @Override
    public Map<Currency, BigDecimal> getClientMoney(Client client) {
        return clientMoneyRepository.getClientMoney(client);
    }
}