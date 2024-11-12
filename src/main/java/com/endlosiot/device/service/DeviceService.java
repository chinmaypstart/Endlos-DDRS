package com.endlosiot.device.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.model.DeviceModel;

import java.util.List;

public interface DeviceService extends BaseService<DeviceModel> {
    List<DeviceModel> getByLocation(Long locationId);

    void hardDelete(Long deviceId);
}
