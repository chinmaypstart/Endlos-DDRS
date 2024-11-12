package com.endlosiot.common.notification.enums;

import com.endlosiot.common.user.enums.ModelEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum defined email server security method.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public enum EmailAuthenticationSecurity implements ModelEnum {

    NONE(0, "None"), SSL(1, "USE SSL"), TLS(2, "USE TLS");

    private final Integer id;
    private final String name;

    EmailAuthenticationSecurity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static final Map<Integer, EmailAuthenticationSecurity> MAP = new HashMap<>();

    static {
        for (EmailAuthenticationSecurity mailAuthenticationSecurity : values()) {
            MAP.put(mailAuthenticationSecurity.getId(), mailAuthenticationSecurity);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EmailAuthenticationSecurity fromId(Integer id) {
        return MAP.get(id);
    }
}
