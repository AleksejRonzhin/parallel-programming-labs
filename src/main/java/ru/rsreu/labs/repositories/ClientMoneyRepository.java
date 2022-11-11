package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

@ThreadSafe
public class ClientMoneyRepository {
    private final Lock lock = new ReentrantLock();

    private final Map<Client, ConcurrentHashMap<Currency, BigDecimal>> clientMoneys = new IdentityHashMap<>();

    public void addClient(Client client) {
        try {
            lock.lock();
            clientMoneys.put(client, new ConcurrentHashMap<>());
        } finally {
            lock.unlock();
        }
    }

    public Map<Currency, BigDecimal> getClientMoney(Client client) {
        return clientMoneys.get(client);
    }

    public void putClientMoney(Client client, Currency currency, BigDecimal value) {
        Map<Currency, BigDecimal> clientMoney = getClientMoney(client);
        clientMoney.put(currency, clientMoney.getOrDefault(currency, BigDecimal.ZERO).add(value));
//
//        clientMoney.computeIfPresent(currency, (key, oldValue) -> oldValue.add(value));
//        clientMoney.putIfAbsent(currency, value);
    }

    public boolean takeClientMoney(Client client, Currency currency, BigDecimal value) {
        Map<Currency, BigDecimal> clientMoney = getClientMoney(client);
        MoneyTaker moneyTaker = new MoneyTaker(value);
        clientMoney.computeIfPresent(currency, moneyTaker);
        return moneyTaker.isSuccess;
    }

    static private class MoneyTaker implements BiFunction<Currency, BigDecimal, BigDecimal> {
        private final BigDecimal value;
        private boolean isSuccess = true;

        public MoneyTaker(BigDecimal value) {
            this.value = value;
        }

        @Override
        public BigDecimal apply(Currency currency, BigDecimal oldValue) {
            BigDecimal newValue = oldValue.subtract(value);
            if (newValue.compareTo(BigDecimal.ZERO) < 0) {
                isSuccess = false;
            }
            return newValue;
        }
    }
}