package com.endlosiot.screen.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.screen.model.ColumnModel;
import com.endlosiot.screen.view.ScreenDataRequestView;

public interface ColumnMasterOperation extends BaseOperation<ScreenDataRequestView.Column> {

    Response doSave(ColumnModel columnModel) throws EndlosiotAPIException;
}
