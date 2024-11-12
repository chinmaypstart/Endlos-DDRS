/*******************************************************************************
 * Copyright -2018 @intentlabs
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
package com.endlosiot.common.notification.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.service.NotificationService;
import com.endlosiot.common.notification.view.NotificationView;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * This class used to perform all business operation on notification.
 *
 * @author Nirav.Shah
 * @since 23/05/2020
 */

@Component(value = "notificationOperationImpl")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class NotificationOperationImpl extends AbstractOperation<NotificationModel, NotificationView>
        implements NotificationOperation {
    @Autowired
    NotificationService notificationService;

    @Override
    public NotificationModel toModel(NotificationModel notificationModel, NotificationView notificationView) {
        notificationModel.setEmail(notificationView.isEmail());
        notificationModel.setPush(notificationView.isPush());
        return notificationModel;
    }

    @Override
    protected NotificationModel getNewModel() {
        return new NotificationModel();
    }

    @Override
    public NotificationView fromModel(NotificationModel notificationModel) {
        return new NotificationView.NotificationViewBuilder().setId(notificationModel.getId())
                .setEmail(notificationModel.isEmail()).setName(notificationModel.getName())
                .setPush(notificationModel.isPush()).build();
    }

    @Override
    public BaseService<NotificationModel> getService() {
        return notificationService;
    }

    @Override
    public Response doUpdate(NotificationView notificationView) throws EndlosiotAPIException {
        NotificationModel notificationModel = notificationService.get(notificationView.getId());
        notificationService.update(toModel(notificationModel, notificationView));
        NotificationModel.getMAP().put(notificationModel.getId(), notificationModel);
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doSearch(NotificationView notificationView, Integer start, Integer recordSize) {
        PageModel pageModel = notificationService.search(notificationView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                pageModel.getRecords(), fromModelList((List<NotificationModel>) pageModel.getList()));
    }

    @Override
    public Response doSave(NotificationView view) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        return null;
    }
}