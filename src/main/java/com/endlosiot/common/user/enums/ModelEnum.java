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

import java.io.Serializable;

/**
 * This is enum interface which maps database enum to java enums.
 *
 * @author Nirav Shah
 * @since 25/12/2019
 */
public interface ModelEnum extends Serializable {

    /**
     * This method returns id of enum
     *
     * @return id of enum
     */
    public Integer getId();

    /**
     * This method returns name of enum
     *
     * @return name of enum
     */
    public String getName();
}
