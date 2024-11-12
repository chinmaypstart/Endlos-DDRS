package com.endlosiot.device.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.device.view.DeviceDataView;
import com.endlosiot.device.view.DeviceView;
import com.endlosiot.device.view.GraphDataView;

public interface DeviceOperation extends BaseOperation<DeviceView> {
    Response doSearchDeviceData(DeviceDataView deviceDataView,Integer start, Integer recordSize);

    Response doSetData(Long id)  throws EndlosiotAPIException;

    Response getDropDownDevice() throws EndlosiotAPIException;

    Response getDropDownDeviceParameter(String deviceId);

    Response gatewayInfo(String deviceId, Integer start, Integer recordSize) throws EndlosiotAPIException;

    Response parameterWiseData(Long locationId,String parameterId, Integer start, Integer recordSize) throws EndlosiotAPIException;

    Response doGetLatitudeLongitude(Long locationId) throws EndlosiotAPIException;

    Response doGetParameterWiseReport(GraphDataView graphDataView) throws EndlosiotAPIException;;

    Response doGetParameterWiseChart(GraphDataView graphDataView) throws EndlosiotAPIException;;

    Response doGetExcelReport(GraphDataView graphDataView) throws EndlosiotAPIException;

    Response gatDeviceDiagnosis(String deviceId, Integer start, Integer recordSize) throws EndlosiotAPIException;

    Response doParameterIdWiseList(DeviceView deviceView, Long id)throws EndlosiotAPIException;
}
