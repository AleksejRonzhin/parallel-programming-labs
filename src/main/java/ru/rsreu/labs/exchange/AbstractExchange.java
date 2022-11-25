package ru.rsreu.labs.exchange;

import ru.rsreu.labs.exceptions.ClientNotFoundException;
import ru.rsreu.labs.exceptions.NotEnoughMoneyException;
import ru.rsreu.labs.models.*;
import ru.rsreu.labs.repositories.ClientBalanceRepository;
import ru.rsreu.labs.repositories.ConcurrentClientBalanceRepository;
import ru.rsreu.labs.repositories.OrderRepository;
import ru.rsreu.labs.utils.BigDecimalUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static ru.rsreu.labs.utils.BigDecimalUtils.getCommission;

@NotThreadSafe
public abstract class AbstractExchange implements Exchange {
    private final static BigDecimal COMMISSION_PERCENT = new BigDecimal("0.01");
    private final ClientBalanceRepository clientBalanceRepository = new ConcurrentClientBalanceRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final boolean withCommission;
    private final ExchangeHelper helper;

    protected AbstractExchange(boolean withCommission, ExchangeHelper helper) {
        this.withCommission = withCommission;
        this.helper = helper;
    }

    public ResponseStatus unsafeCreateOrder(Order order) {
        return safeCreateOrder(order, (ignored, target) -> target.get());
    }

    protected ResponseStatus safeCreateOrder(Order order, BiFunction<OrderRepository.OrderListPair, Supplier<ResponseStatus>, ResponseStatus> actionWithPair) {
        try {
            takeMoney(order.getClient(), order.getSourceCurrency(), order.getSourceValue());
            OrderRepository.OrderListPair orderListPair = getOrderListPair(order.getCurrencyPair());
            return actionWithPair.apply(orderListPair, () -> {
                try {
                    return coverOrCreateOrder(order, orderListPair);
                } catch (ClientNotFoundException e) {
                    return ResponseStatus.ERROR;
                }
            });
        } catch (NotEnoughMoneyException | ClientNotFoundException e) {
            return ResponseStatus.ERROR;
        }
    }

    public List<Order> unsafeGetOpenOrders() {
        return safeGetOpenOrders((ignored, target) -> target.run());
    }

    protected List<Order> safeGetOpenOrders(BiConsumer<CurrencyPair, Runnable> actionWithCurrencyPair) {
        List<Order> openOrders = new ArrayList<>();
        Map<CurrencyPair, OrderRepository.OrderListPair> ordersMap = getOrders();
        ordersMap.forEach((currencyPair, ordersPair) -> actionWithCurrencyPair.accept(currencyPair, () -> {
            ordersPair.getSellOrders().forEach(order -> openOrders.add(new Order(currencyPair, order, true)));
            ordersPair.getBuyOrders().forEach(order -> openOrders.add(new Order(currencyPair, order, false)));
        }));
        return openOrders;
    }

    @Override
    public Client createClient() {
        Client client = new Client();
        clientBalanceRepository.addClient(client);
        return client;
    }

    @Override
    public void pushMoney(Client client, Currency currency, BigDecimal value) throws ClientNotFoundException {
        clientBalanceRepository.pushMoney(client, currency, value);
    }

    @Override
    public void takeMoney(Client client, Currency currency, BigDecimal value) throws NotEnoughMoneyException, ClientNotFoundException {
        boolean isSuccess = clientBalanceRepository.tryTakeMoney(client, currency, value);
        if (!isSuccess) {
            throw new NotEnoughMoneyException();
        }
    }

    @Override
    public Balance getClientBalance(Client client) throws ClientNotFoundException {
        return clientBalanceRepository.getClientBalance(client);
    }

    @Override
    public Balance getGeneralBalance() {
        Balance clientBalance = clientBalanceRepository.getGeneralClientsBalance();
        Balance openOrdersCost = getOpenOrdersCost();
        Balance commission = new Balance(helper.getBank());
        return clientBalance.add(openOrdersCost.add(commission));
    }


