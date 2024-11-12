package com.endlosiot.device.view;

import com.endlosiot.common.view.View;
import lombok.Data;

@Data
public class RawDataChartView implements View {

    private long keyData;
    private double sumData;
}
