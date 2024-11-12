package com.endlosiot.parametermaster.operation;

import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.parametermaster.view.ParameterMasterView;

/**
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
public interface ParameterMasterOperation extends BaseOperation<ParameterMasterView> {

    Response getDropDown();
}
