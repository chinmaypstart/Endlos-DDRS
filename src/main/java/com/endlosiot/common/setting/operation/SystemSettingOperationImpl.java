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
package com.endlosiot.common.setting.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.modelenums.CommonStatusEnum;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.setting.service.SystemSettingService;
import com.endlosiot.common.setting.view.SystemSettingView;
import com.endlosiot.common.validation.DataType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This class used to perform all business operation on city model.
 *
 * @author Nirav
 * @since 14/11/2018
 */
@Component(value = "systemSettingOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SystemSettingOperationImpl implements SystemSettingOperation {

    @Autowired
    SystemSettingService systemSettingService;

    @Override
    public SystemSettingService getService() {
        return systemSettingService;
    }

    @Override
    public Response doView(String key) throws EndlosiotAPIException {
        SystemSettingModel systemSettingModel = systemSettingService.get(key);
        if (systemSettingModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        SystemSettingView systemSettingView = fromModel(systemSettingModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                systemSettingView);
    }

    @Override
    public Response doEdit(String key) throws EndlosiotAPIException {
        return doView(key);
    }

    @Override
    public Response doUpdate(SystemSettingView systemSettingView) throws EndlosiotAPIException {
        SystemSettingModel systemSettingModel = systemSettingService.get(systemSettingView.getKey());
        if (systemSettingModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        systemSettingService.update(toModel(systemSettingModel, systemSettingView));
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdateBulk(List<SystemSettingView> systemSettingViews) throws EndlosiotAPIException {
        for (SystemSettingView systemSettingView : systemSettingViews) {
            if (!StringUtils.isBlank(SystemSettingModel.get(systemSettingView.getKey()))) {
                SystemSettingModel systemSettingModel = systemSettingService.get(systemSettingView.getKey());
                validateSettings(systemSettingModel, systemSettingView);
                systemSettingService.update(toModel(systemSettingModel, systemSettingView));
            } else {
                throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                        ResponseCode.NO_DATA_FOUND.getMessage() + " for " + systemSettingView.getKey());
            }
        }
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    private void validateSettings(SystemSettingModel systemSettingModel, SystemSettingView systemSettingView)
            throws EndlosiotAPIException {
        ResponseCode responseCode = ResponseCode.valueOf(systemSettingModel.getKey() + "_IS_INVALID");
        if (DataType.INT.equals(systemSettingModel.getDataType())
                || DataType.BOOLEAN.equals(systemSettingModel.getDataType())) {
            try {
                new Integer(systemSettingView.getValue());
            } catch (Exception e) {
                throw new EndlosiotAPIException(responseCode.getCode(), responseCode.getMessage());
            }
        }

        if (DataType.BOOLEAN.equals(systemSettingModel.getDataType())
                && CommonStatusEnum.fromId(Integer.valueOf(systemSettingView.getValue())) == null) {
            throw new EndlosiotAPIException(responseCode.getCode(), responseCode.getMessage());
        }

        if (DataType.STRING.equals(systemSettingModel.getDataType())
                && StringUtils.isBlank(systemSettingView.getValue())) {
            throw new EndlosiotAPIException(responseCode.getCode(), responseCode.getMessage());
        }
    }

    @Override
    public Response doSearch(SystemSettingView systemSettingView, Integer start, Integer recordSize)
            throws EndlosiotAPIException {
        List<SystemSettingView> systemSettingViews = new ArrayList<>();

        List<SystemSettingModel> systemSettingModels = systemSettingService.findAll();
        for (SystemSettingModel systemSettingModel : systemSettingModels) {
            systemSettingViews.add(fromModel(systemSettingModel));
        }

        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                (long) systemSettingViews.size(), systemSettingViews);
    }

    @Override
    public SystemSettingModel toModel(SystemSettingModel systemSettingModel, SystemSettingView systemSettingView) {
        systemSettingModel.setValue(systemSettingView.getValue());
        return systemSettingModel;
    }

    @Override
    public SystemSettingModel getModel(SystemSettingView systemSettingView) {
        return toModel(getNewModel(), systemSettingView);
    }

    @Override
    public SystemSettingModel getNewModel() {
        return new SystemSettingModel();
    }

    @Override
    public SystemSettingView fromModel(SystemSettingModel systemSettingModel) {
        SystemSettingView systemSettingView = new SystemSettingView();
        systemSettingView.setKey(systemSettingModel.getKey());
        systemSettingView.setValue(systemSettingModel.getValue());
        return systemSettingView;
    }

    @Override
    public List<SystemSettingView> fromModelList(List<SystemSettingModel> systemSettingModels) {
        List<SystemSettingView> systemSettingViews = new ArrayList<>(systemSettingModels.size());
        for (SystemSettingModel systemSettingModel : systemSettingModels) {
            systemSettingViews.add(fromModel(systemSettingModel));
        }
        return systemSettingViews;
    }

    @Override
    public void doRefreshMap(List<SystemSettingView> systemSettingViews) throws EndlosiotAPIException {
        for (SystemSettingView systemSettingView : systemSettingViews) {
            SystemSettingModel systemSettingModel = systemSettingService.get(systemSettingView.getKey());
            SystemSettingModel.add(systemSettingModel);
        }
    }
}