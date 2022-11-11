package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;

import javax.annotation.concurrent.ThreadSafe;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

@ThreadSafe
public class ClientMoneyRepository {
    private final Lock lock = new ReentrantLock();

    private final Map<Client, ConcurrentHashMap<Currency, BigDecimal>> clientMoney = new IdentityHashMap<>();

    public void addClient(Client client) {
        try {
            lock.lock();
            clientMoney.put(client, new ConcurrentHashMap<>());
        } finally {
            lock.unlock();
        }
    }

    public Map<Currency, BigDecimal> getClientMoney(Client client) {
        try {
            lock.lock();
            return clientMoney.get(client);
        } finally {
            lock.unlock();
        }
    }

    public void pushClientMoney(Client client, Currency currency, BigDecimal value) {
        Map<Currency, BigDecimal> clientMoney = getClientMoney(client);
        clientMoney.compute(currency, (key, oldValue) -> clientMoney.getOrDefault(key, BigDecimal.ZERO).add(value));
    }

    public boolean takeClientMoney(Client client, Currency currency, BigDecimal value) {
        Map<Currency, BigDecimal> clientMoney = getClientMoney(client);
        MoneyTaker moneyTaker = new MoneyTaker(value);
        clientMoney.computeIfPresent(currency, moneyTaker);
        return moneyTaker.isSuccess;
    }

    public Map<Currency, BigDecimal> getAllMoney() {
        try {
            lock.lock();
            Map<Currency, BigDecimal> result = new HashMap<>();
            clientMoney.forEach((client, map) -> map.forEach((currency, money) -> result.compute(currency, (key, value) -> result.getOrDefault(key, BigDecimal.ZERO).add(money))));
            return result;
        } finally {
            lock.unlock();
        }
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
                return oldValue;
            }
            return newValue;
        }
    }
}