package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class PressureTransmitterView implements View {
   private String name;
   private String minVale;
   private String maxValue;
   private Boolean isDay;
   private Boolean isWeek;
   private Boolean isMonth;
   private Boolean isYear;
   private Long startEpoch;
   private Long endEpoch;
   private String currentValue;
   private  DeviceView deviceView;
}
