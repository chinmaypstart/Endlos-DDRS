package com.endlosiot.screen.enums;

/**
 * @author Aryan.Dave
 * @since 08/04/2024
 */
import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

public enum MonthTypeEnum implements EnumType {

    JANUARY(1,"january"),FEBRUARY(2,"February"),MARCH(3,"March"),APRIL(4,"April"),MAY(5,"May"),JUNE(6,"June"),JULY(7,"July"),AUGUST(8,"August"),SEPTEMBER(9,"September"),OCTOBER(10,"October"),NOVEMBER(11,"November"),DECEMBER(12,"December");;
    public  static final Map<Integer,MonthTypeEnum> MAP = new HashMap<>();

    private final int id;
    private final String name;

    MonthTypeEnum(int id , String name)
    {
        this.id=id;
        this.name= name;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "";
    }

    /**
     * This methods is used to fetch Enum base on given id.
     *
     * @param id enum key
     * @return rightsEnums enum
     */

    public static MonthTypeEnum getFromId(Integer id )
    {
        return MAP.get(id);
    }
    public static MonthTypeEnum getFromType(String type)
    {
        return  MonthTypeEnum.valueOf(type.toUpperCase());
    }

}
