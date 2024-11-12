package com.endlosiot.common.user.enums;

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
public enum RoleTypeEnum implements EnumType {
    MASTER_ADMIN(1, "Master Admin"), CLIENT_ADMIN(2, "Client Admin"), CLIENT_USER(3, "Client User");

    private final int id;
    private final String name;
    public static final Map<Integer, RoleTypeEnum> MAP = new HashMap<>();

    static {
        for (RoleTypeEnum groupEnum : values()) {
            MAP.put(groupEnum.getId(), groupEnum);
        }
    }

    RoleTypeEnum(int id, String name) {
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
    public static RoleTypeEnum fromId(Integer id) {
        return MAP.get(id);
    }

    public static RoleTypeEnum fromType(String type) {
        return RoleTypeEnum.valueOf(type.toUpperCase());
    }
}
