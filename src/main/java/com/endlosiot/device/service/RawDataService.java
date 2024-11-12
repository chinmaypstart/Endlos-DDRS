package com.endlosiot.device.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.model.RawDataModel;

import java.util.List;

public interface RawDataService extends BaseService<RawDataModel> {
    PageModel getByDevice(String deviceId,int start,int recordSize);

    List<RawDataModel> getTodaysData(String deviceId,Long startEpoch,Long endEpoch);

    List<RawDataModel> getAllByDevice(Long id, Long startDate, Long endDate);
}
