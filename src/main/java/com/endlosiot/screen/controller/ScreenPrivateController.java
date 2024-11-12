package com.endlosiot.screen.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.aop.Authorization;
import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.screen.operation.ScreenOperation;
import com.endlosiot.screen.service.ScreenService;
import com.endlosiot.screen.view.ScreenDataRequestView;
import com.endlosiot.screen.view.ScreenView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private/screen")
public class ScreenPrivateController extends AbstractController<ScreenView> {
    @Autowired
    private ScreenOperation screenOperation;
    @Autowired
    ScreenService screenService;

    @Override
    public BaseOperation<ScreenView> getOperation() {
        return screenOperation;
    }

    @Override
    public ResponseEntity<Response> save(@RequestBody ScreenView screenView) throws EndlosiotAPIException {
        if (screenView == null)
        isValidSaveData(screenView);
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doSave(screenView));
    }

    @Override
    public ResponseEntity<Response> view(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doView(id));
    }

    @Override
    public ResponseEntity<Response> update(@RequestBody ScreenView screenView) throws EndlosiotAPIException {
        if (screenView == null || screenView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(screenView);
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doUpdate(screenView));
    }

    @Override
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doDelete(id));
    }

    @Override
    public ResponseEntity<Response> activeInActive(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doActiveInActive(id));
    }

    @Override
    @AccessLog
    @Authorization(modules = ModuleEnum.SCREEN, rights = RightsEnum.LIST)
    public ResponseEntity<Response> search(@RequestBody(required = false) ScreenView screenView, @RequestParam(name = "start", required = true) Integer start, @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doSearch(screenView, start, recordSize));
    }

    @Override
    public void isValidSaveData(ScreenView screenView) throws EndlosiotAPIException {
        ScreenView.isValid(screenView);
    }

    @PostMapping(value = "/save-screen-data")
    public ResponseEntity<Response> saveScreenData(@RequestBody ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException {
        if (screenDataRequest == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doSaveScreeData(screenDataRequest));
    }

    @GetMapping(value = "/drop-down")
    public ResponseEntity<Response> dropDown()
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(screenOperation.getDropDownScreen());
    }

    @GetMapping(value = "/user-drop-down")
    public ResponseEntity<Response> userDropDown()
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(screenOperation.getUserDropDownScreen());
    }

    @GetMapping(value = "/save-raw/{id}/{value}")
    public ResponseEntity<Response> saveRawParameter(@PathVariable(name = "id") Long id, @PathVariable(name = "value") String value) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doSendData(id,value));
    }

    @PutMapping(value = "/update-screen-data")
    public ResponseEntity<Response> updateScreenData(@RequestBody ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException {
        if (screenDataRequest == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(screenOperation.doUpdateScreeData(screenDataRequest));
    }
}
