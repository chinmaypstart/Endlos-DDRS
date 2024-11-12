package com.endlosiot.screen.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.screen.model.CellModel;

import java.util.List;

public interface CellMasterService extends BaseService<CellModel> {
    List<CellModel> getByScreen(Long screenId);

    List<CellModel> findByDeviceParameterId(Long id);
}
