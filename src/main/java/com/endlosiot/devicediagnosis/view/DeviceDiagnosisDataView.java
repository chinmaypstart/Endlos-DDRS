package com.endlosiot.devicediagnosis.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.device.view.DeviceParameterView;
import com.endlosiot.device.view.DeviceView;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DeviceDiagnosisDataView extends IdentifierView {
    private DeviceView deviceView;
    private List<DeviceDiagnosisView> DeviceDiagnosisViewList;
    //private List<List<DeviceDiagnosisView>> deviceDiagnosisViewList;
}