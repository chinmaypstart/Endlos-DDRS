package com.endlosiot.screen.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aryan.Dave
 * @since 04/04/2024
 */
public enum ReportTypeEnum implements EnumType {

    DATE_RANGE(1, "Date Range"), DAILY(2, "Daily"), WEEKLY(3, "Weekly"),
    MONTHLY(4, "Monthly");
    public static final Map<Integer, ReportTypeEnum> MAP = new HashMap<>();

    static {
        for (ReportTypeEnum groupEnum : values()) {
            MAP.put(groupEnum.getId(), groupEnum);
        }
    }

    private final int id;
    private final String name;

    ReportTypeEnum(int id, String name) {
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
     * This methods is used to fetch Enum base on given id.
     *
     * @param id enum key
     * @return rightsEnums enum
     */

    public static ReportTypeEnum getFromID(Integer id) {
        return MAP.get(id);
    }

    public static ReportTypeEnum getByType(String type) {
        return ReportTypeEnum.valueOf(type.toUpperCase());
    }
}
