package com.endlosiot.device.controller;

import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.device.enums.MonthTypeEnum;
import com.endlosiot.device.enums.ReportTypeEnum;
import com.endlosiot.device.model.DeviceTypeEnum;
import com.endlosiot.device.operation.DeviceOperation;
import com.endlosiot.device.view.DeviceDataView;
import com.endlosiot.device.view.DeviceView;
import com.endlosiot.device.view.GraphDataView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/private/device")
public class DevicePrivateController extends AbstractController<DeviceView> {
    @Autowired
    private DeviceOperation deviceOperation;

    @Override
    public BaseOperation<DeviceView> getOperation() {
        return deviceOperation;
    }

    @Override
    public ResponseEntity<Response> save(@RequestBody DeviceView deviceView) throws EndlosiotAPIException {
        if (deviceView == null)
        isValidSaveData(deviceView);
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doSave(deviceView));
    }

    @Override
    public ResponseEntity<Response> view(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doView(id));
    }

    @Override
    public ResponseEntity<Response> update(@RequestBody DeviceView deviceView) throws EndlosiotAPIException {
        if (deviceView == null || deviceView.getId() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(deviceView);
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doUpdate(deviceView));
    }

    @Override
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doDelete(id));
    }

    @Override
    public ResponseEntity<Response> activeInActive(@PathVariable(name = "id") Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doActiveInActive(id));
    }

    @Override
    public ResponseEntity<Response> search(@RequestBody(required = false) DeviceView deviceView, @RequestParam(name = "start", required = true) Integer start, @RequestParam(name = "recordSize", required = true) Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doSearch(deviceView, start, recordSize));
    }

    @Override
    public void isValidSaveData(DeviceView deviceView) throws EndlosiotAPIException {
        DeviceView.isValid(deviceView);
    }

//    {{apis}}/private/device/search-device-data?start=0&recordSize=10
    @PostMapping(value = "/search-device-data")
    public ResponseEntity<Response> searchDeviceData(@RequestBody(required = false) DeviceDataView deviceDataView,@RequestParam(name = "start") Integer start, @RequestParam(name = "recordSize") Integer recordSize) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doSearchDeviceData(deviceDataView,start, recordSize));
    }

    @GetMapping(value = "/set-data/{id}")
    public ResponseEntity<Response> setData(@PathVariable Long id) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doSetData(id));
    }

    @GetMapping(value = "/dropdown-type")
    @ResponseBody
    public ResponseEntity<Response> dropdownType() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        DeviceTypeEnum.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(key, value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }
    @GetMapping(value = "/drop-down")
    public ResponseEntity<Response> dropDown()
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(deviceOperation.getDropDownDevice());
    }
    @GetMapping(value = "/device-param/drop-down")
    public ResponseEntity<Response> dropDownDeviceParameter(
            @RequestParam(name = "deviceId") String deviceId)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(deviceOperation.getDropDownDeviceParameter(deviceId));
    }

    @GetMapping("/fetch-records")
    public ResponseEntity<Response> fetchRecords(@RequestParam(name = "deviceId") String deviceId,
                                                 @RequestParam(name = "start") Integer start,
                                                 @RequestParam(name = "recordSize") Integer recordSize) throws IOException, EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.gatDeviceDiagnosis(deviceId, start, recordSize));
    }
    @GetMapping("/parameter-wise-data")
    public ResponseEntity<Response> parameterWiseData(@RequestParam(name = "parameterId") String parameterId,
                                                 @RequestParam(name= "locationId") Long locationId,
                                                 @RequestParam(name = "start") Integer start,
                                                 @RequestParam(name = "recordSize") Integer recordSize) throws IOException, EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.
                parameterWiseData(locationId,parameterId, start, recordSize));
    }
    @GetMapping("/get-latitude-longitude")
    public ResponseEntity<Response> getLatitudeLongitude(@RequestParam(name = "locationId" ,required =false) Long locationId) throws IOException, EndlosiotAPIException {
        if(locationId==null){
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.
                doGetLatitudeLongitude(locationId));
    }

    @PostMapping(value = "/get-parameter-wise-report")
    public ResponseEntity<Response> getParameterWiseReport(@RequestBody(required = true) GraphDataView graphDataView)
            throws EndlosiotAPIException {
        if(graphDataView.getParameterMasterView()==null
                || graphDataView.getParameterMasterView().getId()==null
                || graphDataView.getDeviceView()==null
                || graphDataView.getDeviceView().getId()==null
                || graphDataView.getStartDate()==null
                || graphDataView.getReportType() == null
                || graphDataView.getReportType().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation
                .doGetParameterWiseReport(graphDataView));
    }

    @PostMapping(value = "/get-parameter-wise-chart")
    public ResponseEntity<Response> getParameterWiseChar(@RequestBody(required = true) GraphDataView graphDataView)
            throws EndlosiotAPIException {
        if (graphDataView.getParameterMasterView() == null
                || graphDataView.getParameterMasterView().getId() == null
                || graphDataView.getDeviceView() == null
                || graphDataView.getDeviceView().getId() == null
                || graphDataView.getStartDate() == null
                || graphDataView.getReportType() == null
                || graphDataView.getReportType().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation
                .doGetParameterWiseChart(graphDataView));
    }

    @PostMapping(value = "/get-excel-report")
    public ResponseEntity<Response> getExcelReport(@RequestBody(required = true) GraphDataView graphDataView)
            throws EndlosiotAPIException {
        if(graphDataView.getParameterMasterView()==null
                || graphDataView.getParameterMasterView().getId()==null
                || graphDataView.getDeviceView()==null
                || graphDataView.getDeviceView().getId()==null
                || graphDataView.getStartDate()==null
                || graphDataView.getEndDate()==null){
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation
                .doGetExcelReport(graphDataView));
    }

    @GetMapping(value = "/report-type")
    @ResponseBody
    public ResponseEntity<Response> getReportType() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        ReportTypeEnum.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(key, value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }

    @GetMapping(value = "/month-dropdown")
    @ResponseBody
    public ResponseEntity<Response> getMonthType() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        MonthTypeEnum.MAP.forEach((key, value) -> {
            keyValueViews.add(KeyValueView.create(key, value.getName()));
        });
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }
    @PostMapping(value = "/parameter-id-wise-list/{id}")
    @ResponseBody
    public ResponseEntity<Response> parameterIdWiseList(@PathVariable(name = "id") Long id,@RequestBody(required = false) DeviceView deviceView) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(deviceOperation.doParameterIdWiseList(deviceView, id));
    }
}
