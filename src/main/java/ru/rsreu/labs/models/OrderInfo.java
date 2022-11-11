package ru.rsreu.labs.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderInfo {
    private final BigDecimal targetValue;
    private final BigDecimal sourceToTargetRate; // source/target
    private final Client client;

    public OrderInfo(BigDecimal targetValue, BigDecimal sourceToTargetRate, Client client) {
        this.targetValue = targetValue;
        this.sourceToTargetRate = sourceToTargetRate;
        this.client = client;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public BigDecimal getSourceValue() {
        return targetValue.multiply(sourceToTargetRate);
    }

    public BigDecimal getSourceToTargetRate() {
        return sourceToTargetRate;
    }

    public BigDecimal getTargetToSourceRate() {
        return BigDecimal.ONE.divide(sourceToTargetRate, 8, RoundingMode.HALF_EVEN);
    }

    public Client getClient() {
        return client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderInfo orderInfo = (OrderInfo) o;

        if (!targetValue.equals(orderInfo.targetValue)) return false;
        if (!sourceToTargetRate.equals(orderInfo.sourceToTargetRate)) return false;
        return client.equals(orderInfo.client);
    }

    @Override
    public int hashCode() {
        int result = targetValue.hashCode();
        result = 31 * result + sourceToTargetRate.hashCode();
        result = 31 * result + client.hashCode();
        return result;
    }
}
