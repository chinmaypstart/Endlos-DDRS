package com.endlosiot.gateway.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.gateway.model.GatewayModel;
import com.endlosiot.gateway.service.GatewayService;
import com.endlosiot.gateway.view.GatewayView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component(value = "gatewayOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class GatewayOperationImpl extends AbstractOperation<GatewayModel, GatewayView> implements GatewayOperation {
    @Autowired
    private GatewayService gatewayService;
   /* @Autowired
    private LocationService locationService;*/

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        GatewayModel gatewayModel = gatewayService.get(id);
        if (gatewayModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        GatewayView gatewayView = fromModel(gatewayModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), gatewayView);
    }

    @Override
    public Response doSave(GatewayView gatewayView) throws EndlosiotAPIException {
        GatewayModel gatewayModel = toModel(getNewModel(), gatewayView);
        gatewayService.create(gatewayModel);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdate(GatewayView gatewayView) throws EndlosiotAPIException {
        GatewayModel gatewayModel = gatewayService.get(gatewayView.getId());
        if (gatewayModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        toModel(gatewayModel, gatewayView);
        gatewayService.update(gatewayModel);
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(), ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        GatewayModel gatewayModel = gatewayService.get(id);
        if (gatewayModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //gatewayModel.setArchive(true);
        gatewayService.delete(gatewayModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(), ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        GatewayModel gatewayModel = gatewayService.get(id);
        if (gatewayModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        Auditor.activationAudit(gatewayModel, !gatewayModel.isActive());
        gatewayService.update(gatewayModel);
        return CommonResponse.create(ResponseCode.ACTIVATION_SUCCESSFUL.getCode(), ResponseCode.ACTIVATION_SUCCESSFUL.getMessage());
    }

    @Override
    public GatewayModel toModel(GatewayModel gatewayModel, GatewayView gatewayView) throws EndlosiotAPIException {
        gatewayModel.setName(gatewayView.getName());
        if (gatewayView.getGatewayId() != null) {
            gatewayModel.setGatewayId(gatewayView.getGatewayId());
        }
        if (gatewayView.getIpAddress() != null) {
            gatewayModel.setIpAddress(gatewayView.getIpAddress());
        }
        if (gatewayView.getMacAddress() != null) {
            gatewayModel.setMacAddress(gatewayView.getMacAddress());
        }
        if (gatewayView.getLatitude() != null) {
            gatewayModel.setLatitude(gatewayView.getLatitude());
        }
        if (gatewayView.getLongitude() != null) {
            gatewayModel.setLongitude(gatewayView.getLongitude());
        }
       /* LocationModel locationModel = locationService.get(gatewayView.getLocationView().getId());
        if (locationModel == null) {
            throw new EndlosiotAPIException(ResponseCode.LOCATION_NAME_IS_INVALID.getCode(), ResponseCode.LOCATION_NAME_IS_INVALID.getMessage());
        }
        gatewayModel.setLocationModel(locationModel);
        gatewayModel.setClientModel(locationModel.getClientModel());*/
        return gatewayModel;
    }

    @Override
    protected GatewayModel getNewModel() {
        return new GatewayModel();
    }

    @Override
    public GatewayView fromModel(GatewayModel gatewayModel) {
        return GatewayView.setView(gatewayModel);
    }

    @Override
    public BaseService getService() {
        return gatewayService;
    }
}
