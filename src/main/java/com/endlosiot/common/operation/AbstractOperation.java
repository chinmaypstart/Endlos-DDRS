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
package com.endlosiot.common.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.Model;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.view.View;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is provide transaction wrapper ,Actual transaction begin over here
 * contains common operation and list of abstract method
 *
 * @param <M>
 * @param <V>
 * @author Nirav.Shah
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public abstract class AbstractOperation<M extends Model, V extends View> {

    /**
     * This method is used to save entity
     *
     * @param request
     * @return
     * @throws EndlosiotAPIException
     */
    public Response doSave(V view) throws EndlosiotAPIException {
        Model model = getModel(view);
        getService().create(model);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    /**
     * This method is used to view entity.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    public abstract Response doView(Long id) throws EndlosiotAPIException;

    /**
     * This method is used to update entity
     *
     * @param view
     * @return
     * @throws EndlosiotAPIException
     */
    public Response doUpdate(V view) throws EndlosiotAPIException {
        M model = getModel(view);
        if (model == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        getService().update(toModel(model, view));
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    /**
     * This method is used delete existing data.
     *
     * @param id
     * @return
     */
    public abstract Response doDelete(Long id) throws EndlosiotAPIException;

    /**
     * This method is used active/inactive existing data.
     *
     * @param id
     * @return
     */
    public abstract Response doActiveInActive(Long id) throws EndlosiotAPIException;

    /**
     * This method used for search data base on queries.
     *
     * @param start
     * @param view
     * @param recordSize
     * @return
     */
    public Response doSearch(V view, Integer start, Integer recordSize) {
        PageModel result = getService().search(view, start, recordSize);
        if (result.getRecords() == 0) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                result.getRecords(), fromModelList((List<M>) result.getList()));
    }

    /**
     * This method is used to prepare model from view.
     *
     * @param model
     * @param view
     * @return
     */
    public abstract M toModel(M model, V view) throws EndlosiotAPIException;

    /**
     * This method is used to prepare model from view.
     *
     * @param request
     * @return
     */
    protected M getModel(V view) throws EndlosiotAPIException {
        return toModel(getNewModel(), view);
    }

    /**
     * This method used when require new model for view
     *
     * @param view view of model
     * @return model
     */
    protected abstract M getNewModel();

    /**
     * This method used when need to convert model to view
     *
     * @param model
     * @return view
     */
    public abstract V fromModel(M model);

    /**
     * This method convert list of model to list of view
     *
     * @param models list of model
     * @return list of view
     */
    public List<V> fromModelList(List<M> models) {
        List<V> views = new ArrayList<>(models.size());
        for (M model : models) {
            views.add(fromModel(model));
        }
        return views;
    }

    /**
     * This method use for get Service with respected operation
     *
     * @return BaseJPAService
     */
    public abstract BaseService getService();
}
