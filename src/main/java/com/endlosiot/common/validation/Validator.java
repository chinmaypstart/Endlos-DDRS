/*******************************************************************************
 * Copyright -2018 intentlabs
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

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a common data type wise validator.
 *
 * @author Core Team
 * @since 10/11/2021
 */
public enum Validator implements Serializable {

    STRING("STRING") {
        @Override
        public void isValid(InputField inputField) throws EndlosiotAPIException {
            if (inputField.isMandatory() && StringUtils.isBlank(inputField.getValue())) {
                throw new EndlosiotAPIException(
                        ResponseCode.valueOf(inputField.getName() + "_IS_MISSING").getCode(),
                        ResponseCode.valueOf(inputField.getName() + "_IS_MISSING").getMessage());
            }
            if (!StringUtils.isBlank(inputField.getValue())) {
                if (inputField.getMinLength() != 0 && inputField.getValue().length() < inputField.getMinLength()) {
                    throw new EndlosiotAPIException(
                            ResponseCode.valueOf(inputField.getName() + "_MIN_LENGTH_EXCEED").getCode(),
                            ResponseCode.valueOf(inputField.getName() + "_MIN_LENGTH_EXCEED").getMessage());
                }
                if (inputField.getMaxLength() != 0 && inputField.getValue().length() > inputField.getMaxLength()) {
                    throw new EndlosiotAPIException(
                            ResponseCode.valueOf(inputField.getName() + "_MAX_LENGTH_EXCEED").getCode(),
                            ResponseCode.valueOf(inputField.getName() + "_MAX_LENGTH_EXCEED").getMessage());
                }
                if (inputField.getRegex() != null && inputField.getRegex().getValue() != null) {
                    Pattern pattern = Pattern.compile(inputField.getRegex().getValue());
                    Matcher matcher = pattern.matcher(inputField.getValue());
                    if (!matcher.matches()) {
                        throw new EndlosiotAPIException(
                                ResponseCode.valueOf(inputField.getName() + "_IS_INVALID").getCode(),
                                ResponseCode.valueOf(inputField.getName() + "_IS_INVALID").getMessage());
                    }
                }
            }
        }
    };

    private final String name;

    Validator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * This method is called by enum to check given field value is valid or not.
     *
     * @param inputField
     * @return
     * @throws EndlosiotAPIException
     */
    public abstract void isValid(InputField inputField) throws EndlosiotAPIException;
}