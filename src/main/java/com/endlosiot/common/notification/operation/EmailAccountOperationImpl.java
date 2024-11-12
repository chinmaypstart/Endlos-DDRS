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
import com.endlosiot.common.notification.model.EmailAccountModel;
import com.endlosiot.common.notification.service.EmailAccountService;
import com.endlosiot.common.notification.view.EmailAccountView;
import com.endlosiot.common.notification.view.EmailAccountView.EmailAccountViewBuilder;
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

/**
 * This class used to perform all business operation on program model.
 *
 * @author Nirav.Shah
 * @since 17/07/2018
 */
@Component(value = "emailAccountOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmailAccountOperationImpl extends AbstractOperation<EmailAccountModel, EmailAccountView>
        implements EmailAccountOperation {
    @Autowired
    EmailAccountService emailAccountService;

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        EmailAccountModel emailAccountModel = emailAccountService.get(id);
        if (emailAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        EmailAccountView emailAccountView = fromModel(emailAccountModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                emailAccountView);
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        EmailAccountModel emailAccountModel = emailAccountService.get(id);
        if (emailAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        emailAccountService.hardDelete(emailAccountModel.getId());
        EmailAccountModel.removeEmailAccount(emailAccountModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public EmailAccountModel toModel(EmailAccountModel emailAccountModel, EmailAccountView emailAccountView)
            throws EndlosiotAPIException {
        emailAccountModel.setName(emailAccountView.getName());
        emailAccountModel.setHost(emailAccountView.getHost());
        emailAccountModel.setPort(emailAccountView.getPort());
        emailAccountModel.setUsername(emailAccountView.getUserName());
        emailAccountModel.setPassword(emailAccountView.getPassword());
        emailAccountModel.setReplyToEmail(emailAccountView.getReplyToEmail());
        emailAccountModel.setEmailFrom(emailAccountView.getEmailFrom());
        emailAccountModel.setRatePerHour(emailAccountView.getRatePerHour());
        emailAccountModel.setUpdateRatePerHour(emailAccountView.getUpdateRatePerHour());
        emailAccountModel.setRatePerDay(emailAccountView.getRatePerDay());
        emailAccountModel.setUpdateRatePerDay(emailAccountView.getUpdateRatePerDay());
        emailAccountModel.setAuthenticationMethod(
                Integer.parseInt(String.valueOf(emailAccountView.getAuthenticationMethod().getKey())));
        emailAccountModel.setAuthenticationSecurity(
                Integer.parseInt(String.valueOf(emailAccountView.getAuthenticationSecurity().getKey())));
        if (emailAccountView.getTimeOut() == null) {
            emailAccountModel.setTimeOut(60000l);
        } else {
            emailAccountModel.setTimeOut(emailAccountView.getTimeOut());
        }
        return emailAccountModel;
    }

    @Override
    protected EmailAccountModel getNewModel() {
        return new EmailAccountModel();
    }

    @Override
    public EmailAccountView fromModel(EmailAccountModel emailAccountModel) {
        EmailAccountViewBuilder builder = new EmailAccountView.EmailAccountViewBuilder()
                .setId(emailAccountModel.getId()).setName(emailAccountModel.getName())
                .setHost(emailAccountModel.getHost()).setUserName(emailAccountModel.getUsername())
                .setPassword(emailAccountModel.getPassword()).setEmailFrom(emailAccountModel.getEmailFrom())
                .setReplyToEmail(emailAccountModel.getReplyToEmail());
        if (emailAccountModel.getPort() != null) {
            builder.setPort(emailAccountModel.getPort());
        }
        if (emailAccountModel.getRatePerHour() != null) {
            builder.setRatePerHour(emailAccountModel.getRatePerHour());
        }
        if (emailAccountModel.getUpdateRatePerHour() != null) {
            builder.setUpdateRatePerHour(emailAccountModel.getUpdateRatePerHour());
        }
        if (emailAccountModel.getRatePerDay() != null) {
            builder.setRatePerDay(emailAccountModel.getRatePerDay());
        }
        if (emailAccountModel.getUpdateRatePerDay() != null) {
            builder.setUpdateRatePerDay(emailAccountModel.getUpdateRatePerDay());
        }
        if (emailAccountModel.getAuthenticationMethod() != null) {
            builder.setAuthenticationMethod(KeyValueView.create(emailAccountModel.getAuthenticationMethod().getId(),
                    emailAccountModel.getAuthenticationMethod().getName()));
        }
        if (emailAccountModel.getAuthenticationSecurity() != null) {
            builder.setAuthenticationSecurity(KeyValueView.create(emailAccountModel.getAuthenticationSecurity().getId(),
                    emailAccountModel.getAuthenticationSecurity().getName()));
        }
        if (emailAccountModel.getTimeOut() != null) {
            builder.setTimeOut(emailAccountModel.getTimeOut());
        }
        return builder.build();
    }

    @Override
    public BaseService<EmailAccountModel> getService() {
        return emailAccountService;
    }

    @Override
    public Response doSave(EmailAccountView emailAccountView) throws EndlosiotAPIException {
        EmailAccountModel emailAccountModelExist = emailAccountService.getByName(emailAccountView.getName());
        if (emailAccountModelExist != null) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_ACCOUNT_ALREADY_EXIST.getCode(),
                    ResponseCode.EMAIL_ACCOUNT_ALREADY_EXIST.getMessage());
        }
        EmailAccountModel emailAccountModel = getModel(emailAccountView);
        emailAccountModel.setActive(true);
        emailAccountService.create(emailAccountModel);
        EmailAccountModel.addEmailAccount(emailAccountModel);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdate(EmailAccountView emailAccountView) throws EndlosiotAPIException {
        EmailAccountModel emailAccountModel = emailAccountService.get(emailAccountView.getId());
        if (emailAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        if (!emailAccountModel.getName().equals(emailAccountView.getName())) {
            EmailAccountModel emailAccountModelExist = emailAccountService.getByName(emailAccountView.getName());
            if (emailAccountModelExist != null) {
                throw new EndlosiotAPIException(ResponseCode.EMAIL_ACCOUNT_ALREADY_EXIST.getCode(),
                        ResponseCode.EMAIL_ACCOUNT_ALREADY_EXIST.getMessage());
            }
        }
        emailAccountService.update(toModel(emailAccountModel, emailAccountView));
        EmailAccountModel.updateEmailAccount(emailAccountModel);
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doSearch(EmailAccountView emailAccountView, Integer start, Integer recordSize) {
        PageModel pageModel = emailAccountService.searchByLight(emailAccountView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                pageModel.getRecords(), fromModelList((List<EmailAccountModel>) pageModel.getList()));
    }

    @Override
    public Response doDropdown() {
        EmailAccountView emailAccountView = new EmailAccountView.EmailAccountViewBuilder().build();
        PageModel pageModel = emailAccountService.searchByLight(emailAccountView, null, null);
        List<KeyValueView> keyValueViews = new ArrayList<>();
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        for (EmailAccountModel emailAccountModel : (List<EmailAccountModel>) pageModel.getList()) {
            keyValueViews.add(KeyValueView.create(emailAccountModel.getId(), emailAccountModel.getName()));
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                keyValueViews.size(), keyValueViews);
    }
}