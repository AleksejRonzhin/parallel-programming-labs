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
import java.math.RoundingMode;
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
       clientMoneyRepository.putClientMoney(client, currency, value);
    }

    @Override
    public void takeMoney(Client client, Currency currency, BigDecimal value) {
        boolean result = clientMoneyRepository.takeClientMoney(client, currency, value);
        if(!result){
           throw new RuntimeException();
        }
    }

    @Override
    public void createOrder(Order order) {

        clientMoneyRepository.takeClientMoney(order.getOrderInfo().getClient(),
                order.getSourceCurrency(), order.getOrderInfo().getTargetValue()
                        .multiply(order.getOrderInfo().getRate()));

        List<OrderInfo> orders = orderRepository.getOrderInfoList(order.getCurrencyPair(),
                order.getSourceCurrency());

        if(orders.size() == 0){
            orderRepository.addOrder(order);
            return;
        }

        synchronized (orders){
            BigDecimal minLimitSourceRate = BigDecimal.ONE.divide(
                    order.getOrderInfo().getRate(), 8, RoundingMode.UP);
            OrderInfo bestOrderInfo = orders.get(0);
            for (OrderInfo orderInfo : orders) {
                if (bestOrderInfo.getRate().compareTo(orderInfo.getRate()) < 0) {
                    bestOrderInfo = orderInfo;
                }
            }
            if(bestOrderInfo.getRate().compareTo(minLimitSourceRate) >= 0){
                BigDecimal bestOrderSourceValue = bestOrderInfo.getTargetValue()
                        .multiply(bestOrderInfo.getRate());
                BigDecimal newOrderTargetValue = order.getOrderInfo().getTargetValue();
                BigDecimal orderValue = bestOrderSourceValue.min(newOrderTargetValue);

                BigDecimal bestOrderReverseRate = BigDecimal.ONE.divide(
                        bestOrderInfo.getRate(), 8, RoundingMode.UP);


                BigDecimal newOrderCashback = order.getOrderInfo().getTargetValue().multiply(order.getOrderInfo().getRate()).subtract(orderValue.multiply(bestOrderReverseRate));
                clientMoneyRepository.putClientMoney(order.getOrderInfo().getClient(), order.getSourceCurrency(), newOrderCashback);
                clientMoneyRepository.putClientMoney(order.getOrderInfo().getClient(), order.getTargetCurrency(), orderValue);

                System.out.println();
                BigDecimal bestOrderCashback = bestOrderInfo.getTargetValue().multiply(bestOrderInfo.getRate()).subtract(orderValue);
                clientMoneyRepository.putClientMoney(bestOrderInfo.getClient(), order.getTargetCurrency(), bestOrderCashback);
                clientMoneyRepository.putClientMoney(bestOrderInfo.getClient(), order.getSourceCurrency(), orderValue.multiply(bestOrderReverseRate));
                System.out.println();
                orderRepository.removeOrder(order.getCurrencyPair(), order.getSourceCurrency(),
                        bestOrderInfo);
            } else {
                orderRepository.addOrder(order);
            }
        }


    }

    @Override
    public void getOpenOrders() {

    }

    @Override
    public Map<Currency, BigDecimal> getClientMoney(Client client) {
        return null;
    }
}