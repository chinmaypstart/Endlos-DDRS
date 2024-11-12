package com.endlosiot.mongocall.controller;

import com.endlosiot.common.response.Response;
import com.endlosiot.mongocall.operation.GatewayCommunicationOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("private/diagnosis")
public class GatewayCommunicationController {

    @Autowired
    GatewayCommunicationOperation gatewayCommunicationOperation;

    @GetMapping("/fetch-records")
    public ResponseEntity<Response> fetchRecords(@RequestParam(name = "deviceId") String deviceId,
                                                 @RequestParam(name = "start") Integer start,
                                                 @RequestParam(name = "recordSize") Integer recordSize) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(gatewayCommunicationOperation.
                gatewayInfo(deviceId, start, recordSize));
    }
}
