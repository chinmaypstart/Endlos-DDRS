package com.endlosiot.device.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.view.DeviceParameterView;

import java.util.List;

/**
 * @author chetanporwal
 * @since 06/09/2023
 */
public interface DeviceParameterService extends BaseService<DeviceParameterModel> {

    List<DeviceParameterModel> getByDeviceModel(Long deviceId);

    List<DeviceParameterModel> getByDeviceModelDeviceId(String deviceId);

    void hardDelete(List<DeviceParameterModel> deviceParameterModels) throws EndlosiotAPIException;

    DeviceParameterModel getByDeviceAndParameter(Long deviceId, Long parameterId);

    List<DeviceParameterModel> getByDeviceParameter(List<DeviceParameterView> deviceParameterViewList);

    List<DeviceParameterModel> getRecordForAlarm();
}
