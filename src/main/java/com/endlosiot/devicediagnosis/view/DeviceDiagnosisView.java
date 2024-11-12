package com.endlosiot.devicediagnosis.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.device.view.DeviceView;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DeviceDiagnosisView extends IdentifierView {
    private DeviceView deviceView;
    private String jsoncontent;
    private Long createdate;
    private Long receivedate;
}