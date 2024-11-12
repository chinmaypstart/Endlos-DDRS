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
package com.endlosiot.common.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is Activation view which used to send activation by user details in json
 * format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
@JsonInclude(Include.NON_NULL)
public abstract class ActivationView extends AuditableView {

    private static final long serialVersionUID = 4528679895572287481L;

    private Boolean active;
    private String activationDate;
    private String activationBy;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    public String getActivationBy() {
        return activationBy;
    }

    public void setActivationBy(String activationBy) {
        this.activationBy = activationBy;
    }
}
