package com.endlosiot.devicediagnosis.service;


import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.model.AlarmHistoryModel;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;

import java.util.List;

public interface AlarmHistoryService extends BaseService<AlarmHistoryModel> {

    PageModel getByDevice(String deviceId, Long startDate, Long endDate, Integer start, Integer recordSize);

    AlarmHistoryModel getAlarmByParameter(Long id);
}
