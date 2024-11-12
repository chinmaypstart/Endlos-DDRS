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

import java.util.HashMap;
import java.util.Map;

/**
 * Regex that can be used to validate field value.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
public enum RegexEnum {

    DOMAIN_NAME_VALIDATION_REGEX(1, "[a-zA-Z0-9-]+", "Domain Name is Invalid"),
    ALPHA_NUMERIC_WITH_SPACE(2, "^[a-zA-Z0-9\\s]+$", "Alphabets, numbers & space are allowed"),
    NUMERIC(3, "^[0-9]+$", "Only numbers are allowed"),
    EMAIL(4, "^[a-z0-9](\\.?[a-z0-9_-]){0,}@[a-z0-9-]+\\.([a-z0-9]{1,}\\.){0,}?[a-z0-9]{2,}$", "Invalid email id"),
    ALPHABETS_WITH_SPACE_DOT(5, "^[A-Za-z.\\s]+$", "Alphabets, space & dots are allowed"),
    MOBILE_NUMBER(6, "^$|^[0-9]{5,12}$", "Invalid Mobile no"),
    PIN_CODE(7, "\\b\\d{6}\\b", "Invalid Pincode"),
    WEBSITE(8,
            "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$",
            "Invalid Website"),
    ALPHA_NUMERIC_WITH_HYPEN(9, "^[a-zA-Z0-9-]+$", "Alphabets, numbers & hypen are allowed"),
    PHONE(10, "^[- +()0-9]+$", "Phone number is invalid"), ALPHA_NUMERIC(11, "^[A-Za-z/\\d]+$", "Alpha Numeric"),
    DD_MM_YYYY(12, "dd/MM/yyyy", "date Format"),
    ALPHA_NUMERIC_WITH_SPACE_HYPEN(13, "^[a-zA-Z0-9-\\s]+$", "Alphabets, numbers, space & hypen are allowed"),
    ALBHABETS(14, "^[a-zA-Z]+$", "Only Alphabets are allowed"),
    ALBHABETS_WITH_SPACE_DOT_HYPEN_APOSTROPHE(14, "^[a-zA-Z-'.\\s]+$", "Alphabets, space, dot, hypen & Apostrophe are allowed"),
    ;

    private final int id;
    private final String value;
    private final String message;
    private static Map<Integer, RegexEnum> MAP = new HashMap<>();

    RegexEnum(int id, String value, String message) {
        this.id = id;
        this.value = value;
        this.message = message;
    }

    static {
        for (RegexEnum regexEnum : values()) {
            MAP.put(regexEnum.getId(), regexEnum);
        }
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    /**
     * This methods is used to fetch Enum base on given id.
     *
     * @param id enum key
     * @return RegexEnum enum
     */
    public static RegexEnum fromId(int id) {
        return MAP.get(id);
    }

}
