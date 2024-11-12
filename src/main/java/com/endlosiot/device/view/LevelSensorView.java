package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class LevelSensorView implements View {
   private String name;
//   private String noOfHignTanks;
//   private String noOfLowTanks;
     private Boolean high;
     private DeviceView deviceView;
}
