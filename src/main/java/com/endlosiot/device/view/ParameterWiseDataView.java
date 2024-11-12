package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.*;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class ParameterWiseDataView implements View {
   private List<FlowmeterView> flowMeterViews;
   private List<ChlorineSensorView> chlorineSensorViews;
   private List<TurbiditySensorView> turbiditySensorViews ;
   private List<PressureTransmitterView>  pressureTransmitterViews;
   private List<LevelSensorView> levelSensorViews;
}
