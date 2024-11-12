package com.endlosiot.device.view;

import com.endlosiot.common.view.IdentifierView;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class RawDataView extends IdentifierView {
    private DeviceView deviceView;
    private String flow;
    private String cholrine;
    private String turbidity;
    private String levelSensor;
    private String pressureTransmitter;
    private String digitalOutput;
    private Long createDate;
}