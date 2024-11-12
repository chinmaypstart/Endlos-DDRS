package com.endlosiot.parametermaster.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.parametermaster.model.ParameterMasterModel;

public interface ParameterMasterService extends BaseService<ParameterMasterModel> {
    ParameterMasterModel getParameterMasterByName(String name);
}
