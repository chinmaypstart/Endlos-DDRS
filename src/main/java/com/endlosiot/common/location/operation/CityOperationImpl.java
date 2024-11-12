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

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.location.model.CityModel;
import com.endlosiot.common.location.model.StateModel;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.KeyValueView;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * This class used to perform all business operation on city model.
 *
 * @author Nirav
 * @since 14/11/2017
 */
@Component(value = "cityOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CityOperationImpl implements CityOperation {

    @Override
    public Response doGetAll(Long stateId) throws EndlosiotAPIException {
        List<KeyValueView> cityList = new ArrayList<>();
        Set<CityModel> cityModels = StateModel.getStateCityMap().get(stateId);

        if (cityModels == null || cityModels.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        for (CityModel cityModel : cityModels) {
            cityList.add(KeyValueView.create(cityModel.getId(), cityModel.getName()));
        }
        Collections.sort(cityList, (o1, o2) -> (o1).getValue().compareTo((o2).getValue()));
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                cityList.size(), cityList);
    }
}