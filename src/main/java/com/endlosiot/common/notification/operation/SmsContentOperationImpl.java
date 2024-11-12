package com.endlosiot.common.notification.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.model.SmsAccountModel;
import com.endlosiot.common.notification.model.SmsContentModel;
import com.endlosiot.common.notification.service.SmsAccountService;
import com.endlosiot.common.notification.service.SmsContentService;
import com.endlosiot.common.notification.view.SmsContentView;
import com.endlosiot.common.notification.view.SmsContentView.SmsContentViewBuilder;
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
 * This class used to perform all business operation on sms content model.
 *
 * @author neha
 * @since 15/02/2023
 */
@Component(value = "smsContentOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SmsContentOperationImpl extends AbstractOperation<SmsContentModel, SmsContentView>
        implements SmsContentOperation {
    @Autowired
    private SmsContentService smsContentService;
    @Autowired
    private SmsAccountService smsAccountService;

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        SmsContentModel smsContentModel = smsContentService.get(id);
        if (smsContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        SmsContentView smsContentView = fromModel(smsContentModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                smsContentView);
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        SmsContentModel smsContentModel = smsContentService.get(id);
        if (smsContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        smsContentService.hardDelete(smsContentModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public SmsContentModel toModel(SmsContentModel smsContentModel, SmsContentView smsContentView)
            throws EndlosiotAPIException {
        smsContentModel.setName(smsContentView.getName());
        smsContentModel.setContent(smsContentView.getContent());
        smsContentModel.setTemplateId(smsContentView.getTemplateId());
        return smsContentModel;
    }

    @Override
    protected SmsContentModel getNewModel() {
        return new SmsContentModel();
    }

    @Override
    public SmsContentView fromModel(SmsContentModel smsContentModel) {
        SmsContentViewBuilder builder = new SmsContentView.SmsContentViewBuilder().setId(smsContentModel.getId())
                .setName(smsContentModel.getName()).setContent(smsContentModel.getContent())
                .setTemplateId(smsContentModel.getTemplateId())
                .setNotificationView(KeyValueView.create(smsContentModel.getNotificationModel().getId(),
                        smsContentModel.getNotificationModel().getName()));
        if (smsContentModel.getSmsAccountModel() != null) {
            builder.setSmsAccountView(KeyValueView.create(smsContentModel.getSmsAccountModel().getId(),
                    smsContentModel.getSmsAccountModel().getMobile()));
        }
        return builder.build();
    }

    @Override
    public BaseService<SmsContentModel> getService() {
        return smsContentService;
    }

    @Override
    public Response doSave(SmsContentView smsContentView) throws EndlosiotAPIException {
        SmsContentModel smsContentModel = smsContentService
                .findByNotification(NotificationModel.getMAP().get(smsContentView.getNotificationView().getKey()));
        if (smsContentModel != null) {
            throw new EndlosiotAPIException(ResponseCode.SMS_CONTENT_ALREADY_EXIST.getCode(),
                    ResponseCode.SMS_CONTENT_ALREADY_EXIST.getMessage());
        }
        smsContentModel = getModel(smsContentView);
        SmsAccountModel smsAccountModel = smsAccountService.getLight(smsContentView.getSmsAccountView().getKey());
        if (smsAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.SMS_ACCOUNT_IS_INVALID.getCode(),
                    ResponseCode.SMS_ACCOUNT_IS_INVALID.getMessage());
        }
        smsContentModel.setSmsAccountModel(smsAccountModel);
        smsContentModel
                .setNotificationModel(NotificationModel.getMAP().get(smsContentView.getNotificationView().getKey()));
        smsContentService.create(smsContentModel);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdate(SmsContentView smsContentView) throws EndlosiotAPIException {
        SmsContentModel smsContentModel = smsContentService.get(smsContentView.getId());
        if (smsContentModel == null) {
            throw new EndlosiotAPIException(ResponseCode.SMS_CONTENT_NOT_FOUND.getCode(),
                    ResponseCode.SMS_CONTENT_NOT_FOUND.getMessage());
        }
        if (!smsContentModel.getNotificationModel().getId().equals(Long
                .valueOf(NotificationEnum.fromId(smsContentView.getNotificationView().getKey().intValue()).getId()))) {
            SmsContentModel tempSmsContentModel = smsContentService
                    .findByNotification(NotificationModel.getMAP().get(smsContentView.getNotificationView().getKey()));
            if (tempSmsContentModel != null) {
                throw new EndlosiotAPIException(ResponseCode.SMS_CONTENT_ALREADY_EXIST.getCode(),
                        ResponseCode.SMS_CONTENT_ALREADY_EXIST.getMessage());
            }
        }
        smsContentModel
                .setNotificationModel(NotificationModel.getMAP().get(smsContentView.getNotificationView().getKey()));
        SmsAccountModel smsAccountModel = smsAccountService.getLight(smsContentView.getSmsAccountView().getKey());
        if (smsAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.SMS_ACCOUNT_IS_INVALID.getCode(),
                    ResponseCode.SMS_ACCOUNT_IS_INVALID.getMessage());
        }
        smsContentModel.setSmsAccountModel(smsAccountModel);
        smsContentService.update(toModel(smsContentModel, smsContentView));
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doSearch(SmsContentView smsContentView, Integer start, Integer recordSize) {
        PageModel result = smsContentService.searchLight(smsContentView, start, recordSize);
        if (result.getRecords() == 0) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                result.getRecords(), fromModelList((List<SmsContentModel>) result.getList()));
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }
}
