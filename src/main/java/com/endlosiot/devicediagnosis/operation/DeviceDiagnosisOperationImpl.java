package com.endlosiot.devicediagnosis.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.operation.FileOperation;
import com.endlosiot.common.file.service.FileService;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.user.service.UserService;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.common.util.FileUtility;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.operation.DeviceOperationImpl;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.device.service.DeviceService;
import com.endlosiot.device.view.DeviceParameterView;
import com.endlosiot.device.view.DeviceView;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;
import com.endlosiot.devicediagnosis.service.DeviceDiagnosisService;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisDataView;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisView;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.endlosiot.parametermaster.service.ParameterMasterService;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(value = "deviceDiagnosisOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceDiagnosisOperationImpl extends AbstractOperation<DeviceDiagnosisModel, DeviceDiagnosisView> implements DeviceDiagnosisOperation {
    @Autowired
    private DeviceDiagnosisService deviceDiagnosisService;
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
    @Autowired
    FileService fileService;
    @Autowired
    private FileOperation fileOperation;


    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }

    @Override
    public Response doSave(DeviceDiagnosisView deviceDiagnosisView) throws EndlosiotAPIException {
       /* DeviceDiagnosisModel deviceDiagnosisModel = toModel(getNewModel(), deviceDiagnosisView);
        deviceDiagnosisService.create(deviceDiagnosisModel);
        setDeviceParameterModelList(deviceDiagnosisModel, deviceDiagnosisView);

        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(deviceModel));*/

        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }

    private void setDeviceParameterModelList(DeviceDiagnosisModel deviceDiagnosisModel, DeviceDiagnosisView deviceDiagnosisView) throws EndlosiotAPIException {
        /*List<DeviceParameterModel> existDeviceParameterModels = new ArrayList<>();
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
            DeviceParameterModel deviceParameter = deviceParameterModelList.stream().filter(deviceParameterModel -> deviceParameterModel.getParameterMasterModel().equals(mappingModel) && deviceParameterView.getRegisterName().equals(deviceParameterModel.getRegisterName()) && deviceParameterView.getUnit().equals(deviceParameterModel.getUnit()) && deviceParameterView.getAddress().equals(deviceParameterModel.getAddress())).findAny().orElse(null);
            if (deviceParameter == null) {
                DeviceParameterModel deviceParameterModel = getParameterModel(deviceModel, deviceParameterView, mappingModel);
                deviceParameterService.create(deviceParameterModel);
            } else {
                existDeviceParameterModels.add(deviceParameter);
            }
        }
        for (DeviceParameterModel deviceParameterModel : deviceParameterModelList) {
            if (!existDeviceParameterModels.contains(deviceParameterModel)) {
                toDeleteDeviceParameterModels.add(deviceParameterModel);
            }
        }
        if (!toDeleteDeviceParameterModels.isEmpty()) {
            deviceParameterService.hardDelete(toDeleteDeviceParameterModels);
        }*/
    }

    /*private static DeviceParameterModel getParameterModel(DeviceModel deviceModel, DeviceParameterView deviceParameterView, ParameterMasterModel mappingModel) {
        DeviceParameterModel deviceParameterModel = new DeviceParameterModel();
        deviceParameterModel.setDeviceModel(deviceModel);
        deviceParameterModel.setParameterMasterModel(mappingModel);
        deviceParameterModel.setRegisterName(deviceParameterView.getRegisterName());
        deviceParameterModel.setUnit(deviceParameterView.getUnit());
        deviceParameterModel.setAddress(deviceParameterView.getAddress());
        deviceParameterModel.setFunction(deviceParameterView.getFunction());
        return deviceParameterModel;
    }*/

    @Override
    public Response doUpdate(DeviceDiagnosisView deviceDiagnosisView) throws EndlosiotAPIException {
        /*DeviceModel deviceModel = deviceService.get(deviceView.getId());
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        toModel(deviceModel, deviceView);
        deviceService.update(deviceModel);
        setDeviceParameterModelList(deviceModel, deviceView);
        return ViewResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage(),
                fromModel(deviceModel));*/
        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
       /* DeviceModel deviceModel = deviceService.get(id);
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        deviceModel.setArchive(true);
        deviceService.delete(deviceModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(), ResponseCode.DELETE_SUCCESSFULLY.getMessage());*/
        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        /*DeviceModel deviceModel = deviceService.get(id);
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        Auditor.activationAudit(deviceModel, !deviceModel.isActive());
        deviceService.update(deviceModel);
        return CommonResponse.create(ResponseCode.ACTIVATION_SUCCESSFUL.getCode(), ResponseCode.ACTIVATION_SUCCESSFUL.getMessage());*/

        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }


    @Override
    public DeviceDiagnosisModel toModel(DeviceDiagnosisModel deviceDiagnosisModel, DeviceDiagnosisView deviceDiagnosisView) throws EndlosiotAPIException {
        deviceDiagnosisModel.setJsoncontent(deviceDiagnosisView.getJsoncontent());

        if (deviceDiagnosisView.getDeviceView() != null) {
            DeviceModel deviceModel = deviceService.get(deviceDiagnosisView.getDeviceView().getId());
            deviceDiagnosisModel.setDeviceModel(deviceModel);
        }
        deviceDiagnosisModel.setCreatedate(deviceDiagnosisView.getCreatedate());
        deviceDiagnosisModel.setReceivedate(deviceDiagnosisView.getReceivedate());
        return deviceDiagnosisModel;
    }

    @Override
    protected DeviceDiagnosisModel getNewModel() {
        return new DeviceDiagnosisModel();
    }


   /* private void setDeviceView(DeviceModel deviceModel, DeviceView deviceView) {
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
            deviceParameterView.setUnit(data.getUnit());
            deviceParameterView.setAddress(data.getAddress());
            deviceParameterView.setFunction(data.getFunction());
            deviceParameterViews.add(deviceParameterView);
        });
        deviceView.setDeviceParameterViewList(deviceParameterViews);
    }*/

    @Override
    public BaseService getService() {
        return deviceService;
    }

    @Override
    public Response gatDeviceDiagnosis(String deviceId, String startDate, String endDate, Integer start, Integer recordSize) throws EndlosiotAPIException {

        DeviceModel deviceModel = deviceService.get(Long.valueOf(deviceId));
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        DeviceView deviceView =  deviceOperation.fromModel(deviceModel);

        PageModel deviceDataModels = null;
        Long  sDate = null, eDate = null;

        try {
            if(!startDate.equals("undefined") || !startDate.equals("NaN")) {
                sDate = Long.parseLong(startDate);
            }

        } catch (NumberFormatException e) {
            sDate = null;
        }

        try {
            if(!endDate.equals("undefined") || !endDate.equals("NaN")) {
                eDate = Long.parseLong(endDate);
            }

        } catch (NumberFormatException e) {
            eDate = null;
        }

        if (sDate != null && eDate != null) {
            deviceDataModels = deviceDiagnosisService.getByDevice(deviceId, sDate, eDate, start, recordSize);

        } else if (sDate != null) {
            deviceDataModels = deviceDiagnosisService.getByDevice(deviceId, sDate, null, start, recordSize);

        } else if (eDate != null) {
            deviceDataModels = deviceDiagnosisService.getByDevice(deviceId, null, eDate, start, recordSize);
        }
        else {
            deviceDataModels = deviceDiagnosisService.getByDevice(deviceId, null, null, start, recordSize);
        }

        deviceView.setSize(deviceDataModels.getRecords());
        DeviceDiagnosisDataView deviceDiagnosisDataViews = new DeviceDiagnosisDataView();
        deviceDiagnosisDataViews.setDeviceView(deviceView);
        List<DeviceDiagnosisView> deviceDiagnosisViews = new ArrayList<>();
        for (DeviceDiagnosisModel deviceDiagnosisModel : (List<DeviceDiagnosisModel>) deviceDataModels.getList()) {
            DeviceDiagnosisView  deviceDiagnosisView = fromModel(deviceDiagnosisModel);
            deviceDiagnosisViews.add(deviceDiagnosisView);
        }
        deviceDiagnosisDataViews.setDeviceDiagnosisViewList(deviceDiagnosisViews);
        List list = new ArrayList();
        list.add(deviceDiagnosisViews);
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceDataModels.getRecords(), list);
        //return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), deviceDiagnosisDataViews);
        //return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), pageModel.getRecords(), fromModelList((List<UserModel>) pageModel.getList()));
    }

    @Override
    public DeviceDiagnosisView fromModel(DeviceDiagnosisModel deviceDiagnosisModel) {

        DeviceDiagnosisView deviceDiagnosisView = new DeviceDiagnosisView();

        deviceDiagnosisView.setId(deviceDiagnosisModel.getId());
        deviceDiagnosisView.setJsoncontent(deviceDiagnosisModel.getJsoncontent());
        deviceDiagnosisView.setCreatedate(deviceDiagnosisModel.getCreatedate());
        deviceDiagnosisView.setReceivedate(deviceDiagnosisView.getReceivedate());

        /*if (deviceDiagnosisModel.getDeviceModel() != null) {
            DeviceView deviceView = new DeviceView();
            deviceView.setId(deviceDiagnosisModel.getDeviceModel().getId());
            deviceView.setName(deviceDiagnosisModel.getDeviceModel().getName());
            deviceDiagnosisView.setDeviceView(deviceView);
        }*/

        return deviceDiagnosisView;
    }
    @Override
    public Response doExport(GraphDataView graphDataView) throws EndlosiotAPIException, IOException {

        DeviceModel deviceModel = deviceService.get(Long.valueOf(graphDataView.getDeviceView().getId()));
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }

        DeviceView deviceView =  devicefromModel(deviceModel,graphDataView.getDeviceView().getDeviceParameterViewList());

        DeviceDiagnosisDataView deviceDiagnosisDataViews = new DeviceDiagnosisDataView();
        deviceDiagnosisDataViews.setDeviceView(deviceView);

        String newFileName =  "Diagnosis_"+DateUtility.getCurrentEpoch() + ".xlsx";

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());
        String filepath = null;
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            String sheetname = "Diagnosis";
            Sheet sheet = workbook.createSheet(sheetname);

            int sr=1;
            Row rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("S. No.");
            rowhead.createCell((short) sr++).setCellValue("Date");
            rowhead.createCell((short) sr++).setCellValue("Time");

            for (DeviceParameterView deviceParameterView : deviceView.getDeviceParameterViewList()) {
                rowhead.createCell((short) sr++).setCellValue(deviceParameterView.getRegisterName());
            }

            PageModel deviceDataModels = deviceDiagnosisService.getDignosisByDevice(graphDataView);

            if (deviceDataModels == null || (deviceDataModels.getList() != null && deviceDataModels.getList().isEmpty())) {
                return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            int i = 0;
            for (DeviceDiagnosisModel deviceDiagnosisModel : (List<DeviceDiagnosisModel>) deviceDataModels.getList()) {

                Map<String, String> contentMap = objectMapper.readValue(deviceDiagnosisModel.getJsoncontent(), Map.class);

                i++; int j=3;

                Row row = sheet.createRow((short) i);
                row.createCell((short) 0).setCellValue((Integer) i);
                row.createCell(1).setCellValue(deviceDiagnosisModel.getReceivedate() != null ? dateFormat.format(deviceDiagnosisModel.getReceivedate() * 1000L) : "");
                row.createCell(2).setCellValue(deviceDiagnosisModel.getReceivedate() != null ? timeFormat.format(deviceDiagnosisModel.getReceivedate() * 1000L) : "");
                for (DeviceParameterView deviceParameterView : deviceView.getDeviceParameterViewList()) {

                    String addressToCheck = deviceParameterView.getAddress();
                    String value = "";
                    if (contentMap.containsKey(addressToCheck)) {
                         value = contentMap.get(addressToCheck);
                    }
                    if (value != "") {
                        row.createCell((short) j).setCellValue(value);
                    } else {
                        row.createCell((short) j).setCellValue("");
                    }
                    j++;
                }
            }
            String path = SystemSettingModel.getDefaultFilePath() + File.separator + "Excel" + File.separator;
            //String path = File.separator + "Excel" + File.separator;

            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
            filepath = file.getPath() + File.separator + newFileName;
            FileOutputStream fileOut = new FileOutputStream(filepath);
            workbook.write(fileOut);
            fileOut.close();

            FileUtility.downloadExcel(newFileName, workbook);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private DeviceView devicefromModel(DeviceModel deviceModel, List<DeviceParameterView> deviceParameterViewList) {
        DeviceView deviceView = DeviceView.builder().name(deviceModel.getName()).build();

        deviceView.setId(deviceModel.getId());
        deviceView.setName(deviceModel.getName());
        if (deviceModel.getImei() != null) {
            deviceView.setImei(deviceModel.getImei());
        }
        List<DeviceParameterView> deviceParameterViews = new ArrayList<>();
        List<DeviceParameterModel> deviceParameterModelList = deviceParameterService.getByDeviceParameter(deviceParameterViewList);
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
        return deviceView;
    }
}
