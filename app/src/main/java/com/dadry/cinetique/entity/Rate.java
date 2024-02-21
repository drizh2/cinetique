package com.dadry.cinetique.entity;

import java.util.HashMap;
import java.util.Map;

public enum Rate {
    VERY_BAD("Very bad", 1),
    BAD("Bad", 2),
    MIDDLE("Middle", 3),
    GOOD("Good", 4),
    PERFECT("Perfect", 5);

    private static final Map<Integer, Rate> intValueMap = new HashMap<>();

    static {
        for (Rate rate : values()) {
            intValueMap.put(rate.getIntValue(), rate);
        }
    }

    private final String stringValue;
    private final Integer intValue;

    Rate(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public static Rate getRateByInteger(Integer rate) {
        return intValueMap.get(rate);
    }
}
