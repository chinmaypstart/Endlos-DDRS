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
import com.endlosiot.common.location.model.CountryModel;
import com.endlosiot.common.location.model.StateModel;
import com.endlosiot.common.location.service.CountryService;
import com.endlosiot.common.location.service.StateService;
import com.endlosiot.common.location.view.CountryView;
import com.endlosiot.common.location.view.StateView;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.view.KeyValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


/**
 * This class used to perform all business operation on state model.
 *
 * @author Nirav
 * @since 14/11/2017
 */
@Component(value = "stateOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class StateOperationImpl extends AbstractOperation<StateModel, StateView> implements StateOperation {

    @Autowired
    StateService stateService;

    @Autowired
    CountryService countryService;

    @Override
    public Response doSave(StateView stateView) throws EndlosiotAPIException {
        StateModel stateModel = getModel(stateView);
        stateService.create(stateModel);
        Set<StateModel> stateModelList = CountryModel.getCountryStateMap().get(stateView.getCountry().getId());
        stateModelList.add(stateModel);
        CountryModel.addCountryState(stateView.getCountry().getId(), stateModelList);
        StateView responseView = fromModel(stateModel);
        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage(), responseView);
    }

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        StateModel stateModel = stateService.get(id);
        if (stateModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        StateView stateView = fromModel(stateModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), stateView);
    }

    @Override
    public Response doUpdate(StateView stateView) throws EndlosiotAPIException {
        StateModel stateModel = getModel(stateView);
        if (stateModel == null) {
            return CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        stateService.update(toModel(stateModel, stateView));
        Set<StateModel> stateModels = CountryModel.getCountryStateMap().get(stateView.getCountry().getId());
        stateModels.remove(stateModel);
        stateModels.add(stateModel);
        CountryModel.addCountryState(stateView.getCountry().getId(), stateModels);
        StateView responseView = fromModel(stateModel);
        return ViewResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage(), responseView);
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        StateModel stateModel = stateService.get(id);
        if (stateModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        stateService.hardDelete(stateModel.getId());
        Set<StateModel> stateModelList = CountryModel.getCountryStateMap().get(stateModel.getCountryModel().getId());
        stateModelList.remove(stateModel);
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doSearch(StateView stateView, Integer start, Integer end) {
        return null;
    }

    @Override
    public StateModel toModel(StateModel stateModel, StateView stateView) throws EndlosiotAPIException {
        CountryModel countryModel = countryService.get(stateView.getCountry().getId());
        stateModel.setName(stateView.getName());
        stateModel.setCountryModel(countryModel);
        return stateModel;
    }

    @Override
    protected StateModel getNewModel() {
        return new StateModel();
    }

    @Override
    public StateView fromModel(StateModel stateModel) {
        StateView stateView = new StateView();
        stateView.setId(stateModel.getId());
        stateView.setName(stateModel.getName());
        if (stateModel.getCountryModel() != null) {
            CountryView countryView = new CountryView();
            countryView.setId(stateModel.getCountryModel().getId());
            countryView.setName(stateModel.getCountryModel().getName());
            stateView.setCountry(countryView);
        }
        return stateView;
    }

    @Override
    public BaseService<StateModel> getService() {
        return stateService;
    }

    @Override
    public Response doGetAll(Long countryId) throws EndlosiotAPIException {
        List<KeyValueView> stateList = new ArrayList<>();
        Set<StateModel> stateModels = CountryModel.getCountryStateMap().get(countryId);

        if (stateModels == null || stateModels.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        for (StateModel stateModel : stateModels) {
            stateList.add(KeyValueView.create(stateModel.getId(), stateModel.getName()));
        }
        Collections.sort(stateList, (o1, o2) -> (o1).getValue().compareTo((o2).getValue()));
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                stateList.size(), stateList);
    }
}