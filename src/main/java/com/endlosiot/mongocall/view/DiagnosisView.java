package com.endlosiot.mongocall.view;

import com.endlosiot.common.view.View;
import lombok.Data;

import java.util.List;

@Data
public class DiagnosisView implements View {

    private String deviceId;
    private String time;
    private List<DiagnosisParameter> parameterDetail;
}
