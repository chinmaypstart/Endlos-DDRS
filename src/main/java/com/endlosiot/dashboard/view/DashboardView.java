package com.endlosiot.dashboard.view;

import com.endlosiot.common.view.View;
import com.endlosiot.device.view.FlowmeterView;
import com.endlosiot.device.view.PressureTransmitterView;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DashboardView implements View {
    private String totalFlow;
    private String yesterdayFlow;
    private String avgChlorineValue;
    private String avgTurbidityValue;
    private String minPressureTransmitter;
    private String maxPressureTransmitter;
    private String lowLevelTanks;
    private String HigLevelTanks;
    private String totalFlowMeter;
    private String totalChlorineSensor;
    private String totalTurbiditySensor;
    private String totalPressureTransmitter;
    private String totalLevelSensor;
    private FlowmeterView flowMeterView;
    private PressureTransmitterView pressureTransmitterView;
    private Long lastUpdateDate;
    //private LocationView locationView;
}
