package com.endlosiot.devicediagnosis.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.view.AlarmHistoryView;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisView;

import java.io.IOException;

public interface AlarmHistoryOperation {

    Response gatDeviceDiagnosis(String deviceId, String startDate, String endDate, Integer start, Integer recordSize) throws EndlosiotAPIException;
//    Response doExport(GraphDataView graphDataView) throws EndlosiotAPIException, IOException;
}
