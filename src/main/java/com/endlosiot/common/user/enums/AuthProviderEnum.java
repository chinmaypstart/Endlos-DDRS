/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.user.enums;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

/**
 * This is Auth Provider enum which represent authprovider.
 *
 * @author Nirav.Shah
 * @since 12/08/2018
 */
public enum AuthProviderEnum implements EnumType {
    GOOGLE(1, "GOOGLE"), FACEBOOK(2, "FACEBOOK"), LINKEDIN(3, "LINKEDIN");

    private final int id;
    private final String name;
    private static final Map<Integer, AuthProviderEnum> MAP = new HashMap<>();
    private static final Map<String, AuthProviderEnum> valueMAP = new HashMap<>();

    static {
        for (AuthProviderEnum authProviderEnum : values()) {
            MAP.put(authProviderEnum.getId(), authProviderEnum);
            valueMAP.put(authProviderEnum.getName(), authProviderEnum);
        }
    }

    AuthProviderEnum(int id, String name) {
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
    public static AuthProviderEnum fromId(Integer id) {
        return MAP.get(id);
    }

    /**
     * This methods is used to fetch Enum base on given name.
     *
     * @param name enum value
     */
    public static AuthProviderEnum fromName(String name) {
        return valueMAP.get(name);
    }
}