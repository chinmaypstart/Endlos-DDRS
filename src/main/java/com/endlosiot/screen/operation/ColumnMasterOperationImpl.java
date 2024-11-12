package com.endlosiot.screen.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.screen.model.ColumnModel;
import com.endlosiot.screen.service.ColumnMasterService;
import com.endlosiot.screen.view.ScreenDataRequestView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "columnMasterOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ColumnMasterOperationImpl extends AbstractOperation<ColumnModel, ScreenDataRequestView.Column> implements ColumnMasterOperation {

    @Autowired
    ColumnMasterService columnMasterService;

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

    @Override
    public ColumnModel toModel(ColumnModel model, ScreenDataRequestView.Column view) throws EndlosiotAPIException {
        return null;
    }

    @Override
    protected ColumnModel getNewModel() {
        return null;
    }

    @Override
    public ScreenDataRequestView.Column fromModel(ColumnModel model) {
        return null;
    }

    @Override
    public BaseService getService() {
        return null;
    }

    public Response doSave(ColumnModel columnModel) throws EndlosiotAPIException {

        columnMasterService.create(columnModel);

        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(columnModel));
    }


}
