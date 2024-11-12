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
package com.endlosiot.common.response;

/**
 * This is used to send response of form error request for any model.
 *
 * @author Vishwa Shah
 * @since 09/01/2019
 */
public class FormErrorResponse extends CommonResponse {

    private static final long serialVersionUID = 3944857319523952086L;
    private String errorName;
    private Integer step;

    protected FormErrorResponse(int responseCode, String message, String errorName, Integer step) {
        super(responseCode, message);
        this.errorName = errorName;
        this.step = step;
    }

    public static FormErrorResponse create(int responseCode, String message, String errorName, Integer step) {
        return new FormErrorResponse(responseCode, message, errorName, step);
    }

    public String getErrorName() {
        return errorName;
    }

    public Integer getStep() {
        return step;
    }
}