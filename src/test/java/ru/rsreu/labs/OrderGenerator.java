package ru.rsreu.labs;

import ru.rsreu.labs.models.Client;
import ru.rsreu.labs.models.Currency;
import ru.rsreu.labs.models.Order;

import java.math.BigDecimal;

public class OrderGenerator {

    public static Order generate(Client client) {
        Currency targetCurrency = getRandomCurrency();
        Currency sourceCurrency = getRandomCurrency();
        BigDecimal sourceValue = getRandomValue();
        BigDecimal targetValue = getRandomValue();
        return Order.builder(client).buy(targetValue, targetCurrency).selling(sourceValue, sourceCurrency);
    }

    private static Currency getRandomCurrency() {
        Currency[] currencies = Currency.values();
        int index = getRandomValueInRange(0, currencies.length);
        return currencies[index];
    }

    private static BigDecimal getRandomValue() {
        return new BigDecimal(getRandomValueInRange(1, 100));
    }

    private static int getRandomValueInRange(int left, int right) {
        return left + (int) (Math.random() * (right - left));
    }
}
