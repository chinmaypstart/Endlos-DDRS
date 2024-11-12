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
package com.endlosiot.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * This is JWT token enum which represent jwt token of system.
 *
 * @author Core Team
 * @since 13/11/2021
 */
public enum JWTExceptionEnum implements EnumType {

    SIGNAUTURE_EXCEPTION(1, "Signature Exception"), EXPIRED_JWT_EXCEPTION(2, "Expired Jwt Exception"),
    NO_EXCEPTION(3, "No Exception"),
    ;

    private final int id;
    private final String name;

    private static final Map<Integer, JWTExceptionEnum> MAP = new HashMap<>();

    static {
        for (JWTExceptionEnum moduleEnums : values()) {
            MAP.put(moduleEnums.getId(), moduleEnums);
        }
    }

    JWTExceptionEnum(int id, String name) {
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
    public static JWTExceptionEnum fromId(Integer id) {
        return MAP.get(id);
    }
}