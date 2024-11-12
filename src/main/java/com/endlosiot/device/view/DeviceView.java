package com.endlosiot.device.view;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.ArchiveView;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class DeviceView extends ArchiveView {

    private static final long serialVersionUID = 5678365620021239583L;
    private String name;
    private List<DeviceParameterView> deviceParameterViewList;
    private String imei;
    private long size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeviceParameterView> getDeviceParameterViewList() {
        return deviceParameterViewList;
    }

    public void setDeviceParameterViewList(List<DeviceParameterView> deviceParameterViewList) {
        this.deviceParameterViewList = deviceParameterViewList;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
    /* private ClientView clientView;
    private LocationView locationView;*/

    public static void isValid(DeviceView deviceView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("NAME", deviceView.getName(), true, 500));
        Validator.STRING.isValid(new InputField("IMEI", deviceView.getImei(), false, 40));
    }
}
