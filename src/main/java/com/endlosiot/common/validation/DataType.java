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
package com.endlosiot.common.validation;

import com.endlosiot.common.enums.EnumType;

import java.util.HashMap;
import java.util.Map;

/**
 * Types of data type.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */

public enum DataType implements EnumType {

    INT(1, "Int"), STRING(2, "String"), DATE(3, "Date"), FLOAT(5, "Float"), LONG(6, "Long"), TIME(7, "Time"),
    BOOLEAN(8, "Boolean");

    private final int id;
    private final String name;
    private static Map<Integer, DataType> MAP = new HashMap<>();

    DataType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    static {
        for (DataType dataType : values()) {
            MAP.put(dataType.getId(), dataType);
        }
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
     * @return DataType enum
     */
    public static DataType getFromId(int id) {
        return MAP.get(id);
    }
}