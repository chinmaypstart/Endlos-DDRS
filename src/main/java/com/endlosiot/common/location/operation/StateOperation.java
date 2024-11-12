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
package com.endlosiot.common.location.operation;


import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.location.view.StateView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;

/**
 * @author Nirav
 * @since 14/11/2017
 */
public interface StateOperation extends BaseOperation<StateView> {
    /**
     * This method is used to prepare state drop down.
     *
     * @param countryId
     * @return
     * @throws EndlosiotAPIException
     */
    Response doGetAll(Long countryId) throws EndlosiotAPIException;
}
