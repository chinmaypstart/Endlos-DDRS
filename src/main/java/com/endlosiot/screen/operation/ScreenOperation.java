package com.endlosiot.screen.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.screen.view.ScreenDataRequestView;
import com.endlosiot.screen.view.ScreenView;

public interface ScreenOperation extends BaseOperation<ScreenView> {

    Response doSetData(Long id)  throws EndlosiotAPIException;

    Response doSaveScreeData(ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException;

    Response getDropDownScreen() throws EndlosiotAPIException;
    Response getUserDropDownScreen() throws EndlosiotAPIException;

    Response doSendData(Long id, String value) throws EndlosiotAPIException;


    Response doUpdateScreeData(ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException;
}
