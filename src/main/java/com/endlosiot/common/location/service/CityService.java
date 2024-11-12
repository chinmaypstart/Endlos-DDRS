/*******************************************************************************
 * Copyright -2017 @intentlabs
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
package com.endlosiot.common.location.service;


import com.endlosiot.common.location.model.CityModel;
import com.endlosiot.common.service.BaseService;

/**
 * @author Nirav
 * @since 10/11/2017
 */
public interface CityService extends BaseService<CityModel> {

    /**
     * This method is used to find by name.
     *
     * @param name
     * @param stateId
     * @return
     */
    CityModel findByName(String name, Long stateId);

    CityModel load(Long key);
}
