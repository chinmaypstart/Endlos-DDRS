package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class ChlorineSensorView implements View {
   private String name;
   private String avgValue;
   private DeviceView deviceView;
}
