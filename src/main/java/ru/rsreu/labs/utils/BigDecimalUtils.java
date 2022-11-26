package ru.rsreu.labs.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {
    private static final int VALUE_SCALE = 6;
    private static final int RATE_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public static BigDecimal setRateScale(BigDecimal value){
        return value.setScale(RATE_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal setValueScale(BigDecimal value){
        return value.setScale(VALUE_SCALE, ROUNDING_MODE);
    }

    public static boolean equals(BigDecimal value1, BigDecimal value2){
        return setValueScale(value1).equals(setValueScale(value2));
    }

    public static BigDecimal getRate(BigDecimal source, BigDecimal target){
        return setRateScale(source).divide(setRateScale(target), RATE_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal getValueByRate(BigDecimal sourceValue, BigDecimal targetToSourceRate){
        return sourceValue.multiply(targetToSourceRate).setScale(VALUE_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal getInverseRate(BigDecimal rate){
        return BigDecimal.ONE.divide(rate, RATE_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal getCommission(BigDecimal value, BigDecimal commissionPercent){
        return setValueScale(value.multiply(commissionPercent));
    }
}
