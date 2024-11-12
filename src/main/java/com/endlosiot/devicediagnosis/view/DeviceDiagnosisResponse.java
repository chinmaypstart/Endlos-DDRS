package com.endlosiot.devicediagnosis.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.device.view.DeviceView;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DeviceDiagnosisResponse extends IdentifierView {
    private int code;
    private String message;
    private boolean hasError;
    private View view;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public static class View {
        private DeviceView deviceView;
        private List<List<DeviceDiagnosisView>> deviceDiagnosisViewList;

        public DeviceView getDeviceView() {
            return deviceView;
        }

        public void setDeviceView(DeviceView deviceView) {
            this.deviceView = deviceView;
        }

        public List<List<DeviceDiagnosisView>> getDeviceDiagnosisViewList() {
            return deviceDiagnosisViewList;
        }

        public void setDeviceDiagnosisViewList(List<List<DeviceDiagnosisView>> deviceDiagnosisViewList) {
            this.deviceDiagnosisViewList = deviceDiagnosisViewList;
        }
    }

}