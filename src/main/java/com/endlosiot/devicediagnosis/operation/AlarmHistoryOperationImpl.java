package com.endlosiot.devicediagnosis.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.operation.FileOperation;
import com.endlosiot.common.file.service.FileService;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.service.UserService;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.operation.DeviceOperationImpl;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.device.service.DeviceService;
import com.endlosiot.device.view.DeviceParameterView;
import com.endlosiot.device.view.DeviceView;
import com.endlosiot.devicediagnosis.model.AlarmHistoryModel;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;
import com.endlosiot.devicediagnosis.service.AlarmHistoryService;
import com.endlosiot.devicediagnosis.service.DeviceDiagnosisService;
import com.endlosiot.devicediagnosis.view.AlarmHistoryView;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisDataView;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisView;
import com.endlosiot.parametermaster.service.ParameterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component(value = "alarmHistoryOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AlarmHistoryOperationImpl implements AlarmHistoryOperation {
    @Autowired
    private AlarmHistoryService alarmHistoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterMasterService parameterMasterService;
    @Autowired
    DeviceParameterService deviceParameterService;
    @Autowired
    DeviceService deviceService;
    @Autowired
    DeviceOperationImpl deviceOperation;
    @Override
    public Response gatDeviceDiagnosis(String deviceId, String startDate, String endDate, Integer start, Integer recordSize) throws EndlosiotAPIException {

        DeviceModel deviceModel = deviceService.get(Long.valueOf(deviceId));
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        PageModel alarmHistoryModels = null;
        Long sDate = null, eDate = null;

        try {
            if (!startDate.equals("undefined") || !startDate.equals("NaN")) {
                sDate = Long.parseLong(startDate);
            }

        } catch (NumberFormatException e) {
            sDate = null;
        }

        try {
            if (!endDate.equals("undefined") || !endDate.equals("NaN")) {
                eDate = Long.parseLong(endDate);
            }

        } catch (NumberFormatException e) {
            eDate = null;
        }

        if (sDate != null && eDate != null) {
            alarmHistoryModels = alarmHistoryService.getByDevice(deviceId, sDate, eDate, start, recordSize);
        } else if (sDate != null) {
            alarmHistoryModels = alarmHistoryService.getByDevice(deviceId, sDate, null, start, recordSize);

        } else if (eDate != null) {
            alarmHistoryModels = alarmHistoryService.getByDevice(deviceId, null, eDate, start, recordSize);
        } else {
            alarmHistoryModels = alarmHistoryService.getByDevice(deviceId, null, null, start, recordSize);
        }
        List<AlarmHistoryView> alarmHistoryViews = new ArrayList<>();
        for (AlarmHistoryModel alarmHistoryModel : (List<AlarmHistoryModel>) alarmHistoryModels.getList()) {
            AlarmHistoryView alarmHistoryView = fromModel(alarmHistoryModel);
            alarmHistoryViews.add(alarmHistoryView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), alarmHistoryModels.getRecords(), alarmHistoryViews);
    }

    public AlarmHistoryView fromModel(AlarmHistoryModel alarmHistoryModel) {

        AlarmHistoryView alarmHistoryView = new AlarmHistoryView();

        alarmHistoryView.setId(alarmHistoryModel.getId());

        DeviceView deviceView = new DeviceView();
        deviceView.setId(alarmHistoryModel.getDeviceModel().getId());
        deviceView.setName(alarmHistoryModel.getDeviceModel().getName());

        DeviceParameterView deviceParameterView = new DeviceParameterView();
        deviceParameterView.setAddress(alarmHistoryModel.getDeviceParameterModel().getAddress());
        deviceParameterView.setId(alarmHistoryModel.getDeviceParameterModel().getId());
        deviceParameterView.setRegisterName(alarmHistoryModel.getDeviceParameterModel().getRegisterName());

        alarmHistoryView.setDeviceView(deviceView);
        alarmHistoryView.setDeviceParameterView(deviceParameterView);
        alarmHistoryView.setCreatedate(alarmHistoryModel.getCreatedate());
        alarmHistoryView.setResolvedate(alarmHistoryModel.getResolvedate());

        return alarmHistoryView;
    }
//    @Override
//    public Response doExport(GraphDataView graphDataView) throws EndlosiotAPIException, IOException {
//
//        DeviceModel deviceModel = deviceService.get(Long.valueOf(graphDataView.getDeviceView().getId()));
//        if (deviceModel == null) {
//            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
//        }
//
//        DeviceView deviceView =  devicefromModel(deviceModel,graphDataView.getDeviceView().getDeviceParameterViewList());
//
//        DeviceDiagnosisDataView deviceDiagnosisDataViews = new DeviceDiagnosisDataView();
//        deviceDiagnosisDataViews.setDeviceView(deviceView);
//
//        String newFileName =  "Diagnosis_"+DateUtility.getCurrentEpoch() + ".xlsx";
//
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
//        dateFormat.setTimeZone(TimeZone.getDefault());
//        timeFormat.setTimeZone(TimeZone.getDefault());
//        String filepath = null;
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        try {
//            String sheetname = "Diagnosis";
//            Sheet sheet = workbook.createSheet(sheetname);
//
//            int sr=1;
//            Row rowhead = sheet.createRow((short) 0);
//            rowhead.createCell((short) 0).setCellValue("S. No.");
//            rowhead.createCell((short) sr++).setCellValue("Date");
//            rowhead.createCell((short) sr++).setCellValue("Time");
//
//            for (DeviceParameterView deviceParameterView : deviceView.getDeviceParameterViewList()) {
//                rowhead.createCell((short) sr++).setCellValue(deviceParameterView.getRegisterName());
//            }
//
//            PageModel alarmHistoryModels = alarmHistoryService.getDignosisByDevice(graphDataView);
//
//            if (alarmHistoryModels == null || (alarmHistoryModels.getList() != null && alarmHistoryModels.getList().isEmpty())) {
//                return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
//            }
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            int i = 0;
//            for (DeviceDiagnosisModel deviceDiagnosisModel : (List<DeviceDiagnosisModel>) alarmHistoryModels.getList()) {
//
//                Map<String, String> contentMap = objectMapper.readValue(deviceDiagnosisModel.getJsoncontent(), Map.class);
//
//                i++; int j=3;
//
//                Row row = sheet.createRow((short) i);
//                row.createCell((short) 0).setCellValue((Integer) i);
//                row.createCell(1).setCellValue(deviceDiagnosisModel.getReceivedate() != null ? dateFormat.format(deviceDiagnosisModel.getReceivedate() * 1000L) : "");
//                row.createCell(2).setCellValue(deviceDiagnosisModel.getReceivedate() != null ? timeFormat.format(deviceDiagnosisModel.getReceivedate() * 1000L) : "");
//                for (DeviceParameterView deviceParameterView : deviceView.getDeviceParameterViewList()) {
//
//                    String addressToCheck = deviceParameterView.getAddress();
//                    String value = "";
//                    if (contentMap.containsKey(addressToCheck)) {
//                         value = contentMap.get(addressToCheck);
//                    }
//                    if (value != "") {
//                        row.createCell((short) j).setCellValue(value);
//                    } else {
//                        row.createCell((short) j).setCellValue("");
//                    }
//                    j++;
//                }
//            }
//            String path = SystemSettingModel.getDefaultFilePath() + File.separator + "Excel" + File.separator;
//            //String path = File.separator + "Excel" + File.separator;
//
//            File file = new File(path);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//            filepath = file.getPath() + File.separator + newFileName;
//            FileOutputStream fileOut = new FileOutputStream(filepath);
//            workbook.write(fileOut);
//            fileOut.close();
//
//            FileUtility.downloadExcel(newFileName, workbook);
//        } catch (JsonMappingException e) {
//            throw new RuntimeException(e);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return null;
//    }

//    private DeviceView devicefromModel(DeviceModel deviceModel, List<DeviceParameterView> deviceParameterViewList) {
//        DeviceView deviceView = DeviceView.builder().name(deviceModel.getName()).build();
//
//        deviceView.setId(deviceModel.getId());
//        deviceView.setName(deviceModel.getName());
//        if (deviceModel.getImei() != null) {
//            deviceView.setImei(deviceModel.getImei());
//        }
//        List<DeviceParameterView> deviceParameterViews = new ArrayList<>();
//        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceParameter(deviceParameterViewList);
//        deviceParameterModelList.forEach(data -> {
//            DeviceParameterView deviceParameterView = new DeviceParameterView();
//            ParameterMasterModel parameterMasterModel = parameterMasterService.get(data.getParameterMasterModel().getId());
//            ParameterMasterView parameterMasterView = new ParameterMasterView();
//            parameterMasterView.setName(parameterMasterModel.getName());
//            parameterMasterView.setId(parameterMasterModel.getId());
//            deviceParameterView.setId(data.getId());
//            deviceParameterView.setParameterMasterView(parameterMasterView);
//            deviceParameterView.setRegisterName(data.getRegisterName());
//            deviceParameterView.setParameterName(data.getParameterMasterModel().getName());
//            deviceParameterView.setAddress(data.getAddress());
//            deviceParameterViews.add(deviceParameterView);
//        });
//        deviceView.setDeviceParameterViewList(deviceParameterViews);
//        return deviceView;
//    }
}
