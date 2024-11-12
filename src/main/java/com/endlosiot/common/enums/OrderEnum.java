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
 * This is User Gender enum which represent user gender title.
 *
 * @author Nirav.Shah
 * @since 12/08/2018
 */
public enum OrderEnum implements EnumType {
    ASCENDING(1, "Ascending"), DESCENDING(2, "Descending");

    private final int id;
    private final String name;
    private static final Map<Integer, OrderEnum> MAP = new HashMap<>();

    static {
        for (OrderEnum orderEnum : values()) {
            MAP.put(orderEnum.getId(), orderEnum);
        }
    }

    OrderEnum(int id, String name) {
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
     * @return orderEnum enum
     */
    public static OrderEnum fromId(Integer id) {
        return MAP.get(id);
    }
}
