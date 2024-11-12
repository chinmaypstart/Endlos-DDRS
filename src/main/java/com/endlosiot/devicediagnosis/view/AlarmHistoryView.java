package com.endlosiot.devicediagnosis.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.view.DeviceParameterView;
import com.endlosiot.device.view.DeviceView;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class AlarmHistoryView extends IdentifierView {
    private DeviceView deviceView;
    private DeviceParameterView deviceParameterView;
    private Long createdate;
    private Long resolvedate;
    private Long count;
}