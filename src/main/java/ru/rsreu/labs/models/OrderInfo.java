package ru.rsreu.labs.models;

import ru.rsreu.labs.utils.BigDecimalUtils;

import javax.annotation.concurrent.Immutable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static ru.rsreu.labs.utils.BigDecimalUtils.*;

@Immutable
public class OrderInfo {
    private final BigDecimal sourceValue;
    private final BigDecimal targetValue;
    private final BigDecimal sourceToTargetRate;
    private final BigDecimal targetToSourceRate;
    private final Client client;

    public static OrderInfo createByRate(BigDecimal targetValue, BigDecimal sourceToTargetRate, Client client){
        return new OrderInfo(getValueByRate(targetValue, sourceToTargetRate), targetValue,
                sourceToTargetRate, getInverseRate(sourceToTargetRate), client);
    }

    public static OrderInfo createByCurrencyValues(BigDecimal sourceValue, BigDecimal targetValue, Client client){
        return new OrderInfo(sourceValue, targetValue, getRate(sourceValue, targetValue),
                getRate(targetValue, sourceValue), client);
    }

    private OrderInfo(BigDecimal sourceValue, BigDecimal targetValue, BigDecimal sourceToTargetRate, BigDecimal targetToSourceRate, Client client) {
        this.targetValue = setValueScale(targetValue);
        this.sourceValue = setValueScale(sourceValue);
        this.sourceToTargetRate = setRateScale(sourceToTargetRate);
        this.targetToSourceRate = setRateScale(targetToSourceRate);
        this.client = client;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public BigDecimal getSourceValue() {
        return sourceValue;
    }

    public BigDecimal getSourceToTargetRate() {
        return sourceToTargetRate;
    }

    public BigDecimal getTargetToSourceRate() {
        return targetToSourceRate;
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
