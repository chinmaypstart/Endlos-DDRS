package com.endlosiot.common.notification.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.SmsAccountModel;
import com.endlosiot.common.notification.service.SmsAccountService;
import com.endlosiot.common.notification.view.SmsAccountView;
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
 * This class used to perform all business operation on sms model.
 *
 * @author neha
 * @since 15/02/2023
 */
@Component(value = "smsAccountOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SmsAccountOperationImpl extends AbstractOperation<SmsAccountModel, SmsAccountView>
        implements SmsAccountOperation {
    @Autowired
    SmsAccountService smsAccountService;

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        SmsAccountModel smsAccountModel = getSmsAccount(id);
        SmsAccountView smsAccountView = fromModel(smsAccountModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                smsAccountView);
    }

    private SmsAccountModel getSmsAccount(Long id) throws EndlosiotAPIException {
        SmsAccountModel smsAccountModel = SmsAccountModel.getMap().get(id);
        if (smsAccountModel == null) {
            smsAccountModel = smsAccountService.get(id);
            if (smsAccountModel == null) {
                throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                        ResponseCode.NO_DATA_FOUND.getMessage());
            }
        }
        return smsAccountModel;
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        SmsAccountModel smsAccountModel = smsAccountService.get(id);
        if (smsAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        smsAccountService.hardDelete(smsAccountModel);
        SmsAccountModel.removeSmsAccount(smsAccountModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public SmsAccountModel toModel(SmsAccountModel smsAccountModel, SmsAccountView smsAccountView)
            throws EndlosiotAPIException {
        smsAccountModel.setMobile(smsAccountView.getMobile());
        smsAccountModel.setPassword(smsAccountView.getPassword());
        smsAccountModel.setSenderId(smsAccountView.getSenderId());
        smsAccountModel.setPeId(smsAccountView.getPeId());
        return smsAccountModel;
    }

    @Override
    protected SmsAccountModel getNewModel() {
        return new SmsAccountModel();
    }

    @Override
    public SmsAccountView fromModel(SmsAccountModel smsAccountModel) {
        return new SmsAccountView.SmsAccountViewBuilder().setId(smsAccountModel.getId())
                .setMobile(smsAccountModel.getMobile()).setPassword(smsAccountModel.getPassword())
                .setPeId(smsAccountModel.getPeId()).build();
    }

    @Override
    public BaseService getService() {
        return smsAccountService;
    }

    @Override
    public Response doSave(SmsAccountView smsAccountView) throws EndlosiotAPIException {
        SmsAccountModel smsAccountModelExist = smsAccountService.getByMobile(smsAccountView.getMobile());
        if (smsAccountModelExist != null) {
            throw new EndlosiotAPIException(ResponseCode.SMS_ACCOUNT_ALREADY_EXIST.getCode(),
                    ResponseCode.SMS_ACCOUNT_ALREADY_EXIST.getMessage());
        }
        SmsAccountModel smsAccountModel = toModel(getNewModel(), smsAccountView);
        smsAccountService.create(smsAccountModel);
        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(smsAccountModel));
    }

    @Override
    public Response doUpdate(SmsAccountView smsAccountView) throws EndlosiotAPIException {
        SmsAccountModel smsAccountModel = smsAccountService.get(smsAccountView.getId());
        if (smsAccountModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        if (!smsAccountModel.getMobile().equals(smsAccountView.getMobile())) {
            SmsAccountModel smsAccountModelExist = smsAccountService.getByMobile(smsAccountView.getMobile());
            if (smsAccountModelExist != null) {
                throw new EndlosiotAPIException(ResponseCode.SMS_ACCOUNT_ALREADY_EXIST.getCode(),
                        ResponseCode.SMS_ACCOUNT_ALREADY_EXIST.getMessage());
            }
        }
        smsAccountService.update(toModel(smsAccountModel, smsAccountView));
        return ViewResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage(), fromModel(smsAccountModel));
    }

    @Override
    public Response doSearch(SmsAccountView smsAccountView, Integer start, Integer recordSize) {
        List<SmsAccountModel> smsAccounts = getSmsAccounts(smsAccountView, start, recordSize);
        if (smsAccounts == null || smsAccounts.isEmpty()) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                smsAccounts.size(), fromModelList(smsAccounts));
    }

    private List<SmsAccountModel> getSmsAccounts(SmsAccountView smsAccountView, Integer start, Integer recordSize) {
        List<SmsAccountModel> smsAccounts = new ArrayList<>(SmsAccountModel.getMap().values());
        if (smsAccounts == null || smsAccounts.isEmpty()) {
            PageModel pageModel = smsAccountService.searchByLight(smsAccountView, start, recordSize);
            if (pageModel.getList() != null && !pageModel.getList().isEmpty()) {
                smsAccounts = (List<SmsAccountModel>) pageModel.getList();
            }
        }
        return smsAccounts;
    }

    @Override
    public Response doDropdown() {
        List<SmsAccountModel> smsAccounts = getSmsAccounts(new SmsAccountView.SmsAccountViewBuilder().build(), 0, null);
        if (smsAccounts == null || smsAccounts.isEmpty()) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        List<KeyValueView> keyValueViews = new ArrayList<>();
        for (SmsAccountModel smsAccountModel : smsAccounts) {
            keyValueViews.add(KeyValueView.create(smsAccountModel.getId(), smsAccountModel.getMobile()));
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                keyValueViews.size(), keyValueViews);
    }

    @Override
    public void doSaveMap(SmsAccountView smsAccountView) {
        SmsAccountModel smsAccountModel = smsAccountService.get(smsAccountView.getId());
        SmsAccountModel.addSmsAccount(smsAccountModel);
    }

    @Override
    public void doUpdateMap(SmsAccountView smsAccountView) {
        SmsAccountModel smsAccountModel = smsAccountService.get(smsAccountView.getId());
        SmsAccountModel.updateSmsAccount(smsAccountModel);
    }
}
