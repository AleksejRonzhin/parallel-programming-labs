package ru.rsreu.labs.models;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.util.Objects;

@Immutable
public class Order {
    private final CurrencyPair currencyPair;
    private final OrderInfo orderInfo;
    private final boolean isSelling;

    public Order(CurrencyPair currencyPair, OrderInfo orderInfo, boolean isSelling) {
        this.currencyPair = currencyPair;
        this.orderInfo = orderInfo;
        this.isSelling = isSelling;
    }

    public Order(Currency sourceCurrency, Currency targetCurrency, BigDecimal targetValue, BigDecimal sourceToTargetRate, Client client) {
        this(CurrencyPair.create(sourceCurrency, targetCurrency), OrderInfo.createByRate(targetValue, sourceToTargetRate, client), CurrencyPair.create(sourceCurrency, targetCurrency).getFirstCurrency() == sourceCurrency);
    }

    public static Builder builder(Client client) {
        return new Builder(client);
    }

    @Override
    public String toString() {
        return "Order{" + "currencyPair=" + currencyPair + ", orderInfo=" + orderInfo + ", isSelling=" + isSelling + '}';
    }

    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    public Currency getSourceCurrency() {
        if (isSelling) {
            return currencyPair.getFirstCurrency();
        } else {
            return currencyPair.getSecondCurrency();
        }
    }

    public Currency getTargetCurrency() {
        if (isSelling) {
            return currencyPair.getSecondCurrency();
        } else {
            return currencyPair.getFirstCurrency();
        }
    }

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public BigDecimal getTargetValue() {
        return orderInfo.getTargetValue();
    }

    public BigDecimal getSourceValue() {
        return orderInfo.getSourceValue();
    }

    public BigDecimal getSourceToTargetRate() {
        return orderInfo.getSourceToTargetRate();
    }

    public BigDecimal getTargetToSourceRate() {
        return orderInfo.getTargetToSourceRate();
    }

    public Client getClient() {
        return orderInfo.getClient();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (!Objects.equals(currencyPair, order.currencyPair)) return false;
        return Objects.equals(orderInfo, order.orderInfo);
    }

    @Override
    public int hashCode() {
        int result = currencyPair != null ? currencyPair.hashCode() : 0;
        result = 31 * result + (orderInfo != null ? orderInfo.hashCode() : 0);
        return result;
    }

    public boolean isSelling() {
        return isSelling;
    }

    public static class Builder {
        private final Client client;
        private Currency targetCurrency;
        private BigDecimal targetValue;

        public Builder(Client client) {
            this.client = client;
        }

        public Builder buy(BigDecimal targetValue, Currency targetCurrency) {
            this.targetValue = targetValue;
            this.targetCurrency = targetCurrency;
            return this;
        }

        public Builder buy(int targetValue, Currency targetCurrency) {
            return buy(new BigDecimal(targetValue), targetCurrency);
        }

        public Builder buy(String targetValue, Currency targetCurrency) {
            return buy(new BigDecimal(targetValue), targetCurrency);
        }

        public Order at(BigDecimal rate, Currency sourceCurrency) {
            return new Order(sourceCurrency, targetCurrency, targetValue, rate, client);
        }

        public Order at(int rate, Currency sourceCurrency) {
            return at(new BigDecimal(rate), sourceCurrency);
        }

        public Order at(String rate, Currency sourceCurrency) {
            return at(new BigDecimal(rate), sourceCurrency);
        }


        public Order selling(BigDecimal sourceValue, Currency sourceCurrency) {
            OrderInfo orderInfo = OrderInfo.createByCurrencyValues(sourceValue, targetValue, client);
            CurrencyPair currencyPair = CurrencyPair.create(sourceCurrency, targetCurrency);
            boolean isSelling = currencyPair.getFirstCurrency() == sourceCurrency;
            return new Order(currencyPair, orderInfo, isSelling);
        }

        public Order selling(int sourceValue, Currency sourceCurrency) {
            return selling(new BigDecimal(sourceValue), sourceCurrency);
        }

        public Order selling(String sourceValue, Currency sourceCurrency) {
            return selling(new BigDecimal(sourceValue), sourceCurrency);
        }
    }
}