    private Balance getOpenOrdersCost() {
        ConcurrentHashMap<Currency, BigDecimal> result = new ConcurrentHashMap<>();
        List<Order> openOrders = getOpenOrders();
        openOrders.forEach(order -> result.compute(order.getSourceCurrency(), (key, value) -> result.getOrDefault(key, BigDecimal.ZERO).add(order.getSourceValue())));
        return new Balance(result);
    }

    protected OrderRepository.OrderListPair getOrderListPair(CurrencyPair currencyPair) {
        return orderRepository.getOrdersByCurrencyPair(currencyPair);
    }

    protected Map<CurrencyPair, OrderRepository.OrderListPair> getOrders() {
        return orderRepository.getOrders();
    }

    protected ResponseStatus coverOrCreateOrder(Order order, OrderRepository.OrderListPair orderListPair) throws ClientNotFoundException {
        List<OrderInfo> backOrders = order.isSelling() ? orderListPair.getBuyOrders() : orderListPair.getSellOrders();
        boolean isCovered = tryFindAndCoverOrder(order, backOrders);
        if (isCovered) return ResponseStatus.COVERED;

        List<OrderInfo> orders = order.isSelling() ? orderListPair.getSellOrders() : orderListPair.getBuyOrders();
        orders.add(order.getOrderInfo());
        return ResponseStatus.CREATED;
    }

    private boolean tryFindAndCoverOrder(Order order, List<OrderInfo> orders) throws ClientNotFoundException {
        Optional<OrderInfo> coverOrder = getCoveredOrder(order.getOrderInfo(), orders);
        if (coverOrder.isPresent()) {
            coverOrders(order.getSourceCurrency(), order.getTargetCurrency(), order.getOrderInfo(), coverOrder.get());
            orders.remove(coverOrder.get());
            return true;
        }
        return false;
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

    private void coverOrders(Currency sourceCurrency, Currency targetCurrency, OrderInfo newOrderInfo, OrderInfo oldOrderInfo) throws ClientNotFoundException {
        BigDecimal targetCurrencyOrderSum = oldOrderInfo.getSourceValue().min(newOrderInfo.getTargetValue());
        BigDecimal sourceCurrencyOrderSum = BigDecimalUtils.getValueByRate(targetCurrencyOrderSum, oldOrderInfo.getTargetToSourceRate());

        BigDecimal newOrderCashback = newOrderInfo.getSourceValue().subtract(sourceCurrencyOrderSum);
        clientBalanceRepository.pushMoney(newOrderInfo.getClient(), sourceCurrency, newOrderCashback);
        BigDecimal oldOrderCashback = oldOrderInfo.getSourceValue().subtract(targetCurrencyOrderSum);
        clientBalanceRepository.pushMoney(oldOrderInfo.getClient(), targetCurrency, oldOrderCashback);

        if (withCommission) {
            targetCurrencyOrderSum = chargeCommission(targetCurrencyOrderSum, targetCurrency);
            sourceCurrencyOrderSum = chargeCommission(sourceCurrencyOrderSum, sourceCurrency);
        }
        clientBalanceRepository.pushMoney(oldOrderInfo.getClient(), sourceCurrency, sourceCurrencyOrderSum);
        clientBalanceRepository.pushMoney(newOrderInfo.getClient(), targetCurrency, targetCurrencyOrderSum);

        helper.incrementCoverCount();
    }

    private BigDecimal chargeCommission(BigDecimal value, Currency currency) {
        BigDecimal commission = getCommission(value, COMMISSION_PERCENT);
        Map<Currency, BigDecimal> bank = helper.getBank();
        bank.compute(currency, (key, ignored) -> bank.getOrDefault(key, BigDecimal.ZERO).add(commission));
        return value.subtract(commission);
    }

    @Override
    public long getCoverCount() {
        return helper.getCoverCount();
    }
}
