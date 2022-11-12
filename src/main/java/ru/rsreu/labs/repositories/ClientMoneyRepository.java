package ru.rsreu.labs.repositories;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Money;

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

    private final Map<Client, ConcurrentHashMap<Currency, BigDecimal>>
            clientMoneyStorage = new IdentityHashMap<>();

    public void addClient(Client client) {
        try {
            lock.lock();
            clientMoneyStorage.put(client, new ConcurrentHashMap<>());
        } finally {
            lock.unlock();
        }
    }

    public Money getClientMoney(Client client) {
        try {
            lock.lock();
            return new Money(clientMoneyStorage.get(client));
        } finally {
            lock.unlock();
        }
    }

    private ConcurrentHashMap<Currency, BigDecimal> getClientMoneyMap(Client client){
        return clientMoneyStorage.get(client);
    }

    public void pushClientMoney(Client client, Currency currency, BigDecimal value) {
        ConcurrentHashMap<Currency, BigDecimal> clientMoneyMap = getClientMoneyMap(client);
        clientMoneyMap.compute(currency, (key, oldValue) -> clientMoneyMap
                .getOrDefault(key, BigDecimal.ZERO).add(value));
    }

    public boolean takeClientMoney(Client client, Currency currency, BigDecimal value) {
        ConcurrentHashMap<Currency, BigDecimal> clientMoneyMap = getClientMoneyMap(client);
        MoneyTaker moneyTaker = new MoneyTaker(value);
        clientMoneyMap.compute(currency, moneyTaker);
        return moneyTaker.isSuccess;
    }

    public Money getAllMoney() {
        try {
            lock.lock();
            ConcurrentHashMap<Currency, BigDecimal> result = new ConcurrentHashMap<>();
            clientMoneyStorage.forEach((client, map) -> map.forEach((currency, money) ->
                    result.compute(currency, (key, value) ->
                            result.getOrDefault(key, BigDecimal.ZERO).add(money))));
            return new Money(result);
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
            BigDecimal prevValue = oldValue;
            if(prevValue == null) prevValue = BigDecimal.ZERO;

            BigDecimal newValue = prevValue.subtract(value);
            if (newValue.compareTo(BigDecimal.ZERO) < 0) {
                isSuccess = false;
                return prevValue;
            }
            return newValue;
        }
    }
}