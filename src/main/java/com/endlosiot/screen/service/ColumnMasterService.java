package com.endlosiot.screen.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.screen.model.ColumnModel;

public interface ColumnMasterService extends BaseService<ColumnModel> {
    ColumnModel getByScreenIdAndColumnName(Long screenId, String column);
}
