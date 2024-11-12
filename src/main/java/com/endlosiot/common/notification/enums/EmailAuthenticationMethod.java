package com.endlosiot.common.notification.enums;

import com.endlosiot.common.user.enums.ModelEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum defined email server authentication method.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */
public enum EmailAuthenticationMethod implements ModelEnum {

    PLAIN(0, "PLAIN"), LOGIN(1, "LOGIN"), CRAM_MD5(2, "CRAM MD5");

    private final Integer id;
    private final String name;

    public static final Map<Integer, EmailAuthenticationMethod> MAP = new HashMap<>();

    static {
        for (EmailAuthenticationMethod mailAuthenticationMethod : values()) {
            MAP.put(mailAuthenticationMethod.getId(), mailAuthenticationMethod);
        }
    }

    EmailAuthenticationMethod(Integer id, String name) {
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

    public static EmailAuthenticationMethod fromId(Integer id) {
        return MAP.get(id);
    }
}
