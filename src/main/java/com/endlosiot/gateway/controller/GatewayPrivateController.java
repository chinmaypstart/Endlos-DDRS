package com.endlosiot.gateway.controller;

import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.gateway.operation.GatewayOperation;
import com.endlosiot.gateway.view.GatewayView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/gateway")
public class GatewayPrivateController extends AbstractController<GatewayView> {
    @Autowired
    private GatewayOperation gatewayOperation;

    @Override
    public BaseOperation<GatewayView> getOperation() {
        return gatewayOperation;
    }

    @Override
    public ResponseEntity<Response> save(@RequestBody GatewayView gatewayView) throws EndlosiotAPIException {
        if (gatewayView == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(gatewayView);
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doSave(gatewayView));
    }

    @Override
    public ResponseEntity<Response> view(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doView(id));
    }

    @Override
    public ResponseEntity<Response> update(@RequestBody GatewayView gatewayView) throws EndlosiotAPIException {
        if (gatewayView == null || gatewayView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(gatewayView);
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doUpdate(gatewayView));
    }

    @Override
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doDelete(id));
    }

    @Override
    public ResponseEntity<Response> activeInActive(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doActiveInActive(id));
    }

    @Override
    public ResponseEntity<Response> search(@RequestBody(required = false) GatewayView gatewayView, @RequestParam(name = "start", required = true) Integer start, @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(gatewayOperation.doSearch(gatewayView, start, recordSize));
    }

    @Override
    public void isValidSaveData(GatewayView gatewayView) throws EndlosiotAPIException {
    GatewayView.isValid(gatewayView);
    }
}
