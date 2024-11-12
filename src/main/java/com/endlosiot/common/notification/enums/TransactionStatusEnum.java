package com.endlosiot.common.notification.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum is used to maintain email/sms/push notification status.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public enum TransactionStatusEnum implements EnumType {

    NEW(0, "NEW"), INPROCESS(1, "INPROCESS"), FAILED(2, "FAILED"), SENT(3, "SENT");

    private final int id;
    private final String name;

    public static final Map<Integer, TransactionStatusEnum> MAP = new HashMap<>();

    static {
        for (TransactionStatusEnum status : values()) {
            MAP.put(status.getId(), status);
        }
    }

    TransactionStatusEnum(Integer id, String name) {
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

    public static TransactionStatusEnum fromId(Integer status) {
        return MAP.get(status);
    }

}
