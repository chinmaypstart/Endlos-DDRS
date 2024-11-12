package com.endlosiot.common.notification.enums;

import com.endlosiot.common.user.enums.ModelEnum;

import java.util.HashMap;
import java.util.Map;

public enum ProviderType implements ModelEnum {
    SSL_SECURE(1, "SSL Secure"), TLS_SECURE(2, "TLS Secure");

    private final Integer id;
    private final String name;

    public static final Map<Integer, ProviderType> MAP = new HashMap<>();

    static {
        for (ProviderType providerType : values()) {
            MAP.put(providerType.getId(), providerType);
        }
    }

    ProviderType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static ProviderType fromId(Integer id) {
        return MAP.get(id);
    }
}
