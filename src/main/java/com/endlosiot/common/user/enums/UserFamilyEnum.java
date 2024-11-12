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
 * This is User Gender enum which represent user gender title.
 *
 * @author Nirav.Shah
 * @since 12/08/2018
 */
public enum UserFamilyEnum implements EnumType {
    SPOUSE(1, "Spouse"), SON(2, "Son"), DAUGHTER(3, "Daughter"), FATHER(4, "Father"), MOTHER(5, "Mother"),
    BROTHER(6, "Brother"), SISTER(7, "Sister");

    private final int id;
    private final String name;
    public static final Map<Integer, UserFamilyEnum> MAP = new HashMap<>();

    static {
        for (UserFamilyEnum userGenderEnum : values()) {
            MAP.put(userGenderEnum.getId(), userGenderEnum);
        }
    }

    UserFamilyEnum(int id, String name) {
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
    public static UserFamilyEnum fromId(Integer id) {
        return MAP.get(id);
    }
}
