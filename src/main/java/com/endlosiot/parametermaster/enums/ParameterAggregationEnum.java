package com.endlosiot.parametermaster.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

public enum ParameterAggregationEnum implements EnumType {
    SUM(1, "SUM"),
    AVG(2, "AVG"),
    RANGE(3, "MIN-MAX"),
    ABSOLUTE(4, "ABSOLUTE"),
    CUMULATIVE(5, "CUMULATIVE");

    private final int id;
    private final String name;

    private static final Map<Integer, ParameterAggregationEnum> MAP = new HashMap<>();

    static {
        for (ParameterAggregationEnum aggregationEnum : values()) {
            getMap().put(aggregationEnum.getId(), aggregationEnum);
        }
    }

    ParameterAggregationEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * These methods are used to fetch Enum base on given id.
     *
     * @param id enum key
     * @return portEnum enum
     */
    public static ParameterAggregationEnum fromId(Integer id) {
        return getMap().get(id);
    }

    public static Map<Integer, ParameterAggregationEnum> getMap() {
        return MAP;
    }
}
