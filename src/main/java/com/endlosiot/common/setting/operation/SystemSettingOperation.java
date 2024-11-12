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

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.setting.service.SystemSettingService;
import com.endlosiot.common.setting.view.SystemSettingView;

import java.util.List;

/*
 * @author Nirav
 * @since 24/11/2018
 */
public interface SystemSettingOperation {
    /**
     * This method is used to view entity.
     *
     * @param key
     * @return
     * @throws EndlosiotAPIException
     */
    Response doView(String key) throws EndlosiotAPIException;

    /**
     * This method is used to edit entity.
     *
     * @param key
     * @return
     * @throws EndlosiotAPIException
     */
    Response doEdit(String key) throws EndlosiotAPIException;

    /**
     * This method is used to update entity
     *
     * @param systemSettingView
     * @return
     * @throws EndlosiotAPIException
     */
    Response doUpdate(SystemSettingView systemSettingView) throws EndlosiotAPIException;

    /**
     * This method is used to update entity
     *
     * @param systemSettingViews
     * @return
     * @throws EndlosiotAPIException
     */
    Response doUpdateBulk(List<SystemSettingView> systemSettingViews) throws EndlosiotAPIException;

    /**
     * This method used for search data base on queries.
     *
     * @param systemSettingView
     * @param start
     * @param view
     * @param recordSize
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSearch(SystemSettingView systemSettingView, Integer start, Integer recordSize)
            throws EndlosiotAPIException;

    /**
     * This method is used to prepare model from view.
     *
     * @param systemSettingModel
     * @param systemSettingView
     * @return
     */
    SystemSettingModel toModel(SystemSettingModel systemSettingModel, SystemSettingView systemSettingView);

    /**
     * This method is used to prepare model from view.
     *
     * @param systemSettingView
     * @return
     */
    SystemSettingModel getModel(SystemSettingView systemSettingView);

    /**
     * This method used when your require to create a new object of system setting
     * model.
     *
     * @return model
     */
    SystemSettingModel getNewModel();

    /**
     * This method used when need to convert model to view
     *
     * @param systemSettingModel
     * @return {@link SystemSettingView}
     */
    SystemSettingView fromModel(SystemSettingModel systemSettingModel);

    /**
     * This method convert list of model to list of view
     *
     * @param systemSettingModels list of model
     * @return {@link SystemSettingView}
     */
    List<SystemSettingView> fromModelList(List<SystemSettingModel> systemSettingModels);

    /**
     * This method use for get Service with respected operation
     *
     * @return SettingService
     */
    SystemSettingService getService();

    /**
     * This method is used to update/refresh with multiple keys.
     *
     * @throws EndlosiotAPIException
     */
    void doRefreshMap(List<SystemSettingView> systemSettingViews) throws EndlosiotAPIException;
}
