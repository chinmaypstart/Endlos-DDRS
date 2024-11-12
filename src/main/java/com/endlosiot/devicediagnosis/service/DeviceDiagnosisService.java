package com.endlosiot.devicediagnosis.service;


import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;

import java.util.List;

public interface DeviceDiagnosisService extends BaseService<DeviceDiagnosisModel> {
    PageModel getByDevice(String deviceId, Long startDate, Long endDate, Integer start, Integer recordSize);
    DeviceDiagnosisModel getLatestDiagnosisDataByDeviceId(String deviceId);

    PageModel getDignosisByDevice(GraphDataView graphDataView);

    List<DeviceDiagnosisModel> getByDeviceByStartDate(Long deviceId);
}
