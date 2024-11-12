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
package com.endlosiot.common.location.operation;


import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.location.model.CountryModel;
import com.endlosiot.common.location.model.StateModel;
import com.endlosiot.common.location.service.CountryService;
import com.endlosiot.common.location.service.StateService;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.KeyValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class used to perform all business operation on state model.
 *
 * @author Dhruvang
 * @since 29/01/2018
 */

@Component(value = "countryOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CountryOperationImpl implements CountryOperation {

    @Autowired
    private CountryService countryService;

    @Autowired
    private StateService stateService;

    @Override
    public Response doGetAll() throws EndlosiotAPIException {
        List<KeyValueView> listCountry = new ArrayList<>();
        for (Map.Entry<Long, CountryModel> countryMap : CountryModel.getMAP().entrySet()) {
            listCountry.add(KeyValueView.create(countryMap.getValue().getId(), countryMap.getValue().getName()));

        }
        Collections.sort(listCountry, (o1, o2) -> (o1).getValue().compareTo((o2).getValue()));
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                listCountry.size(), listCountry);
    }

    @Override
    public Response doGetCode() throws EndlosiotAPIException {
        List<KeyValueView> listCountry = new ArrayList<>();
        for (Map.Entry<Long, CountryModel> countryMap : CountryModel.getMAP().entrySet()) {
            if (countryMap.getValue().getPhoneCode() != null) {
                String code = ("+" + countryMap.getValue().getPhoneCode() + " - " + countryMap.getValue().getName());
                listCountry.add(KeyValueView.create(countryMap.getValue().getPhoneCode(), code));
            }
        }
        Collections.sort(listCountry, (o1, o2) -> (o1).getValue().compareTo((o2).getValue()));
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                listCountry.size(), listCountry);
    }

    @Override
    public Response doRefreshCache() throws EndlosiotAPIException {
        CountryModel.getMAP().clear();
        CountryModel.getCountryStateMap().clear();
        StateModel.getStateCityMap().clear();

        List<CountryModel> countryModels = countryService.findAll();
        for (CountryModel countryModel : countryModels) {
            CountryModel.addCountry(new CountryModel(countryModel.getId(), countryModel.getSortName(),
                    countryModel.getName(), countryModel.getPhoneCode()));
            CountryModel.addCountryState(countryModel.getId(), countryModel.getStates());
        }

        List<StateModel> stateModelList = stateService.findAll();
        for (StateModel stateModel : stateModelList) {
            StateModel.addState(stateModel);
            StateModel.addStateCity(stateModel.getId(), stateModel.getCities());
        }
        LoggerService.debug(CountryOperationImpl.class.getName(), "Refresh Cache",
                "Country, region & city map is refreshed");
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }
}