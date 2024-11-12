package com.endlosiot.screen.operation;

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
import com.endlosiot.common.user.model.UserScreenModel;
import com.endlosiot.common.user.service.UserScreenService;
import com.endlosiot.common.user.service.UserService;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.device.service.DeviceService;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;
import com.endlosiot.devicediagnosis.service.DeviceDiagnosisService;
import com.endlosiot.screen.model.CellModel;
import com.endlosiot.screen.model.ColumnModel;
import com.endlosiot.screen.model.RowModel;
import com.endlosiot.screen.model.ScreenModel;
import com.endlosiot.screen.service.CellMasterService;
import com.endlosiot.screen.service.ColumnMasterService;
import com.endlosiot.screen.service.RowMasterService;
import com.endlosiot.screen.service.ScreenService;
import com.endlosiot.screen.view.ScreenDataRequestView;
import com.endlosiot.screen.view.ScreenView;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component(value = "screenOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScreenOperationImpl extends AbstractOperation<ScreenModel, ScreenView> implements ScreenOperation {
    @Autowired
    private ScreenService screenService;
    @Autowired
    private UserService userService;
    @Autowired
    private ColumnMasterOperation columnMasterOperation;
    @Autowired
    RowMasterService rowMasterService;
    @Autowired
    CellMasterService cellMasterService;
    @Autowired
    ColumnMasterService columnMasterService;
    @Autowired
    private DeviceParameterService deviceParameterService;
    @Autowired
    private DeviceDiagnosisService deviceDiagnosisService;
    @Autowired
    private DeviceService deviceService;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserScreenService userScreenService;

    public ScreenOperationImpl() {
    }


    @Override
    public Response doView(Long id) throws EndlosiotAPIException {

        List<CellModel> cellList = cellMasterService.getByScreen(id);

        ScreenDataRequestView screenDataRequestView = fromModelLarge(cellList);

        //return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenView);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenDataRequestView);
    }

    public ScreenDataRequestView fromModelLarge(List<CellModel> cellModels) {
        ScreenDataRequestView screenDataRequestView = new ScreenDataRequestView();
        if (!cellModels.isEmpty()) {
            ScreenModel screenModel = cellModels.get(0).getScreenModel();
            screenDataRequestView.setId(screenModel.getId());
            screenDataRequestView.setScreenName(screenModel.getScreenName());
            screenDataRequestView.setTitleText(screenModel.getTitleText());
            screenDataRequestView.setScreenDesc(screenModel.getScreenDesc());
            screenDataRequestView.setRowNumber(screenModel.getRowNumber());
            screenDataRequestView.setColNumber(screenModel.getColNumber());
        }
        // Group by row name
        Map<String, List<CellModel>> rowsMap = cellModels.stream().collect(Collectors.groupingBy(cell -> cell.getRowModel().getRowName()));

        Map<String, List<CellModel>> sortedRowsMap = rowsMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparing(list -> list.get(0).getRowModel().getId()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        List<ScreenDataRequestView.Row> rows = new ArrayList<>();
        for (Map.Entry<String, List<CellModel>> rowEntry : sortedRowsMap.entrySet()) {
            ScreenDataRequestView.Row row = new ScreenDataRequestView.Row();
            row.setRow(rowEntry.getKey());
            // Group by column name within each row
            Map<String, List<CellModel>> columnsMap = rowEntry.getValue().stream().collect(Collectors.groupingBy(cell -> cell.getColumnModel().getColumnName()));

            Map<String, List<CellModel>> sortedColumnMap = columnsMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.comparing(list -> list.get(0).getColumnModel().getId()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            List<ScreenDataRequestView.Column> columns = new ArrayList<>();
            for (Map.Entry<String, List<CellModel>> columnEntry : sortedColumnMap.entrySet()) {
                ScreenDataRequestView.Column column = new ScreenDataRequestView.Column();
                CellModel cell = columnEntry.getValue().get(0);
                column.setColumn(cell.getColumnModel().getColumnName());
                column.setAddress(cell.getDeviceParameterModel().getAddress());
                column.setDecimal(cell.getDecimal().toString());
                column.setMin(cell.getMin());
                column.setMax(cell.getMax());
                if (cell.getFunction() != null) {
                    column.setFunction(cell.getFunction());
                    if (cell.getFunction().equals("Read/write")) {
                        column.setReadOnly(true);
                    }
                }
                if (cell.getUnit() != null) {
                    column.setUnit(cell.getUnit());
                }
                if (cell.getZeroButtonText() != null) {
                    column.setZeroButtonText(cell.getZeroButtonText());
                }
                if (cell.getOneButtonText() != null) {
                    column.setOneButtonText(cell.getOneButtonText());
                }
                if(cell.getShowValueMessage()!=null){
                    column.setShowValueMessage(cell.getShowValueMessage());
                }
                DeviceDiagnosisModel deviceDiagnosisModel = deviceDiagnosisService.getLatestDiagnosisDataByDeviceId(String.valueOf(cell.getDeviceParameterModel().getDeviceModel().getId()));
                if (deviceDiagnosisModel == null) {
                    column.setValue("NA");
                    column.setReadOnly(true);
                } else {
                    String jsonContent = deviceDiagnosisModel.getJsoncontent();
                    //System.out.println(jsonContent);
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        Map<String, String> contentMap = objectMapper.readValue(jsonContent, Map.class);
                        //System.out.println("Register Name...." + cell.getDeviceParameterModel().getRegisterName() + "    Address...."+ cell.getDeviceParameterModel().getAddress() +"   Device Id is..." + cell.getDeviceParameterModel().getDeviceModel().getId());
                        // Addresses to check
                        String addressToCheck = cell.getDeviceParameterModel().getAddress();
                        if (contentMap.containsKey(addressToCheck)) {
                            String value = contentMap.get(addressToCheck);
                            //String allValue = value+" ("+cell.getDeviceParameterModel().getRegisterName()+" : "+cell.getDeviceParameterModel().getAddress()+")";
                            //String allValue = value+" ("+cell.getDeviceParameterModel().getRegisterName()+")";
                            String allValue = value;
                            BigDecimal bdValue = new BigDecimal(allValue);
                            bdValue = bdValue.setScale(Math.toIntExact(cell.getDecimal()), RoundingMode.HALF_UP);
                            column.setValue(bdValue.toString());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                column.setId(cell.getDeviceParameterModel().getId());
                columns.add(column);
            }
            row.setColumns(columns);
            rows.add(row);
        }
        screenDataRequestView.setRows(rows);
        return screenDataRequestView;
    }

    @Override
    public Response doSave(ScreenView screenView) throws EndlosiotAPIException {
        ScreenModel screenModel = toModel(getNewModel(), screenView);

        //screenModel.setActive(true);
        screenService.create(screenModel);

        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(screenModel));
    }

    @Override
    public Response doUpdate(ScreenView screenView) throws EndlosiotAPIException {

        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        ScreenModel screenModel = screenService.get(id);
        if (screenModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //screenModel.setArchive(true);
        //screenService.delete(screenModel);
        screenService.hardDelete(screenModel.getId());
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(), ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        ScreenModel screenModel = screenService.get(id);
        if (screenModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //Auditor.activationAudit(screenModel, !screenModel.isActive());
        screenService.update(screenModel);
        return CommonResponse.create(ResponseCode.ACTIVATION_SUCCESSFUL.getCode(), ResponseCode.ACTIVATION_SUCCESSFUL.getMessage());
    }

    @Override
    public ScreenModel toModel(ScreenModel screenModel, ScreenView screenView) throws EndlosiotAPIException {

        screenModel.setScreenName(screenView.getScreenName());
        screenModel.setTitleText(screenView.getTitleText());
        if (screenView.getScreenDesc() != null) {
            screenModel.setScreenDesc(screenView.getScreenDesc());
        }
        if (screenView.getRowNumber() != null) {
            screenModel.setRowNumber(screenView.getRowNumber());
        }
        if (screenView.getColNumber() != null) {
            screenModel.setColNumber(screenView.getColNumber());
        }
        return screenModel;
    }

    @Override
    protected ScreenModel getNewModel() {
        return new ScreenModel();
    }

    @Override
    public ScreenView fromModel(ScreenModel screenModel) {
        ScreenView screenView = ScreenView.builder().screenName(screenModel.getScreenName()).build();
        screenView.setId(screenModel.getId());
        screenView.setScreenName(screenModel.getScreenName());
        screenView.setTitleText(screenModel.getTitleText());
        if (screenModel.getScreenDesc() != null) {
            screenView.setScreenDesc(screenModel.getScreenDesc());
        }
        if (screenModel.getRowNumber() != null) {
            screenView.setRowNumber(screenModel.getRowNumber());
        }
        if (screenModel.getColNumber() != null) {
            screenView.setColNumber(screenModel.getColNumber());
        }
        return screenView;
    }

    @Override
    public BaseService getService() {
        return screenService;
    }

    @Override
    public Response doSearch(ScreenView screenView, Integer start, Integer recordSize) {
        if (Auditor.getAuditor().getName().equals("Master Admin")) {
            PageModel pageModel = screenService.search(screenView, start, recordSize);
            if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
                return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
            }
            return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), pageModel.getRecords(), fromModelList((List<ScreenModel>) pageModel.getList()));

        } else {
            //PageModel pageModel = screenService.search(screenView, start, recordSize);
            List<UserScreenModel> l = userScreenService.getAllScreenListByUserId(Auditor.getAuditor());
            if ((l != null && l.isEmpty())) {
                return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
            }

            List<ScreenView> screenViews = new ArrayList<>();
            for (UserScreenModel userScreenModel : l) {
                ScreenModel screenModel = screenService.get(userScreenModel.getScreenModel().getId());
                ScreenView screenView1 = fromModel(screenModel);
                screenViews.add(screenView1);
            }

            return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenViews.size(), screenViews);
        }
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

    @Override
    public List<ScreenView> fromModelList(List<ScreenModel> models) {
        return super.fromModelList(models);
    }

    public Response doSaveScreeData(ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException {

        boolean screenSave, columnSave, rowSave;
        ScreenModel screenMaster = new ScreenModel();
        screenMaster.setScreenName(screenDataRequest.getScreenName());
        screenMaster.setTitleText(screenDataRequest.getTitleText());
        screenMaster.setRowNumber(screenDataRequest.getRowNumber());
        screenMaster.setColNumber(screenDataRequest.getColNumber());
        screenMaster.setScreenDesc(screenDataRequest.getScreenDesc());
        //screenMaster.setActive(true);
        screenService.create(screenMaster);
        screenSave = true;

        if (screenSave) {

            for (String columnName : screenDataRequest.getColumns()) {
                ColumnModel columnModel = new ColumnModel();
                columnModel.setColumnName(columnName);
                columnModel.setScreenModel(screenMaster);
                columnMasterOperation.doSave(columnModel);
            }
            columnSave = true;

            if (columnSave) {

                for (ScreenDataRequestView.Row row : screenDataRequest.getRows()) {
                    RowModel rowMaster = new RowModel();
                    rowMaster.setRowName(row.getRow());
                    rowMaster.setScreenModel(screenMaster);
                    rowMasterService.create(rowMaster);
                    rowSave = true;

                    if (rowSave) {
                        for (ScreenDataRequestView.Column column : row.getColumns()) {
                            CellModel cellMaster = new CellModel();
                            DeviceParameterModel deviceParameterModel = deviceParameterService.get(Long.parseLong(column.getValue()));
                            cellMaster.setDeviceParameterModel(deviceParameterModel);
                            cellMaster.setCellValue(column.getValue());
                            cellMaster.setRowModel(rowMaster);
                            ColumnModel columnMaster = columnMasterService.getByScreenIdAndColumnName(screenMaster.getId(), column.getColumn());
                            cellMaster.setColumnModel(columnMaster);
                            cellMaster.setScreenModel(screenMaster);
                            cellMaster.setDecimal(Long.valueOf(column.getDecimal()));
                            cellMaster.setMin(column.getMin());
                            cellMaster.setMax(column.getMax());
                            if (column.getUnit() != null) {
                                cellMaster.setUnit(column.getUnit());
                            }
                            if (column.getFunction() != null) {
                                cellMaster.setFunction(column.getFunction());
                            }
                            if (column.getZeroButtonText() != null) {
                                cellMaster.setZeroButtonText(column.getZeroButtonText());
                            }
                            if (column.getOneButtonText() != null) {
                                cellMaster.setOneButtonText(column.getOneButtonText());
                            }
                            if(column.getShowValueMessage()!=null){
                                cellMaster.setShowValueMessage(column.getShowValueMessage());
                            }
                            cellMasterService.create(cellMaster);
                        }
                    }

                }
            } else {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
            }
        } else {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }

        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage(), fromModel(screenMaster));
    }

    @Override
    public Response getDropDownScreen() {

        if (Auditor.getAuditor().getId() == 1) {

            List<ScreenView> screenViewList = new ArrayList<>();
            List<ScreenModel> screenModelList = screenService.findAll();
            for (ScreenModel screenModel : screenModelList) {
                ScreenView screenView = ScreenView.builder().screenName(screenModel.getScreenName()).build();
                screenView.setId(screenModel.getId());
                screenViewList.add(screenView);
            }
            return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenViewList.size(), screenViewList);

        } else {

            List<UserScreenModel> l = userScreenService.getAllScreenListByUserId(Auditor.getAuditor());
            if ((l != null && l.isEmpty())) {

                List<ScreenView> screenViewList = new ArrayList<>();
                List<ScreenModel> screenModelList = screenService.findAll();
                for (ScreenModel screenModel : screenModelList) {
                    ScreenView screenView = ScreenView.builder().screenName(screenModel.getScreenName()).build();
                    screenView.setId(screenModel.getId());
                    screenViewList.add(screenView);
                }
                return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenViewList.size(), screenViewList);

                //return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
            }

            List<ScreenView> screenViews = new ArrayList<>();
            for (UserScreenModel userScreenModel : l) {
                ScreenModel screenModel = screenService.get(userScreenModel.getScreenModel().getId());
                ScreenView screenView = ScreenView.builder().screenName(screenModel.getScreenName()).build();
                screenView.setId(screenModel.getId());
                screenViews.add(screenView);
            }
            return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenViews.size(), screenViews);
        }
    }

    @Override
    public Response getUserDropDownScreen() throws EndlosiotAPIException {
        List<ScreenView> screenViewList = new ArrayList<>();
        List<ScreenModel> screenModelList = screenService.findAll();
        for (ScreenModel screenModel : screenModelList) {
            ScreenView screenView = ScreenView.builder().screenName(screenModel.getScreenName()).build();
            screenView.setId(screenModel.getId());
            screenViewList.add(screenView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), screenViewList.size(), screenViewList);
    }

    @Override
    public Response doSendData(Long id, String value) throws EndlosiotAPIException {
        DeviceParameterModel deviceParameterModel = deviceParameterService.get(id);
        if (deviceParameterModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        DeviceModel deviceModel = deviceService.get(deviceParameterModel.getDeviceModel().getId());
        if (deviceModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        JSONObject json = new JSONObject();
        json.put("did", deviceModel.getImei());

        JSONArray contentArray = new JSONArray();
        JSONObject contentObject = new JSONObject();
        contentObject.put("pid", "1");
        contentObject.put("addr", deviceParameterModel.getAddress());
        contentObject.put("addrv", value);

        contentArray.put(contentObject);
        json.put("content", contentArray);

        System.out.println(json.toString());

        String clientId = UUID.randomUUID().toString();
        MqttClientPersistence mqttClientPersistence = new MemoryPersistence();
        boolean cleanSession = false;
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(cleanSession);
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(10);
        try {
            String broker = "tcp://broker.hivemq.com:1883";

            MqttClient client = new MqttClient(broker, clientId, mqttClientPersistence);
            client.connect(options); // connecting to broker
            // Printing the JSON object
            byte[] ackBytes = json.toString().getBytes();
            MqttMessage ackMessage = new MqttMessage(ackBytes);
            int qos = 0;
            ackMessage.setQos(qos);
            ackMessage.setRetained(false);
            MqttTopic topic2 = client.getTopic("hemiltesttopic2");
            topic2.publish(ackMessage);
            if (client.isConnected()) {
                client.close();
            }
        } catch (MqttException me) {
            LoggerService.error("reason " + me.getReasonCode());
            LoggerService.error("msg " + me.getMessage());
            LoggerService.error("loc " + me.getLocalizedMessage());
            LoggerService.error("cause " + me.getCause());
            LoggerService.error("excep " + me);
        }
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }

    public Response doUpdateScreeData(ScreenDataRequestView screenDataRequest) throws EndlosiotAPIException {

        ScreenModel screenMaster = screenService.get(screenDataRequest.getId());
        if (screenMaster == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }

        boolean screenSave, columnSave, rowSave;
        screenMaster.setId(screenDataRequest.getId());
        screenMaster.setScreenName(screenDataRequest.getScreenName());
        screenMaster.setTitleText(screenDataRequest.getTitleText());
        screenMaster.setRowNumber(screenDataRequest.getRowNumber());
        screenMaster.setColNumber(screenDataRequest.getColNumber());
        screenMaster.setScreenDesc(screenDataRequest.getScreenDesc());
        screenService.update(screenMaster);
        screenSave = true;

        screenService.hardDeleteRowColumnCell(screenDataRequest.getId());

        if (screenSave) {

            for (String columnName : screenDataRequest.getColumns()) {
                ColumnModel columnModel = new ColumnModel();
                columnModel.setColumnName(columnName);
                columnModel.setScreenModel(screenMaster);
                columnMasterOperation.doSave(columnModel);
            }
            columnSave = true;

            if (columnSave) {

                for (ScreenDataRequestView.Row row : screenDataRequest.getRows()) {
                    RowModel rowMaster = new RowModel();
                    rowMaster.setRowName(row.getRow());
                    rowMaster.setScreenModel(screenMaster);
                    rowMasterService.create(rowMaster);
                    rowSave = true;

                    if (rowSave) {
                        for (ScreenDataRequestView.Column column : row.getColumns()) {
                            CellModel cellMaster = new CellModel();
                            DeviceParameterModel deviceParameterModel = deviceParameterService.get(Long.parseLong(column.getValue()));
                            cellMaster.setDeviceParameterModel(deviceParameterModel);
                            cellMaster.setCellValue(column.getValue());
                            cellMaster.setRowModel(rowMaster);
                            ColumnModel columnMaster = columnMasterService.getByScreenIdAndColumnName(screenMaster.getId(), column.getColumn());
                            cellMaster.setColumnModel(columnMaster);
                            cellMaster.setScreenModel(screenMaster);
                            cellMaster.setDecimal(Long.valueOf(column.getDecimal()));
                            cellMaster.setMin(column.getMin());
                            cellMaster.setMax(column.getMax());
                            if (column.getUnit() != null) {
                                cellMaster.setUnit(column.getUnit());
                            }
                            if (column.getFunction() != null) {
                                cellMaster.setFunction(column.getFunction());
                            }
                            if (column.getZeroButtonText() != null) {
                                cellMaster.setZeroButtonText(column.getZeroButtonText());
                            }
                            if (column.getOneButtonText() != null) {
                                cellMaster.setOneButtonText(column.getOneButtonText());
                            }
                            if(column.getShowValueMessage()!=null){
                                cellMaster.setShowValueMessage(column.getShowValueMessage());
                            }
                            cellMasterService.create(cellMaster);
                        }
                    }

                }
            } else {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
            }
        } else {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        return ViewResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(), ResponseCode.UPDATE_SUCCESSFULLY.getMessage(), fromModel(screenMaster));
    }
}
