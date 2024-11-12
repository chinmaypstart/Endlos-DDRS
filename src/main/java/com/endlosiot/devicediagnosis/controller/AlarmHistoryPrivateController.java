package com.endlosiot.devicediagnosis.controller;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.operation.AlarmHistoryOperation;
import com.endlosiot.devicediagnosis.operation.DeviceDiagnosisOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/private/alarmhistory")
public class AlarmHistoryPrivateController {
    @Autowired
    private AlarmHistoryOperation alarmHistoryOperation;

    @GetMapping("/fetch-records")
    public ResponseEntity<Response> fetchRecords(@RequestParam(name = "deviceId") String deviceId, @RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate, @RequestParam(name = "start") Integer start, @RequestParam(name = "recordSize") Integer recordSize) throws IOException, EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(alarmHistoryOperation.gatDeviceDiagnosis(deviceId, startDate, endDate, start, recordSize));
    }

//    @PostMapping(value = "/export")
//    public ResponseEntity<Response> getExcelReport(@RequestBody(required = true) GraphDataView graphDataView) throws EndlosiotAPIException, IOException {
//        if (graphDataView.getDeviceView() == null || graphDataView.getDeviceView().getId() == null) {
//            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(alarmHistoryOperation.doExport(graphDataView));
//    }


}
