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

/**
 * This class is used to set fields values & the same will be validated against
 * their data types.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
public class InputField {

    private String name;
    private String value;
    private boolean isMandatory;
    private int minLength;
    private int maxLength;
    private RegexEnum regex;

    public InputField(String name, String value, boolean isMandatory, int minLength, int maxLength, RegexEnum regex) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.regex = regex;
    }

    public InputField(String name, String value, boolean isMandatory, int maxLength, RegexEnum regex) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
        this.maxLength = maxLength;
        this.regex = regex;
    }

    public InputField(String name, String value, boolean isMandatory, RegexEnum regex) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
        this.regex = regex;
    }

    public InputField(String name, String value, boolean isMandatory, int maxLength) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
        this.maxLength = maxLength;
    }

    public InputField(String name, String value, boolean isMandatory, int minLength, int maxLength) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
        this.maxLength = maxLength;
        this.minLength = minLength;
    }

    public InputField(String name, String value, boolean isMandatory) {
        super();
        this.name = name;
        this.value = value;
        this.isMandatory = isMandatory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public RegexEnum getRegex() {
        return regex;
    }

    public void setRegex(RegexEnum regex) {
        this.regex = regex;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name;
    }
}