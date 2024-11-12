package com.endlosiot.screen.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.screen.model.ScreenModel;

public interface ScreenService extends BaseService<ScreenModel> {
    void hardDelete(Long screenId);

    void hardDeleteRowColumnCell(Long screenId);
}
