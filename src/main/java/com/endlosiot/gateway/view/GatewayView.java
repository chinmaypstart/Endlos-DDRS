package com.endlosiot.gateway.view;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.RegexEnum;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.ArchiveView;
import com.endlosiot.gateway.model.GatewayModel;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class GatewayView extends ArchiveView {
    private String name;
    private String gatewayId;
    private String ipAddress;
    private String macAddress;
    private String latitude;
    private String longitude;
    //private LocationView locationView;

    public static void isValid(GatewayView gatewayView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("GATEWAY_NAME", gatewayView.getName(), true, 500));
        Validator.STRING.isValid(new InputField("LATITUDE", gatewayView.getLatitude(), false, 20, RegexEnum.NUMERIC));
        Validator.STRING.isValid(new InputField("LONGITUDE", gatewayView.getLatitude(), false, 20, RegexEnum.NUMERIC));
        Validator.STRING.isValid(new InputField("GATEWAY_ID", gatewayView.getGatewayId(), false, 40));
        Validator.STRING.isValid(new InputField("IP_ADDRESS", gatewayView.getIpAddress(), false, 50));
        Validator.STRING.isValid(new InputField("MAC_ADDRESS", gatewayView.getMacAddress(), false, 50));
        /*if (gatewayView.getLocationView() == null || gatewayView.getLocationView().getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.LOCATION_NAME_IS_MISSING.getCode(), ResponseCode.LOCATION_NAME_IS_MISSING.getMessage());
        }*/
    }

    public static GatewayView setView(GatewayModel gatewayModel) {
        GatewayView gatewayView = new GatewayView();
        //GatewayView gatewayView = GatewayView.builder().name(gatewayModel.getName()).locationView(LocationView.setView(gatewayModel.getLocationModel())).build();
       /* gatewayView.setId(gatewayModel.getId());
        if (gatewayModel.getLongitude() != null) {
            gatewayView.setLongitude(gatewayModel.getLongitude());
        }
        if (gatewayModel.getLatitude() != null) {
            gatewayView.setLatitude(gatewayModel.getLatitude());
        }
        if (gatewayModel.getGatewayId() != null) {
            gatewayView.setGatewayId(gatewayModel.getGatewayId());
        }
        if (gatewayModel.getIpAddress() != null) {
            gatewayView.setIpAddress(gatewayModel.getIpAddress());
        }
        if (gatewayModel.getMacAddress() != null) {
            gatewayView.setMacAddress(gatewayModel.getMacAddress());
        }*/
        return gatewayView;
    }
}
