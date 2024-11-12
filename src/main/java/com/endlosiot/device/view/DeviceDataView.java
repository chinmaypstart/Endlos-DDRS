package com.endlosiot.device.view;

import com.endlosiot.common.view.IdentifierView;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DeviceDataView extends IdentifierView {
    private String plus;
    private String analog1;
    private String analog2;
    private String turbodity;
    private String chlorine;
    private Long dateCreate;
    private Long type;
}
