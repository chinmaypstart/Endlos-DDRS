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
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.notification.model.EmailAccountModel;
import com.endlosiot.common.notification.model.EmailContentModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.service.EmailAccountService;
import com.endlosiot.common.notification.service.EmailContentService;
import com.endlosiot.common.notification.view.EmailContentView;
import com.endlosiot.common.notification.view.EmailContentView.EmailContentViewBuilder;
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

import java.util.Collections;
import java.util.List;

/**
 * This class used to perform all business operation on program model.
 *
 * @author Nisha.Panchal
 * @since 23/07/2018
 */

@Component(value = "emailContentOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmailContentOperationImpl extends AbstractOperation<EmailContentModel, EmailContentView>
        implements EmailContentOperation {
    @Autowired
    EmailContentService emailContentService;
    @Autowired
    EmailAccountService emailAccountService;
    @Autowired
    EmailAccountOperationImpl emailAccountOperation;

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        EmailContentModel emailContentModel = emailContentService.get(id);
        if (emailContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        EmailContentView emailContentView = fromModel(emailContentModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                emailContentView);
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        EmailContentModel emailContentModel = emailContentService.get(id);
        if (emailContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        emailContentService.hardDelete(emailContentModel.getId());
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public EmailContentModel toModel(EmailContentModel emailContentModel, EmailContentView emailContentView) {
        emailContentModel.setSubject(emailContentView.getSubject());
        emailContentModel.setContent(emailContentView.getContent());
        emailContentModel.setEmailBcc(emailContentView.getEmailBcc());
        emailContentModel.setEmailCc(emailContentView.getEmailCc());
        return emailContentModel;
    }

    @Override
    protected EmailContentModel getNewModel() {
        return new EmailContentModel();
    }

    @Override
    public EmailContentView fromModel(EmailContentModel emailContentModel) {
        EmailContentViewBuilder builder = new EmailContentView.EmailContentViewBuilder()
                .setId(emailContentModel.getId()).setSubject(emailContentModel.getSubject())
                .setContent(emailContentModel.getContent()).setEmailBcc(emailContentModel.getEmailBcc())
                .setEmailCc(emailContentModel.getEmailCc())
                .setNotificationView(KeyValueView.create(emailContentModel.getNotificationModel().getId(),
                        emailContentModel.getNotificationModel().getName()));
        if (emailContentModel.getEmailAccountId() != null) {
            EmailAccountModel emailAccountModel = EmailAccountModel.getMAP().get(emailContentModel.getEmailAccountId());
            if (emailAccountModel == null) {
                emailAccountModel = emailAccountService.get(emailContentModel.getEmailAccountId());
            }
            builder.setEmailAccountView(KeyValueView.create(emailAccountModel.getId(), emailAccountModel.getName()));
        }
        return builder.build();
    }

    @Override
    public BaseService<EmailContentModel> getService() {
        return emailContentService;
    }

    @Override
    public Response doSave(EmailContentView emailContentView) throws EndlosiotAPIException {
        EmailContentModel emailContentModel = emailContentService
                .findByNotification(NotificationModel.getMAP().get(emailContentView.getNotificationView().getKey()));
        if (emailContentModel != null) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getCode(),
                    ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getMessage());
        }
        emailContentModel = getModel(emailContentView);
        EmailAccountModel emailAccountModel = emailAccountService.get(emailContentView.getEmailAccountView().getKey());
        if (emailAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_ACCOUNT_IS_INVALID.getCode(),
                    ResponseCode.EMAIL_ACCOUNT_IS_INVALID.getMessage());
        }
        emailContentModel.setEmailAccountId(emailAccountModel.getId());
        emailContentModel
                .setNotificationModel(NotificationModel.getMAP().get(emailContentView.getNotificationView().getKey()));
        emailContentService.create(emailContentModel);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdate(EmailContentView emailContentView) throws EndlosiotAPIException {
        EmailContentModel emailContentModel = emailContentService.get(emailContentView.getId());
        if (emailContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        if (!emailContentModel.getNotificationModel().getId().equals(Long.valueOf(
                NotificationEnum.fromId(emailContentView.getNotificationView().getKey().intValue()).getId()))) {
            EmailContentModel tempEmailContentModel = emailContentService.findByNotification(
                    NotificationModel.getMAP().get(emailContentView.getNotificationView().getKey()));
            if (tempEmailContentModel != null) {
                throw new EndlosiotAPIException(ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getCode(),
                        ResponseCode.EMAIL_CONTENT_ALREADY_EXIST.getMessage());
            }
        }
        EmailAccountModel emailAccountModel = emailAccountService.get(emailContentView.getEmailAccountView().getKey());
        if (emailAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_ACCOUNT_IS_INVALID.getCode(),
                    ResponseCode.EMAIL_ACCOUNT_IS_INVALID.getMessage());
        }
        emailContentModel.setEmailAccountId(emailAccountModel.getId());
        emailContentModel
                .setNotificationModel(NotificationModel.getMAP().get(emailContentView.getNotificationView().getKey()));
        emailContentService.update(toModel(emailContentModel, emailContentView));
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doSearch(EmailContentView emailContentView, Integer start, Integer recordSize) {
        PageModel result = emailContentService.searchLight(emailContentView, start, recordSize);
        if (result.getRecords() == 0) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.EMPTY_LIST);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                result.getRecords(), fromModelList((List<EmailContentModel>) result.getList()));
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }
}