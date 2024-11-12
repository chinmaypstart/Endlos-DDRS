package com.endlosiot.device.view;

import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.common.view.View;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import lombok.Data;

@Data
public class GraphDataView implements View {

    private DeviceView deviceView;
    private ParameterMasterView parameterMasterView;
    private Long startDate;
    private Long endDate;
    private KeyValueView reportType;

}
