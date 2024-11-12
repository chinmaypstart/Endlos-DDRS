package com.endlosiot.dashboard.controller;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.dashboard.operation.DashboardOperation;
import com.endlosiot.dashboard.view.DashboardView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/private/dashbaord")
public class DashboardPrivateController {
   @Autowired
    DashboardOperation dashboardOperation;

    @PostMapping("/get-dashboard-data")
    public ResponseEntity<Response> fetchRecords(@RequestBody(required = false) DashboardView dashboardView) throws IOException, EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(dashboardOperation.
                getDashboardData(dashboardView));
    }

}
