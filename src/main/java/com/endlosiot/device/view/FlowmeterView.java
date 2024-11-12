package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class FlowmeterView implements View {
   private String name;
   private String todayVale;
   private String previousValue;
   private Boolean isDay;
   private Boolean isWeek;
   private Boolean isMonth;
   private Boolean isYear;
   private Long startEpoch;
   private Long endEpoch;
   private DeviceView deviceView;
}
