package com.endlosiot.dashboard.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.dashboard.view.DashboardView;

public interface DashboardOperation {

    Response getDashboardData(DashboardView dashboardView) throws EndlosiotAPIException;
}
