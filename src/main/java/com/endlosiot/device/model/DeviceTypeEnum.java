package com.endlosiot.device.model;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

/**
 * This is Role type enum which is used to idenfity basic roles into system. If
 * role with one role type is already created then system should not allow to
 * create the same role again.
 *
 * @author Nirav.Shah
 * @since 28/03/2019
 */
public enum DeviceTypeEnum implements EnumType {
    LEVEL_SENSOR(1, "Level Sensor"),TURBODITY_SENSOR(2, "Turbidity Sensor"),
    CHLORINE_SENSOR(3,"Chlorine Sensor"),PRESSURE_TRANSMITTER(4,"Pressure Transmitter"),
    FLOWMETER(5,"Flow meter");

    private final int id;
    private final String name;
    public static final Map<Integer, DeviceTypeEnum> MAP = new HashMap<>();

    static {
        for (DeviceTypeEnum groupEnum : values()) {
            MAP.put(groupEnum.getId(), groupEnum);
        }
    }

    DeviceTypeEnum(int id, String name) {
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
    public static DeviceTypeEnum fromId(Integer id) {
        return MAP.get(id);
    }

    public static DeviceTypeEnum fromType(String type) {
        return DeviceTypeEnum.valueOf(type.toUpperCase());
    }
}
