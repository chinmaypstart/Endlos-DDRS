package com.endlosiot.device.operation;

import com.amazonaws.services.gamelift.model.LocationModel;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.user.service.UserService;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.common.util.FileUtility;
import com.endlosiot.device.enums.ReportTypeEnum;
import com.endlosiot.device.model.DeviceDataModel;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.model.RawDataModel;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.device.service.DeviceService;
import com.endlosiot.device.service.RawDataService;
import com.endlosiot.device.view.*;
import com.endlosiot.devicediagnosis.service.DeviceDiagnosisService;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.endlosiot.parametermaster.service.ParameterMasterService;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import com.endlosiot.screen.model.CellModel;
import com.endlosiot.screen.service.CellMasterService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.*;

@Component(value = "deviceOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceOperationImpl extends AbstractOperation<DeviceModel, DeviceView> implements DeviceOperation {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterMasterService parameterMasterService;
    @Autowired
    private DeviceParameterService deviceParameterService;
    @Autowired
    private DeviceDiagnosisService deviceDiagnosisService;

    @Autowired
    private CellMasterService cellMasterService;


    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        DeviceModel deviceModel = deviceService.get(id);
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        DeviceView deviceView = fromModel(deviceModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceView);
    }

    @Override
    public Response doSave(DeviceView deviceView) throws EndlosiotAPIException {
        DeviceModel deviceModel = toModel(getNewModel(), deviceView);
        deviceModel.setActive(true);
        deviceService.create(deviceModel);
        setDeviceParameterModelList(deviceModel, deviceView);

        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(deviceModel));
    }

    private void setDeviceParameterModelList(DeviceModel deviceModel, DeviceView deviceView) throws EndlosiotAPIException {
        List<DeviceParameterModel> existDeviceParameterModels = new ArrayList<>();
        List<DeviceParameterModel> toDeleteDeviceParameterModels = new ArrayList<>();
        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceModel(deviceModel.getId());
        if (deviceParameterModelList == null) {
            deviceParameterModelList = new ArrayList<>();
        }
        for (DeviceParameterView deviceParameterView : deviceView.getDeviceParameterViewList()) {
            if (Objects.isNull(deviceParameterView.getParameterMasterView())) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
            }
            ParameterMasterView mappingView = deviceParameterView.getParameterMasterView();
            ParameterMasterModel mappingModel = parameterMasterService.get(mappingView.getId());
            if (mappingModel == null) {
                throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
            }

            if (deviceParameterView.getId() != null) {
                DeviceParameterModel deviceParameter = deviceParameterService.get(deviceParameterView.getId());
                if (deviceParameter != null) {
                    DeviceParameterModel deviceParameterM;
//                    if(deviceParameter.getMin()==null && deviceParameterView.getMin()!=null
//                            || (deviceParameter.getMax()==null && deviceParameterView.getMax()!=null)
//                            || ((deviceParameterView.getMax() == null || deviceParameterView.getMax().isEmpty()) && deviceParameter.getMax()!=null)
//                            || ((deviceParameterView.getMin() == null || deviceParameterView.getMin().isEmpty()) && deviceParameter.getMin()!=null)){
//                        deviceParameterM =null;
//                    }else {
//                        deviceParameterM = deviceParameterModelList.stream()
//                                .filter(deviceParameterModel ->
//                                        deviceParameterModel.getParameterMasterModel().equals(mappingModel) &&
//                                                deviceParameterView.getRegisterName().equals(deviceParameterModel.getRegisterName()) &&
//                                                deviceParameterView.getUnit().equals(deviceParameterModel.getUnit()) &&
//                                                deviceParameterView.getAddress().equals(deviceParameterModel.getAddress())
//                                ).findAny().orElse(null);
//                    }
                    deviceParameterM = deviceParameterModelList.stream()
                                .filter(deviceParameterModel ->
                                        deviceParameterModel.getParameterMasterModel().equals(mappingModel) &&
                                                deviceParameterView.getRegisterName().equals(deviceParameterModel.getRegisterName()) &&
                                                deviceParameterView.getAddress().equals(deviceParameterModel.getAddress())
                                ).findAny().orElse(null);
                    if (deviceParameterM == null) {
                        deviceParameterService.update(getParameterModel(deviceParameter, deviceModel, deviceParameterView, mappingModel));
                        existDeviceParameterModels.add(deviceParameter);
                    } else {
                        existDeviceParameterModels.add(deviceParameter);
                    }
                } else {
                    DeviceParameterModel deviceParameterModel = getParameterModel(new DeviceParameterModel(), deviceModel, deviceParameterView, mappingModel);
                    deviceParameterService.create(deviceParameterModel);
                }
            } else {
                DeviceParameterModel deviceParameterModel = getParameterModel(new DeviceParameterModel(), deviceModel, deviceParameterView, mappingModel);
                deviceParameterService.create(deviceParameterModel);
            }


//            DeviceParameterModel deviceParameter = deviceParameterModelList.stream().filter(deviceParameterModel -> deviceParameterModel.getParameterMasterModel().equals(mappingModel) && deviceParameterView.getRegisterName().equals(deviceParameterModel.getRegisterName()) && deviceParameterView.getUnit().equals(deviceParameterModel.getUnit()) && deviceParameterView.getAddress().equals(deviceParameterModel.getAddress())).findAny().orElse(null);
//            if (deviceParameter == null) {
//                DeviceParameterModel deviceParameterModel = getParameterModel(deviceModel, deviceParameterView, mappingModel);
//                deviceParameterService.create(deviceParameterModel);
//            } else {
//                existDeviceParameterModels.add(deviceParameter);
//            }
        }
        for (DeviceParameterModel deviceParameterModel : deviceParameterModelList) {
            if (!existDeviceParameterModels.contains(deviceParameterModel)) {
                toDeleteDeviceParameterModels.add(deviceParameterModel);
            }
        }
        if (!toDeleteDeviceParameterModels.isEmpty()) {
            deviceParameterService.hardDelete(toDeleteDeviceParameterModels);
        }
    }

    private static DeviceParameterModel getParameterModel(DeviceParameterModel deviceParameterModel, DeviceModel deviceModel, DeviceParameterView deviceParameterView, ParameterMasterModel mappingModel) {
        deviceParameterModel.setDeviceModel(deviceModel);
        deviceParameterModel.setParameterMasterModel(mappingModel);
        deviceParameterModel.setRegisterName(deviceParameterView.getRegisterName());
        deviceParameterModel.setAddress(deviceParameterView.getAddress());
        return deviceParameterModel;
    }

    @Override
    public Response doUpdate(DeviceView deviceView) throws EndlosiotAPIException {
        DeviceModel deviceModel = deviceService.get(deviceView.getId());
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        toModel(deviceModel, deviceView);
        deviceService.update(deviceModel);
        setDeviceParameterModelList(deviceModel, deviceView);
        return ViewResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage(),
                fromModel(deviceModel));
    }

    @Override
    public Response doDelete(Long deviceId) throws EndlosiotAPIException {
        DeviceModel deviceModel = deviceService.get(deviceId);
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        /*deviceModel.setArchive(true);
        deviceService.delete(deviceModel);*/

        //Case 1 No data available in Device Diagnosis Table
        PageModel deviceDataModels = deviceDiagnosisService.getByDevice(String.valueOf(deviceId), null, null, 0, 100);

        if (deviceDataModels.getList().isEmpty()) {

            //Case 2 No one any Device Parameter is used in any screen
            List<DeviceParameterModel> deviceParameters = deviceParameterService.getByDeviceModelDeviceId(String.valueOf(deviceId));
            boolean isDeletable = false;
            if ((deviceParameters != null && !deviceParameters.isEmpty())) {

                for (DeviceParameterModel deviceParameter : deviceParameters) {

                    List<CellModel> cellModels = cellMasterService.findByDeviceParameterId(deviceParameter.getId());
                    if ((cellModels.isEmpty())) {
                        isDeletable = true;
                        break;
                    } else {
                        isDeletable = false;
                    }
                }
            }

            if (isDeletable) {
                try {
                    deviceService.hardDelete(deviceId);
                    return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(), ResponseCode.DELETE_SUCCESSFULLY.getMessage());

                } catch (DataIntegrityViolationException dataIntegrityViolationException) {
                    LoggerService.exception(dataIntegrityViolationException);

                    throw new EndlosiotAPIException(ResponseCode.CANT_DELETE_DEVICE.getCode(), ResponseCode.CANT_DELETE_DEVICE.getMessage());
                }
            }
        }
        throw new EndlosiotAPIException(ResponseCode.CANT_DELETE_DEVICE.getCode(), ResponseCode.CANT_DELETE_DEVICE.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        DeviceModel deviceModel = deviceService.get(id);
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        Auditor.activationAudit(deviceModel, !deviceModel.isActive());
        deviceService.update(deviceModel);
        return CommonResponse.create(ResponseCode.ACTIVATION_SUCCESSFUL.getCode(), ResponseCode.ACTIVATION_SUCCESSFUL.getMessage());
    }

    @Override
    public DeviceModel toModel(DeviceModel deviceModel, DeviceView deviceView) throws EndlosiotAPIException {
        deviceModel.setName(deviceView.getName());
        if (deviceView.getImei() != null) {
            deviceModel.setImei(deviceView.getImei());
        }
        return deviceModel;
    }

    @Override
    protected DeviceModel getNewModel() {
        return new DeviceModel();
    }

    @Override
    public DeviceView fromModel(DeviceModel deviceModel) {
        DeviceView deviceView = DeviceView.builder().name(deviceModel.getName()).build();

        deviceView.setId(deviceModel.getId());
        deviceView.setName(deviceModel.getName());
        if (deviceModel.getImei() != null) {
            deviceView.setImei(deviceModel.getImei());
        }

        setDeviceView(deviceModel, deviceView);
        return deviceView;
    }

    private void setDeviceView(DeviceModel deviceModel, DeviceView deviceView) {
        List<DeviceParameterView> deviceParameterViews = new ArrayList<>();
        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceModel(deviceModel.getId());
        deviceParameterModelList.forEach(data -> {
            DeviceParameterView deviceParameterView = new DeviceParameterView();
            ParameterMasterModel parameterMasterModel = parameterMasterService.get(data.getParameterMasterModel().getId());
            ParameterMasterView parameterMasterView = new ParameterMasterView();
            parameterMasterView.setName(parameterMasterModel.getName());
            parameterMasterView.setId(parameterMasterModel.getId());
            deviceParameterView.setId(data.getId());
            deviceParameterView.setParameterMasterView(parameterMasterView);
            deviceParameterView.setRegisterName(data.getRegisterName());
            deviceParameterView.setParameterName(data.getParameterMasterModel().getName());
            deviceParameterView.setAddress(data.getAddress());
//            if(data.getMin()!=null){
//                deviceParameterView.setMin(data.getMin().toString());
//            }
//            if(data.getMax()!=null){
//                deviceParameterView.setMax(data.getMax().toString());
//            }
            deviceParameterViews.add(deviceParameterView);
        });
        deviceView.setDeviceParameterViewList(deviceParameterViews);
    }

    @Override
    public BaseService getService() {
        return deviceService;
    }

    @Override
    public Response doSearch(DeviceView deviceView, Integer start, Integer recordSize) {
        PageModel pageModel = deviceService.search(deviceView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), pageModel.getRecords(), fromModelList((List<DeviceModel>) pageModel.getList()));
    }

    @Override
    public Response doSearchDeviceData(DeviceDataView deviceDataView, Integer start, Integer recordSize) {
       /* PageModel pageModel = deviceDataService.search(deviceDataView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), pageModel.getRecords(), getDeviceDataViews(pageModel));*/
        return null;
    }

    @Override
    public Response doSetData(Long id) throws EndlosiotAPIException {
        UserModel userModel = userService.get(1);
        if (userModel != null) {
            userModel.setPlus(false);
            userModel.setAnalog1(false);
            userModel.setAnalog2(false);
            userModel.setModbus(false);
            if (id == 1) {
                userModel.setPlus(true);
            } else if (id == 2) {
                userModel.setAnalog1(true);
            } else if (id == 3) {
                userModel.setAnalog2(true);
            } else if (id == 4) {
                userModel.setModbus(true);
            } else {
                userModel.setPlus(false);
                userModel.setAnalog1(false);
                userModel.setAnalog2(false);
                userModel.setModbus(false);
            }
            userService.update(userModel);
        }
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    private static List<DeviceDataView> getDeviceDataViews(PageModel pageModel) {
        List<DeviceDataView> deviceDataViews = new ArrayList<>();
        for (DeviceDataModel deviceDataModel : (List<DeviceDataModel>) pageModel.getList()) {
            DeviceDataView deviceDataView = new DeviceDataView();
            deviceDataView.setId(deviceDataModel.getId());
            deviceDataView.setPlus(deviceDataModel.getPlus().toString());
            deviceDataView.setAnalog1(deviceDataModel.getAnalog1().toString());
            deviceDataView.setAnalog2(deviceDataModel.getAnalog2().toString());
            deviceDataView.setTurbodity(deviceDataModel.getTurbodity().toString());
            deviceDataView.setChlorine(deviceDataModel.getChlorine().toString());
            deviceDataView.setDateCreate(deviceDataModel.getDateCreate());
            deviceDataViews.add(deviceDataView);
        }
        return deviceDataViews;
    }

    @Override
    public Response getDropDownDevice() {
        List<DeviceView> deviceViewList = new ArrayList<>();
        List<DeviceModel> deviceModelList = deviceService.findAll();
        for (DeviceModel deviceModel : deviceModelList) {
            DeviceView deviceView = DeviceView.builder().name(deviceModel.getName()).build();
            deviceView.setId(deviceModel.getId());
            deviceViewList.add(deviceView);
        }

        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceModelList.size(), deviceViewList);
    }

    @Override
    public Response getDropDownDeviceParameter(String deviceId) {
        List<DeviceParameterView> deviceViewList = new ArrayList<>();
        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceModelDeviceId(deviceId);
        for (DeviceParameterModel deviceParameterModel : deviceParameterModelList) {
            DeviceParameterView deviceParameterView = DeviceParameterView.builder().parameterName(deviceParameterModel.getParameterMasterModel().getName()).build();
            deviceParameterView.setId(deviceParameterModel.getId());
            deviceParameterView.setRegisterName(deviceParameterModel.getRegisterName());
            deviceParameterView.setAddress(deviceParameterModel.getAddress());
            deviceViewList.add(deviceParameterView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceViewList.size(), deviceViewList);
    }

    @Autowired
    RawDataService rawDataService;

    @Override
    public Response gatewayInfo(String deviceId, Integer start, Integer recordSize) throws EndlosiotAPIException {
        PageModel rawDataModels = rawDataService.getByDevice(deviceId, start, recordSize);
        List<RawDataView> rawDataViews = new ArrayList<>();
        for (RawDataModel rawDataModel : (List<RawDataModel>) rawDataModels.getList()) {
            RawDataView rawDataView = new RawDataView();
            rawDataView.setId(rawDataModel.getId());
            if (rawDataModel.getFlow() != null) {
                rawDataView.setFlow(rawDataModel.getFlow().toString());
            }
            if (rawDataModel.getCholrine() != null) {
                rawDataView.setCholrine(rawDataModel.getCholrine().toString());
            }
            if (rawDataModel.getTurbidity() != null) {
                rawDataView.setTurbidity(rawDataModel.getTurbidity().toString());
            }
            if (rawDataModel.getLevelSensor() != null) {
                rawDataView.setLevelSensor(rawDataModel.getLevelSensor().toString());
            }
            if (rawDataModel.getPressureTransmitter() != null) {
                rawDataView.setPressureTransmitter(rawDataModel.getPressureTransmitter().toString());
            }
            if (rawDataModel.getDigitalOutput() != null) {
                rawDataView.setDigitalOutput(rawDataModel.getDigitalOutput().toString());
            }
            DeviceView deviceView = new DeviceView();
            deviceView.setImei(rawDataModel.getDeviceModel().getImei());
            rawDataView.setDeviceView(deviceView);
            rawDataView.setCreateDate(rawDataModel.getCreateDate());
            rawDataViews.add(rawDataView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), rawDataModels.getRecords(), rawDataViews);
    }

    @Override
    public Response gatDeviceDiagnosis(String deviceId, Integer start, Integer recordSize) throws EndlosiotAPIException {
        PageModel rawDataModels = rawDataService.getByDevice(deviceId, start, recordSize);
        List<RawDataView> rawDataViews = new ArrayList<>();
        for (RawDataModel rawDataModel : (List<RawDataModel>) rawDataModels.getList()) {
            RawDataView rawDataView = new RawDataView();
            rawDataView.setId(rawDataModel.getId());
            if (rawDataModel.getFlow() != null) {
                rawDataView.setFlow(rawDataModel.getFlow().toString());
            }
            if (rawDataModel.getCholrine() != null) {
                rawDataView.setCholrine(rawDataModel.getCholrine().toString());
            }
            if (rawDataModel.getTurbidity() != null) {
                rawDataView.setTurbidity(rawDataModel.getTurbidity().toString());
            }
            if (rawDataModel.getLevelSensor() != null) {
                rawDataView.setLevelSensor(rawDataModel.getLevelSensor().toString());
            }
            if (rawDataModel.getPressureTransmitter() != null) {
                rawDataView.setPressureTransmitter(rawDataModel.getPressureTransmitter().toString());
            }
            if (rawDataModel.getDigitalOutput() != null) {
                rawDataView.setDigitalOutput(rawDataModel.getDigitalOutput().toString());
            }
            DeviceView deviceView = new DeviceView();
            deviceView.setImei(rawDataModel.getDeviceModel().getImei());
            rawDataView.setDeviceView(deviceView);
            rawDataView.setCreateDate(rawDataModel.getCreateDate());
            rawDataViews.add(rawDataView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), rawDataModels.getRecords(), rawDataViews);
    }

    @Override
    public Response parameterWiseData(Long locationId, String parameterId, Integer start, Integer recordSize) throws EndlosiotAPIException {
        ParameterWiseDataView parameterWiseDataView = new ParameterWiseDataView();
        List<DeviceModel> deviceModels = new ArrayList<>();
        /*if (locationId != null) {
            LocationModel locationModel = locationService.get(locationId);
            if (locationModel == null) {
                throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
            }
            deviceModels = deviceService.getByLocation(locationModel.getId());
        } else*/
        {
            deviceModels = deviceService.findAll();
        }
        if (deviceModels == null || deviceModels.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        switch (parameterId) {
            case "1":
                processFlowMeterData(deviceModels, parameterWiseDataView);
                break;
            case "2":
                processChlorineSensorData(deviceModels, parameterWiseDataView);
                break;
            case "3":
                processTurbiditySensorData(deviceModels, parameterWiseDataView);
                break;
            case "4":
                processPressureTransmitterData(deviceModels, parameterWiseDataView);
                break;
            case "5":
                processLevelSensorData(deviceModels, parameterWiseDataView);
                break;
            default:
                break;
        }
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), parameterWiseDataView);

    }

    private void processLevelSensorData(List<DeviceModel> deviceModels, ParameterWiseDataView parameterWiseDataView) {
        List<LevelSensorView> levelSensorViews = new ArrayList<>();
        for (DeviceModel deviceModel : deviceModels) {
            LevelSensorView levelSensorView = new LevelSensorView();
            DeviceParameterModel deviceParameterModel = deviceParameterService.getByDeviceAndParameter(deviceModel.getId(), 5L);
            if (deviceParameterModel != null) {
                levelSensorView.setName(deviceParameterModel.getRegisterName());
                LocalDate today = LocalDate.now();
                Long startEpoch = DateUtility.getStartEpoch(today);
                Long endEpoch = DateUtility.getEndEpoch(today);
                List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                if (rawDataModels != null && !rawDataModels.isEmpty()) {
//                            int lowLevelTank = 0;
//                            int highLevelTank = 0;
//                            for (RawDataModel rawDataModel : rawDataModels){
//                                if(rawDataModel.getLevelSensor().equals(0)){
//                                    lowLevelTank++;
//                                }else{
//                                    highLevelTank++;
//                                }
//                            }
//                            levelSensorView.setNoOfLowTanks(String.valueOf(lowLevelTank));
//                            levelSensorView.setNoOfHignTanks(String.valueOf(highLevelTank));
                    BigDecimal level = rawDataModels.get(rawDataModels.size() - 1).getLevelSensor();
                    if (level.compareTo(BigDecimal.ZERO) == 0) {
                        levelSensorView.setHigh(false);
                    } else {
                        levelSensorView.setHigh(true);
                    }
                }
                DeviceView deviceView = new DeviceView();
                deviceView.setImei(deviceModel.getImei());
                deviceView.setId(deviceModel.getId());
                levelSensorView.setDeviceView(deviceView);
                levelSensorViews.add(levelSensorView);
            }
        }
        parameterWiseDataView.setLevelSensorViews(levelSensorViews);
    }

    private void processPressureTransmitterData(List<DeviceModel> deviceModels, ParameterWiseDataView parameterWiseDataView) {
        List<PressureTransmitterView> pressureTransmitterViews = new ArrayList<>();
        for (DeviceModel deviceModel : deviceModels) {
            PressureTransmitterView pressureTransmitterView = new PressureTransmitterView();
            DeviceParameterModel deviceParameterModel = deviceParameterService.getByDeviceAndParameter(deviceModel.getId(), 4L);
            if (deviceParameterModel != null) {
                pressureTransmitterView.setName(deviceParameterModel.getRegisterName());
                LocalDate today = LocalDate.now();
                Long startEpoch = DateUtility.getStartEpoch(today);
                Long endEpoch = DateUtility.getEndEpoch(today);
                List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                if (rawDataModels != null && !rawDataModels.isEmpty()) {
//                            BigDecimal minValue = rawDataModels.stream()
//                                    .map(RawDataModel::getPressureTransmitter)
//                                    .min(Comparator.naturalOrder()).orElse(null);
//                            BigDecimal maxValue = rawDataModels.stream()
//                                    .map(RawDataModel::getPressureTransmitter)
//                                    .max(Comparator.naturalOrder()).orElse(null);
//                            minValue = scaleData(minValue);
//                            maxValue = scaleData(maxValue);
//                            pressureTransmitterView.setMinVale(minValue.toString());
//                            pressureTransmitterView.setMaxValue(maxValue.toString());
                    BigDecimal currentValue = scaleData(rawDataModels.get(rawDataModels.size() - 1).getPressureTransmitter());
                    pressureTransmitterView.setCurrentValue(currentValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                }
                DeviceView deviceView = new DeviceView();
                deviceView.setImei(deviceModel.getImei());
                deviceView.setId(deviceModel.getId());
                pressureTransmitterView.setDeviceView(deviceView);
                pressureTransmitterViews.add(pressureTransmitterView);
            }
        }
        parameterWiseDataView.setPressureTransmitterViews(pressureTransmitterViews);
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
            BigDecimal scaledValue = BigDecimal.ONE.add(value.subtract(lowerBound).multiply(maxValue.subtract(minValue)).divide(upperBound.subtract(lowerBound), 10, BigDecimal.ROUND_HALF_UP));
            return scaledValue;
        }
    }

    private void processTurbiditySensorData(List<DeviceModel> deviceModels, ParameterWiseDataView parameterWiseDataView) {
        List<TurbiditySensorView> turbiditySensorViews = new ArrayList<>();
        for (DeviceModel deviceModel : deviceModels) {
            TurbiditySensorView turbiditySensorView = new TurbiditySensorView();
            DeviceParameterModel deviceParameterModel = deviceParameterService.getByDeviceAndParameter(deviceModel.getId(), 3L);
            if (deviceParameterModel != null) {
                turbiditySensorView.setName(deviceParameterModel.getRegisterName());
                LocalDate today = LocalDate.now();
                Long startEpoch = DateUtility.getStartEpoch(today);
                Long endEpoch = DateUtility.getEndEpoch(today);
                List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                if (rawDataModels != null && !rawDataModels.isEmpty()) {
                    if (rawDataModels.get(rawDataModels.size() - 1).getTurbidity() != null) {
                        turbiditySensorView.setAvgValue(rawDataModels.get(rawDataModels.size() - 1).getTurbidity().toString());
                    }
                }
                DeviceView deviceView = new DeviceView();
                deviceView.setImei(deviceModel.getImei());
                deviceView.setId(deviceModel.getId());
                turbiditySensorView.setDeviceView(deviceView);
                turbiditySensorViews.add(turbiditySensorView);
            }
        }
        parameterWiseDataView.setTurbiditySensorViews(turbiditySensorViews);
    }

    @Override
    public List<DeviceView> fromModelList(List<DeviceModel> models) {
        return super.fromModelList(models);
    }

    private void processChlorineSensorData(List<DeviceModel> deviceModels, ParameterWiseDataView parameterWiseDataView) {
        List<ChlorineSensorView> chlorineSensorViews = new ArrayList<>();
        for (DeviceModel deviceModel : deviceModels) {
            ChlorineSensorView chlorineSensorView = new ChlorineSensorView();
            DeviceParameterModel deviceParameterModel = deviceParameterService.getByDeviceAndParameter(deviceModel.getId(), 2L);
            if (deviceParameterModel != null) {
                chlorineSensorView.setName(deviceParameterModel.getRegisterName());
                LocalDate today = LocalDate.now();
                Long startEpoch = DateUtility.getStartEpoch(today);
                Long endEpoch = DateUtility.getEndEpoch(today);
                List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                if (rawDataModels != null && !rawDataModels.isEmpty()) {
                    if (rawDataModels.get(rawDataModels.size() - 1).getCholrine() != null) {
                        chlorineSensorView.setAvgValue(rawDataModels.get(rawDataModels.size() - 1).getCholrine().toString());
                    }
                }
                DeviceView deviceView = new DeviceView();
                deviceView.setImei(deviceModel.getImei());
                deviceView.setId(deviceModel.getId());
                chlorineSensorView.setDeviceView(deviceView);
                chlorineSensorViews.add(chlorineSensorView);
            }
        }
        parameterWiseDataView.setChlorineSensorViews(chlorineSensorViews);
    }

    private void processFlowMeterData(List<DeviceModel> deviceModels, ParameterWiseDataView parameterWiseDataView) {
        List<FlowmeterView> flowmeterViews = new ArrayList<>();
        for (DeviceModel deviceModel : deviceModels) {
            FlowmeterView flowMeterView = new FlowmeterView();
            DeviceParameterModel deviceParameterModel = deviceParameterService.getByDeviceAndParameter(deviceModel.getId(), 1L);
            if (deviceParameterModel != null) {
                flowMeterView.setName(deviceParameterModel.getRegisterName());
                LocalDate today = LocalDate.now();
                Long startEpoch = DateUtility.getStartEpoch(today);
                Long endEpoch = DateUtility.getEndEpoch(today);
                List<RawDataModel> rawDataModels = rawDataService.getTodaysData(deviceModel.getImei(), startEpoch, endEpoch);
                if (rawDataModels != null && !rawDataModels.isEmpty()) {
//                            BigDecimal firstValue = rawDataModels.get(0).getFlow();
                    BigDecimal lastValue = rawDataModels.get(rawDataModels.size() - 1).getFlow();
//                            flowMeterView.setTodayVale(lastValue.subtract(firstValue).toString());
                    BigDecimal totalFlow = lastValue.multiply(new BigDecimal(10)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    flowMeterView.setTodayVale(Long.toString(totalFlow.longValue()));
                }
                LocalDate yesterDay = LocalDate.now().minusDays(1);
                Long yesterDayStartEpoch = DateUtility.getStartEpoch(yesterDay);
                Long yesterDayeEndEpoch = DateUtility.getEndEpoch(yesterDay);
                List<RawDataModel> yesterDayeModels = rawDataService.getTodaysData(deviceModel.getImei(), yesterDayStartEpoch, yesterDayeEndEpoch);
                if (yesterDayeModels != null && !yesterDayeModels.isEmpty()) {
//                            BigDecimal firstValue = yesterDayeModels.get(0).getFlow();
                    BigDecimal lastValue = yesterDayeModels.get(yesterDayeModels.size() - 1).getFlow();
//                            flowMeterView.setPreviousValue(lastValue.subtract(firstValue).toString());
                    BigDecimal yesterDayFlow = lastValue.multiply(new BigDecimal(10)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    flowMeterView.setPreviousValue(Long.toString(yesterDayFlow.longValue()));
                }
                DeviceView deviceView = new DeviceView();
                deviceView.setImei(deviceModel.getImei());
                deviceView.setId(deviceModel.getId());
                flowMeterView.setDeviceView(deviceView);
                flowmeterViews.add(flowMeterView);
            }
        }
        parameterWiseDataView.setFlowMeterViews(flowmeterViews);
    }

    @Override
    public Response doGetLatitudeLongitude(Long locationId) throws EndlosiotAPIException {
        /*LocationModel locationModel = locationService.get(locationId);
        if (locationModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }*/
        LocationModel locationModel = new LocationModel();
        List<DeviceModel> deviceModels = null; //deviceService.getByLocation(locationModel.getId());
        List<DeviceView> deviceViews = new ArrayList<>();
        if (deviceModels != null && !deviceModels.isEmpty()) {
            for (DeviceModel deviceModel : deviceModels) {
                DeviceView deviceView = new DeviceView();
                deviceView.setId(deviceModel.getId());
                deviceView.setName(deviceModel.getName());
                setDeviceView(deviceModel, deviceView);
                deviceViews.add(deviceView);
            }
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceViews.size(), deviceViews);
    }

    private static RawDataView getRawDataView(RawDataModel rawDataModel, ParameterMasterModel masterModel) {
        RawDataView rawDataView = new RawDataView();
        rawDataView.setId(rawDataModel.getId());
        if (masterModel.getId() == 1) {
            rawDataView.setFlow(rawDataModel.getFlow().toString());
        } else if (masterModel.getId() == 2) {
            rawDataView.setCholrine(rawDataModel.getCholrine().toString());
        } else if (masterModel.getId() == 3) {
            rawDataView.setTurbidity(rawDataModel.getTurbidity().toString());
        } else if (masterModel.getId() == 4) {
            rawDataView.setPressureTransmitter(rawDataModel.getPressureTransmitter().toString());
        } else if (masterModel.getId() == 5) {
            rawDataView.setLevelSensor(rawDataModel.getLevelSensor().toString());
        }
        rawDataView.setCreateDate(rawDataModel.getCreateDate());
        return rawDataView;
    }

    @Override
    public Response doGetExcelReport(GraphDataView graphDataView) throws EndlosiotAPIException {
        ParameterMasterModel masterModel = parameterMasterService.get(graphDataView.getParameterMasterView().getId());
        if (masterModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        List<RawDataModel> rawDataModelList = getReportData(graphDataView);
        if (rawDataModelList == null || rawDataModelList.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }

        String newFileName = "DashboardReportFile" + DateUtility.getCurrentEpoch() + ".xlsx";
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet realSheet = workbook.createSheet("DashboardReportFile");

            for (int i = 0; i < 4; i++) {
                realSheet.setColumnWidth(i, 256 * 35);
                realSheet.autoSizeColumn(i);
            }

            CellStyle style = workbook.createCellStyle();

            Row firstRow = realSheet.createRow(0);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);

            Font firstFont = workbook.createFont();
            firstFont.setBold(true);
            firstFont.setFontHeightInPoints((short) 10);
            style.setFont(firstFont);

            Row secondRow = realSheet.createRow(1);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            style.setBorderTop(BorderStyle.MEDIUM);

            for (int i = 0; i < 4; i++) {
                Cell cell = firstRow.createCell(i);
                cell.setCellStyle(style);
            }

            for (int i = 0; i < 4; i++) {
                Cell cell = secondRow.createCell(i);
                cell.setCellStyle(style);
            }
            LocalDate Date = LocalDate.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String downloadedDate = Date.format(dateTimeFormatter);
            DeviceModel deviceModel = deviceService.get(graphDataView.getDeviceView().getId());
            if (deviceModel == null) {
                throw new EndlosiotAPIException(ResponseCode.DEVICE_NAME_IS_MISSING.getCode(),
                        ResponseCode.DEVICE_NAME_IS_MISSING.getMessage());
            }
            ParameterMasterModel parameterMasterModel = parameterMasterService.get(graphDataView.getParameterMasterView().getId());
            realSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 1));
            Cell firstRowCell0 = firstRow.getCell(0);


            realSheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 3));
            Cell firstRowCell2 = firstRow.getCell(2);
            firstRowCell2.setCellValue("Parameter Name:" + " " + parameterMasterModel.getName());

            realSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
            Cell secondRowCell0 = secondRow.getCell(0);

            realSheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
            Cell secondRowCell2 = secondRow.getCell(2);
            secondRowCell2.setCellValue("Downloaded Date:" + " " + downloadedDate);

            // Create header row
            Row headerRow = realSheet.createRow(2);
            String[] headers = {"Sr.No", "Create Date", "Time", "Value"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                CellStyle headerCellStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
                headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerCellStyle.setFont(headerFont);
                cell.setCellStyle(headerCellStyle);
            }
            // Populate data rows
            int rowCount = 3;
            int srno = 1;
            for (RawDataModel rawDataModel : rawDataModelList) {
                Row dataRow = realSheet.createRow(rowCount++);
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
                headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                dataRow.setRowStyle(headerCellStyle);
                dataRow.createCell(0).setCellValue(srno++);

                long createDateMillis = rawDataModel.getCreateDate() * 1000;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                String time = timeFormat.format(createDateMillis);
                String formattedDate = dateFormat.format(new Date(createDateMillis));

                dataRow.createCell(1).setCellValue(formattedDate);
                dataRow.createCell(2).setCellValue(time);
                Double value = getValueForParameter(rawDataModel, Math.toIntExact(graphDataView.getParameterMasterView().getId()));
                dataRow.createCell(3).setCellValue(value);
            }
            // Adjust column widths
            for (int i = 0; i < headers.length; i++) {
                realSheet.autoSizeColumn(i);
            }

            // Download Excel
            FileUtility.downloadExcel(newFileName, workbook);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private Double getValueForParameter(RawDataModel rawDataModel, int parameterId) {
        return switch (parameterId) {
            case 1 -> rawDataModel.getFlow().doubleValue();
            case 2 -> rawDataModel.getCholrine().doubleValue();
            case 3 -> rawDataModel.getTurbidity().doubleValue();
            case 4 -> rawDataModel.getPressureTransmitter().doubleValue();
            case 5 -> rawDataModel.getLevelSensor().doubleValue();
            default -> BigDecimal.ONE.doubleValue(); // Or handle appropriately for unknown parameters
        };
    }

    @Override
    public Response doGetParameterWiseReport(GraphDataView graphDataView) throws EndlosiotAPIException {
        ParameterMasterModel masterModel = parameterMasterService.get(graphDataView.getParameterMasterView().getId());
        if (masterModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }

        List<RawDataModel> rawDataModelList = getReportData(graphDataView);
        List<RawDataView> rawDataViews = new ArrayList<>();
        if (rawDataModelList != null && !rawDataModelList.isEmpty()) {
            for (RawDataModel rawDataModel : rawDataModelList) {
                RawDataView rawDataView = getRawDataView(rawDataModel, masterModel);
                rawDataViews.add(rawDataView);
            }
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), rawDataViews.size(), rawDataViews);
    }

    private List<RawDataModel> getReportData(GraphDataView graphDataView) throws EndlosiotAPIException {
        List<RawDataModel> rawDataModelList = new ArrayList<>();
        DeviceModel deviceModel = deviceService.get(graphDataView.getDeviceView().getId());
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //Date Range
        if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.DATE_RANGE.getId()
                && graphDataView.getEndDate() != null) {
            rawDataModelList = getRawDataDateRange(graphDataView, deviceModel);
        }
        // Get Daily Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.DAILY.getId()) {
            rawDataModelList = getRawDataDailyWise(graphDataView, deviceModel);
        }
        // Get Weekly Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.WEEKLY.getId()) {
            rawDataModelList = getRawDataWeeklyWise(graphDataView, deviceModel);

        }
        //  Get Monthly Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.MONTHLY.getId()) {
            rawDataModelList = getRawDataModelMonthlyWise(graphDataView, deviceModel);
        }
        return rawDataModelList;
    }

    private List<RawDataModel> getRawDataDateRange(GraphDataView graphDataView, DeviceModel deviceModel) throws EndlosiotAPIException {
        List<RawDataModel> rawDataModelList;
        LocalDate startLocalDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        LocalDate endLocalDate = DateUtility.getLocalDate(graphDataView.getEndDate());
        if (startLocalDate == null || endLocalDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        // Get the start of the day
        LocalDateTime startOfTheDay = startLocalDate.atStartOfDay();
        // Get the end of the day
        LocalDateTime endOfTheDay = endLocalDate.atTime(LocalTime.MAX);
        // Converting the startOfTheDay and endOfTheDay to Long for setting it into method
        long starOfDay = startOfTheDay.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfDay = endOfTheDay.toEpochSecond(java.time.OffsetTime.now().getOffset());
        rawDataModelList = rawDataService.getTodaysData(deviceModel.getImei(), starOfDay, endOfDay);
        return rawDataModelList;
    }

    private List<RawDataModel> getRawDataDailyWise(GraphDataView graphDataView, DeviceModel deviceModel) throws EndlosiotAPIException {
        List<RawDataModel> rawDataModelList;
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        // Get the start of the day
        LocalDateTime startOfTheDay = localDate.atStartOfDay();
        // Get the end of the day
        LocalDateTime endOfTheDay = localDate.atTime(LocalTime.MAX);
        // Converting the startOfTheDay and endOfTheDay to Long for setting it into method
        long starOfDay = startOfTheDay.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfDay = endOfTheDay.toEpochSecond(java.time.OffsetTime.now().getOffset());
        rawDataModelList = rawDataService.getTodaysData(deviceModel.getImei(), starOfDay, endOfDay);
        return rawDataModelList;
    }

    private List<RawDataModel> getRawDataWeeklyWise(GraphDataView graphDataView, DeviceModel deviceModel) throws EndlosiotAPIException {
        List<RawDataModel> rawDataModelList;
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);
        //Converting both dates to long
        long startOfWeekInMillis = startOfWeekDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfWeekInMillis = endOfWeekDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        rawDataModelList = rawDataService.getAllByDevice(deviceModel.getId(), startOfWeekInMillis, endOfWeekInMillis);
        return rawDataModelList;
    }

    private List<RawDataModel> getRawDataModelMonthlyWise(GraphDataView graphDataView, DeviceModel deviceModel) throws EndlosiotAPIException {
        List<RawDataModel> rawDataModelList;
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        LocalDate firstDayOfCurrentMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfCurrentMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startOfCurrentMonth = firstDayOfCurrentMonth.atStartOfDay();
        LocalDateTime endOfCurrentMonth = lastDayOfCurrentMonth.atTime(LocalTime.MAX);
        long startOfMonth = startOfCurrentMonth.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfMonth = endOfCurrentMonth.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        rawDataModelList = rawDataService.getAllByDevice(deviceModel.getId(), startOfMonth, endOfMonth);
        return rawDataModelList;
    }


    @Value("${datasource.url}")
    private String databaseUrl;

    @Value("${datasource.user}")
    private String user;

    @Value("${datasource.password}")
    private String databasePassword;

    @Value("${datasource.databaseName}")
    private String databaseName;

    @Override
    public Response doGetParameterWiseChart(GraphDataView graphDataView) throws EndlosiotAPIException {
        List<RawDataChartView> rawDataChartViewList = getChartData(graphDataView);
        if (rawDataChartViewList == null || rawDataChartViewList.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                rawDataChartViewList.size(), rawDataChartViewList);
    }

    private List<RawDataChartView> getChartData(GraphDataView graphDataView) throws EndlosiotAPIException {

        ParameterMasterModel masterModel = parameterMasterService.get(graphDataView.getParameterMasterView().getId());
        if (masterModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }

        DeviceModel deviceModel = deviceService.get(graphDataView.getDeviceView().getId());
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //Date Range
        if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.DATE_RANGE.getId()
                && graphDataView.getEndDate() != null) {
            return getRawDataDateRangeChart(graphDataView, deviceModel, masterModel);
        }
        // Get Daily Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.DAILY.getId()) {
            //  Get the current date
            return getRawDataDailyWiseChart(graphDataView, deviceModel, masterModel);
        }
        // Get Weekly Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.WEEKLY.getId()) {
            // Get the Week Date
            return getRawDataWeeklyWiseChart(graphDataView, deviceModel, masterModel);
        }
        //        Get Monthly Report
        else if (graphDataView.getReportType().getKey().intValue() == ReportTypeEnum.MONTHLY.getId()) {
            return getRawDataMonthWiseChart(graphDataView, deviceModel, masterModel);
        } else {
            return null;
        }
    }

    private List<RawDataChartView> getRawDataDateRangeChart(GraphDataView graphDataView, DeviceModel deviceModel, ParameterMasterModel masterModel) throws EndlosiotAPIException {

        List<RawDataChartView> rawDataChartViewList = new ArrayList<>();
        LocalDate startLocalDate = DateUtility.getLocalDate(graphDataView.getStartDate());

        LocalDate endLocalDate = DateUtility.getLocalDate(graphDataView.getEndDate());
        if (startLocalDate == null || endLocalDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }

        // Get the start of the day
        LocalDateTime startOfTheDay = startLocalDate.atStartOfDay();
        // Get the end of the day
        LocalDateTime endOfTheDay = endLocalDate.atTime(LocalTime.MAX);
        // Converting the startOfTheDay and endOfTheDay to Long for setting it into method
        long starOfDay = startOfTheDay.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfDay = endOfTheDay.toEpochSecond(java.time.OffsetTime.now().getOffset());

        String cleanedName = getParameterNameWithoutSpaceAndInLowerCase(masterModel);

        String mainQuery = "SELECT EXTRACT(DAY FROM TO_TIMESTAMP(createdate)) AS keydata," +
                " SUM(" + cleanedName + ") AS sumdata" +
                " FROM rawdata" +
                " WHERE fkdeviceid = " + deviceModel.getId() + " " +
                " AND createdate >= " + starOfDay +
                " AND createdate <=" + endOfDay +
                " GROUP BY EXTRACT(DAY FROM TO_TIMESTAMP(createdate))" +
                " order by keydata";
        return getCalculatedChartValue(mainQuery, rawDataChartViewList);
    }

    private List<RawDataChartView> getRawDataDailyWiseChart(GraphDataView graphDataView, DeviceModel deviceModel, ParameterMasterModel masterModel) throws EndlosiotAPIException {

        List<RawDataChartView> rawDataChartViewList = new ArrayList<>();
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        // Get the start of the day
        LocalDateTime startOfTheDay = localDate.atStartOfDay();
        // Get the end of the day
        LocalDateTime endOfTheDay = localDate.atTime(LocalTime.MAX);
        // Converting the startOfTheDay and endOfTheDay to Long for setting it into method
        long starOfDay = startOfTheDay.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfDay = endOfTheDay.toEpochSecond(java.time.OffsetTime.now().getOffset());
        String cleanedName = getParameterNameWithoutSpaceAndInLowerCase(masterModel);

        String mainQuery = "SELECT EXTRACT(HOUR FROM TO_TIMESTAMP(createdate)) AS keydata," +
                " SUM(" + cleanedName + ") AS sumdata" +
                " FROM rawdata" +
                " WHERE fkdeviceid = " + deviceModel.getId() + " " +
                " AND createdate >= " + starOfDay +
                " AND createdate <=" + endOfDay +
                " GROUP BY EXTRACT(HOUR FROM TO_TIMESTAMP(createdate))" +
                " order by keydata";
        return getCalculatedChartValue(mainQuery, rawDataChartViewList);
    }


    private List<RawDataChartView> getRawDataWeeklyWiseChart(GraphDataView graphDataView, DeviceModel deviceModel, ParameterMasterModel masterModel) throws EndlosiotAPIException {

        List<RawDataChartView> rawDataChartViewList = new ArrayList<>();
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        LocalDateTime startOfWeekDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endOfWeekDateTime = endOfWeek.atTime(LocalTime.MAX);
        //Converting both dates to long
        long startOfWeekInMillis = startOfWeekDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfWeekInMillis = endOfWeekDateTime.toEpochSecond(java.time.OffsetDateTime.now().getOffset());

        String cleanedName = getParameterNameWithoutSpaceAndInLowerCase(masterModel);

        String mainQuery = "SELECT EXTRACT(DOW FROM TO_TIMESTAMP(createdate)) AS keydata,\n" +
                " SUM(" + cleanedName + ") AS sumdata" +
                " FROM rawdata" +
                " WHERE fkdeviceid = " + deviceModel.getId() + " " +
                " AND createdate >= " + startOfWeekInMillis +
                " AND createdate <=" + endOfWeekInMillis +
                " GROUP BY EXTRACT(DOW FROM TO_TIMESTAMP(createdate))" +
                " order by keydata";
        return getCalculatedChartValue(mainQuery, rawDataChartViewList);
    }

    private List<RawDataChartView> getRawDataMonthWiseChart(GraphDataView graphDataView, DeviceModel deviceModel, ParameterMasterModel masterModel) throws EndlosiotAPIException {

        List<RawDataChartView> rawDataChartViewList = new ArrayList<>();
        LocalDate localDate = DateUtility.getLocalDate(graphDataView.getStartDate());
        if (localDate == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        LocalDate firstDayOfCurrentMonth = localDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfCurrentMonth = localDate.with(TemporalAdjusters.lastDayOfMonth());

        LocalDateTime startOfCurrentMonth = firstDayOfCurrentMonth.atStartOfDay();
        LocalDateTime endOfCurrentMonth = lastDayOfCurrentMonth.atTime(LocalTime.MAX);
//            Converting into long
        long startOfMonth = startOfCurrentMonth.toEpochSecond(java.time.OffsetDateTime.now().getOffset());
        long endOfMonth = endOfCurrentMonth.toEpochSecond(java.time.OffsetDateTime.now().getOffset());

        String cleanedName = getParameterNameWithoutSpaceAndInLowerCase(masterModel);

        String mainQuery = "SELECT EXTRACT(DAY FROM TO_TIMESTAMP(createdate)) AS keydata,\n" +
                " SUM(" + cleanedName + ") AS sumdata" +
                " FROM rawdata" +
                " WHERE fkdeviceid = " + deviceModel.getId() + " " +
                " AND createdate >= " + startOfMonth +
                " AND createdate <=" + endOfMonth +
                " GROUP BY EXTRACT(DAY FROM TO_TIMESTAMP(createdate))" +
                " order by keydata";
        return getCalculatedChartValue(mainQuery, rawDataChartViewList);
    }

    private static String getParameterNameWithoutSpaceAndInLowerCase(ParameterMasterModel masterModel) {
        String originalName = masterModel.getName();
        String cleanedName = originalName.replaceAll("[^a-zA-Z0-9]", "");
        cleanedName = cleanedName.replaceAll("\\s", "");
        cleanedName = cleanedName.toLowerCase();
        return cleanedName;
    }

    private List<RawDataChartView> getCalculatedChartValue(String mainQuery, List<RawDataChartView> rawDataChartViewList) {
        String url = databaseUrl + "/" + databaseName;
        String username = user;
        String password = databasePassword;

        try (
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement(mainQuery);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                RawDataChartView rawDataChartView = new RawDataChartView();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = resultSet.getObject(i);
                    if (Objects.equals(columnName, "keydata")) {
                        if (value instanceof BigDecimal) {
                            rawDataChartView.setKeyData(((BigDecimal) value).longValue());
                        }
                    }
                    if (Objects.equals(columnName, "sumdata")) {
                        if (value instanceof BigDecimal) {
                            rawDataChartView.setSumData(((BigDecimal) value).doubleValue());
                        }
                    }
                }
                rawDataChartViewList.add(rawDataChartView);
            }
            return rawDataChartViewList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response doParameterIdWiseList(DeviceView deviceParameterIds, Long id) throws EndlosiotAPIException {
        DeviceModel deviceModel = deviceService.get(id);
        if(deviceModel==null){
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        DeviceView deviceView = DeviceView.builder().name(deviceModel.getName()).build();

        List<DeviceParameterView> deviceParameterViews = new ArrayList<>();
        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceParameter(deviceParameterIds.getDeviceParameterViewList());
        deviceParameterModelList.forEach(data -> {
            DeviceParameterView deviceParameterView = new DeviceParameterView();
            ParameterMasterModel parameterMasterModel = parameterMasterService.get(data.getParameterMasterModel().getId());
            ParameterMasterView parameterMasterView = new ParameterMasterView();
            parameterMasterView.setName(parameterMasterModel.getName());
            parameterMasterView.setId(parameterMasterModel.getId());
            deviceParameterView.setId(data.getId());
            deviceParameterView.setParameterMasterView(parameterMasterView);
            deviceParameterView.setRegisterName(data.getRegisterName());
            deviceParameterView.setParameterName(data.getParameterMasterModel().getName());
            deviceParameterView.setAddress(data.getAddress());
            deviceParameterViews.add(deviceParameterView);
        });
        deviceView.setDeviceParameterViewList(deviceParameterViews);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceView);
    }
}
