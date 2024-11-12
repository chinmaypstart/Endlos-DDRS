package com.endlosiot.dashboard.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.dashboard.view.DashboardView;
import com.endlosiot.device.model.*;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.device.service.DeviceService;
import com.endlosiot.device.service.RawDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Component(value = "dashboardOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DashboardOperationImpl implements DashboardOperation {
    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceParameterService deviceParameterService;

    @Autowired
    RawDataService rawDataService;

   /* @Autowired
    LocationService locationService;*/

    @Override
    public Response getDashboardData(DashboardView view) throws EndlosiotAPIException {
        DashboardView dashboardView = new DashboardView();
        LocalDate localDate = LocalDate.now();
        Long totalFlowMeter = 0l;
        Long totalChlorineMeter = 0l;
        Long totalTurbidityMeter = 0l;
        Long totalTransmitter = 0l;
        Long totalLevelSensor = 0l;
        BigDecimal totalFlow = new BigDecimal(0);
        BigDecimal yesterDayFlow = new BigDecimal(0);
        BigDecimal sumChlorineValue = new BigDecimal(0);
        BigDecimal sumTurbidityValue = new BigDecimal(0);
        BigDecimal minPressureTransmitter = null;
        BigDecimal maxPressureTransmitter = null;
        Long lowLevelTank = 0l;
        Long highLevelTank = 0l;
        List<DeviceModel> deviceModels = new ArrayList<>();
        /*if (view.getLocationView() != null && view.getLocationView().getId() != null) {
            LocationModel locationModel = locationService.get(view.getLocationView().getId());
            if (locationModel == null) {
                throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
            }
            deviceModels = deviceService.getByLocation(locationModel.getId());
        } else*/ {
            deviceModels = deviceService.findAll();
        }
        for (DeviceModel deviceModel : deviceModels) {
            List<DeviceParameterModel> deviceParameterModels = deviceParameterService.getByDeviceModel(deviceModel.getId());
            if (deviceParameterModels != null && !deviceParameterModels.isEmpty()) {
                for (DeviceParameterModel deviceParameterModel : deviceParameterModels) {
                    if (deviceParameterModel != null) {
                        if (deviceParameterModel.getParameterMasterModel().getId() == 1) {
                            Long startEpoch = DateUtility.getStartEpoch(localDate);
                            Long endEpoch = DateUtility.getEndEpoch(localDate);
                            Long previousStartEpoch = DateUtility.getStartEpoch(localDate.minusDays(1));
                            Long previousEndEpoch = DateUtility.getEndEpoch(localDate.minusDays(1));
                            if (view.getFlowMeterView() != null) {
                                if (view.getFlowMeterView().getIsDay() && view.getFlowMeterView().getStartEpoch() != null && view.getFlowMeterView().getEndEpoch() != null) {
                                    startEpoch = view.getFlowMeterView().getStartEpoch();
                                    endEpoch = view.getFlowMeterView().getEndEpoch();
                                    LocalDate dayEpoch = DateUtility.getEpochTodate(view.getFlowMeterView().getStartEpoch());
                                    LocalDate yesterDay = dayEpoch.minusDays(1);
                                    previousStartEpoch = DateUtility.getStartEpoch(yesterDay);
                                    previousEndEpoch = DateUtility.getEndEpoch(yesterDay);
                                }
                                if (view.getFlowMeterView().getIsWeek()) {
                                    LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                                    LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                                    startEpoch = DateUtility.getStartEpoch(startOfWeek);
                                    endEpoch = DateUtility.getEndEpoch(endOfWeek);
                                    previousStartEpoch = DateUtility.getStartEpoch(localDate.minus(1, ChronoUnit.WEEKS).with(DayOfWeek.MONDAY));
                                    previousEndEpoch = DateUtility.getEndEpoch(localDate.minus(1, ChronoUnit.WEEKS).with(DayOfWeek.SUNDAY));
                                }
                                if (view.getFlowMeterView().getIsMonth()) {
                                    LocalDate firstDayOfCurrentMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
                                    LocalDate lastDayOfCurrentMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
                                    startEpoch = DateUtility.getStartEpoch(firstDayOfCurrentMonth);
                                    endEpoch = DateUtility.getEndEpoch(lastDayOfCurrentMonth);
                                    LocalDate firstDayOfPreviousMonth = localDate.minusMonths(1).withDayOfMonth(1);
                                    LocalDate lastDayOfPreviousMonth = firstDayOfPreviousMonth.plusMonths(1).minusDays(1);
                                    previousStartEpoch = DateUtility.getStartEpoch(firstDayOfPreviousMonth);
                                    previousEndEpoch = DateUtility.getEndEpoch(lastDayOfPreviousMonth);
                                }
                                if (view.getFlowMeterView().getIsYear()) {
                                    LocalDate firstDayOfCurrentYear = localDate.with(TemporalAdjusters.firstDayOfYear());
                                    LocalDate lastDayOfCurrentYear = localDate.with(TemporalAdjusters.lastDayOfYear());
                                    startEpoch = DateUtility.getStartEpoch(firstDayOfCurrentYear);
                                    endEpoch = DateUtility.getEndEpoch(lastDayOfCurrentYear);
                                    LocalDate firstDayOfPreviousYear = localDate.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
                                    LocalDate lastDayOfPreviousYear = firstDayOfPreviousYear.plusYears(1).minusDays(1);
                                    previousStartEpoch = DateUtility.getStartEpoch(firstDayOfPreviousYear);
                                    previousEndEpoch = DateUtility.getEndEpoch(lastDayOfPreviousYear);
                                }
                            }
                            List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                            if (rawDataModels != null && !rawDataModels.isEmpty()) {
                                if(rawDataModels.get(rawDataModels.size() - 1).getFlow()!=null){
                                    BigDecimal firstValue = rawDataModels.get(0).getFlow();
                                    BigDecimal lastValue = rawDataModels.get(rawDataModels.size() - 1).getFlow();
//                                totalFlow = totalFlow.add(lastValue.subtract(firstValue));
                                    BigDecimal flow = lastValue.multiply(new BigDecimal(10)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    totalFlow = totalFlow.add(flow);
                                }
                            }
                            List<RawDataModel> yesterDayeModels = rawDataService.getTodaysData(deviceModel.getImei(), previousStartEpoch, previousEndEpoch);
                            if (yesterDayeModels != null && !yesterDayeModels.isEmpty()) {
                                if(yesterDayeModels.get(yesterDayeModels.size() - 1).getFlow()!=null){
                                    //                                BigDecimal firstValue = yesterDayeModels.get(0).getFlow();
                                    BigDecimal lastValue = yesterDayeModels.get(yesterDayeModels.size() - 1).getFlow();
//                                yesterDayFlow = yesterDayFlow.add(lastValue.subtract(firstValue));
                                    BigDecimal yesterdayFlow = lastValue.multiply(new BigDecimal(10)).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    yesterDayFlow = yesterDayFlow.add(yesterdayFlow);
                                }
                            }
                            totalFlowMeter++;
                        }
                        if (deviceParameterModel.getParameterMasterModel().getId() == 2) {
                            Long startEpoch = DateUtility.getStartEpoch(localDate);
                            Long endEpoch = DateUtility.getEndEpoch(localDate);
                            List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                            if (rawDataModels != null && !rawDataModels.isEmpty()) {
                                if(rawDataModels.get(rawDataModels.size() - 1).getCholrine()!=null){
                                    BigDecimal chlorineValue = rawDataModels.get(rawDataModels.size() - 1).getCholrine();
                                    sumChlorineValue = sumChlorineValue.add(chlorineValue);
                                }
                            }
                            totalChlorineMeter++;
                        }
                        if (deviceParameterModel.getParameterMasterModel().getId() == 3) {
                            Long startEpoch = DateUtility.getStartEpoch(localDate);
                            Long endEpoch = DateUtility.getEndEpoch(localDate);
                            List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                            if (rawDataModels != null && !rawDataModels.isEmpty()) {
                                if(rawDataModels.get(rawDataModels.size() - 1).getTurbidity()!=null){
                                    BigDecimal turbidityValue = rawDataModels.get(rawDataModels.size() - 1).getTurbidity();
                                    sumTurbidityValue = sumTurbidityValue.add(turbidityValue);
                                }
                            }
                            totalTurbidityMeter++;
                        }
                        if (deviceParameterModel.getParameterMasterModel().getId() == 4) {
                            Long startEpoch = DateUtility.getStartEpoch(localDate);
                            Long endEpoch = DateUtility.getEndEpoch(localDate);
                            if (view.getPressureTransmitterView() != null) {
                                if (view.getPressureTransmitterView().getIsDay() && view.getPressureTransmitterView().getStartEpoch() != null && view.getPressureTransmitterView().getEndEpoch() != null) {
                                    startEpoch = view.getPressureTransmitterView().getStartEpoch();
                                    endEpoch = view.getPressureTransmitterView().getEndEpoch();
                                }
                                if (view.getPressureTransmitterView().getIsWeek()) {
                                    LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                                    LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
                                    startEpoch = DateUtility.getStartEpoch(startOfWeek);
                                    endEpoch = DateUtility.getEndEpoch(endOfWeek);
                                }
                                if (view.getPressureTransmitterView().getIsMonth()) {
                                    LocalDate firstDayOfCurrentMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
                                    LocalDate lastDayOfCurrentMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
                                    startEpoch = DateUtility.getStartEpoch(firstDayOfCurrentMonth);
                                    endEpoch = DateUtility.getEndEpoch(lastDayOfCurrentMonth);
                                }
                                if (view.getPressureTransmitterView().getIsYear()) {
                                    LocalDate firstDayOfCurrentYear = localDate.with(TemporalAdjusters.firstDayOfYear());
                                    LocalDate lastDayOfCurrentYear = localDate.with(TemporalAdjusters.lastDayOfYear());
                                    startEpoch = DateUtility.getStartEpoch(firstDayOfCurrentYear);
                                    endEpoch = DateUtility.getEndEpoch(lastDayOfCurrentYear);
                                }
                            }
                            List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                            if (rawDataModels != null && !rawDataModels.isEmpty()) {
                                BigDecimal minValue = rawDataModels.stream()
                                        .map(RawDataModel::getPressureTransmitter)
                                        .min(Comparator.naturalOrder()).orElse(null);
                                BigDecimal maxValue = rawDataModels.stream()
                                        .map(RawDataModel::getPressureTransmitter)
                                        .max(Comparator.naturalOrder()).orElse(null);
                                if (minPressureTransmitter == null) {
                                    minPressureTransmitter = minValue;
                                }
                                if (maxPressureTransmitter == null) {
                                    maxPressureTransmitter = maxValue;
                                }
                                if (minPressureTransmitter.compareTo(minValue) < 0) {
                                    minPressureTransmitter = minValue;
                                }
                                if (maxPressureTransmitter.compareTo(maxValue) > 0) {
                                    maxPressureTransmitter = maxValue;
                                }
                                minPressureTransmitter = scaleData(minPressureTransmitter);
                                maxPressureTransmitter = scaleData(maxPressureTransmitter);
//                                minPressureTransmitter = minPressureTransmitter.add(minValue);
//                                maxPressureTransmitter = maxPressureTransmitter.add(maxValue);
                            }
                            totalTransmitter++;
                        }
                        if (deviceParameterModel.getParameterMasterModel().getId() == 5) {
                            Long startEpoch = DateUtility.getStartEpoch(localDate);
                            Long endEpoch = DateUtility.getEndEpoch(localDate);
                            List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                            if (rawDataModels != null && !rawDataModels.isEmpty()) {
                                if(rawDataModels.get(rawDataModels.size() - 1).getLevelSensor()!=null){
                                    BigDecimal levelValue = rawDataModels.get(rawDataModels.size() - 1).getLevelSensor();
//                                for (RawDataModel rawDataModel : rawDataModels) {
//                                    if (rawDataModel.getLevelSensor().equals(0)) {
//                                        lowLevelTank++;
//                                    } else {
//                                        highLevelTank++;
//                                    }
//                                }
                                    if (levelValue.compareTo(BigDecimal.ZERO) == 0) {
                                        lowLevelTank++;
                                    } else {
                                        highLevelTank++;
                                    }
                                }
                            }
                            totalLevelSensor++;
                        }
                    }
                }
            }
        }
        dashboardView.setTotalFlow(Long.toString(totalFlow.longValue()));
        dashboardView.setYesterdayFlow(Long.toString(yesterDayFlow.longValue()));
        if(totalChlorineMeter!=0l){
            BigDecimal avgChlorineValue = sumChlorineValue.divide(new BigDecimal(totalChlorineMeter)).setScale(2, BigDecimal.ROUND_HALF_UP);
            dashboardView.setAvgChlorineValue(avgChlorineValue.toString());
        }
        if(totalTurbidityMeter!=0l){
            BigDecimal avgTurbidityValue = sumTurbidityValue.divide(new BigDecimal(totalTurbidityMeter)).setScale(2, BigDecimal.ROUND_HALF_UP);
            dashboardView.setAvgTurbidityValue(avgTurbidityValue.toString());
        }
        if (minPressureTransmitter == null) {
            minPressureTransmitter = new BigDecimal(0);
        }
        if (maxPressureTransmitter == null) {
            maxPressureTransmitter = new BigDecimal(0);
        }
        dashboardView.setMinPressureTransmitter(minPressureTransmitter.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dashboardView.setMaxPressureTransmitter(maxPressureTransmitter.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dashboardView.setLowLevelTanks(lowLevelTank.toString());
        dashboardView.setHigLevelTanks(highLevelTank.toString());
        dashboardView.setTotalFlowMeter(totalFlowMeter.toString());
        dashboardView.setTotalChlorineSensor(totalChlorineMeter.toString());
        dashboardView.setTotalTurbiditySensor(totalTurbidityMeter.toString());
        dashboardView.setTotalPressureTransmitter(totalTransmitter.toString());
        dashboardView.setTotalLevelSensor(totalLevelSensor.toString());
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), dashboardView);
    }

    private BigDecimal scaleData(BigDecimal value) {
        BigDecimal lowerBound = new BigDecimal("4");
        BigDecimal upperBound = new BigDecimal("20");
        BigDecimal minValue = new BigDecimal(0);
        BigDecimal maxValue = new BigDecimal("10");

        if (value.compareTo(lowerBound) <= 0) {
            return minValue;
        } else if (value.compareTo(upperBound) >= 0) {
            return maxValue;
        } else {
            // Scale linearly between 1 and 10 for values between 4 and 20
            BigDecimal scaledValue = BigDecimal.ONE.add(
                    value.subtract(lowerBound).multiply(maxValue.subtract(minValue))
                            .divide(upperBound.subtract(lowerBound), 10, BigDecimal.ROUND_HALF_UP)
            );
            return scaledValue;
        }
    }
}
