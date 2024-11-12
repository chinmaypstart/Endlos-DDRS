package com.endlosiot.devicediagnosis.batchjob;

import com.endlosiot.common.notification.service.TransactionalEmailService;
import com.endlosiot.common.notification.thread.EmailThread;
import com.endlosiot.common.util.Constant;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.service.DeviceParameterService;
import com.endlosiot.devicediagnosis.model.AlarmHistoryModel;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;
import com.endlosiot.devicediagnosis.service.AlarmHistoryService;
import com.endlosiot.devicediagnosis.service.DeviceDiagnosisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
@Component
public class AlarmHistoryJob implements Job {

    @Autowired
    TransactionalEmailService transactionalEmailService;

    @Autowired
    DeviceParameterService deviceParameterService;

    @Autowired
    DeviceDiagnosisService deviceDiagnosisService;

    @Autowired
    AlarmHistoryService alarmHistoryService;

    @Autowired
    EmailThread emailThread;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        long startTime = System.currentTimeMillis();

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        List<DeviceParameterModel> deviceParameterModelsList = deviceParameterService.getRecordForAlarm();
        if(deviceParameterModelsList!=null && !deviceParameterModelsList.isEmpty() ){
            for (DeviceParameterModel deviceParameterModel : deviceParameterModelsList) {
                List<DeviceDiagnosisModel> deviceDiagnosisList = deviceDiagnosisService.getByDeviceByStartDate(deviceParameterModel.getDeviceModel().getId());
                if(deviceDiagnosisList!=null && !deviceDiagnosisList.isEmpty()){
                    for (DeviceDiagnosisModel deviceDiagnosisModel : deviceDiagnosisList) {
                        String jsonContent = deviceDiagnosisModel.getJsoncontent();
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Map<String, String> contentMap = objectMapper.readValue(jsonContent, Map.class);
                            String addressToCheck = deviceParameterModel.getAddress();
                            if (contentMap.containsKey(addressToCheck)) {
                                String value = contentMap.get(addressToCheck);
                                String allValue = value;
                                BigDecimal bdValue = new BigDecimal(allValue);
                                bdValue = bdValue.setScale(Math.toIntExact(2), RoundingMode.HALF_UP);

                                if (bdValue.compareTo(new BigDecimal(0)) > 0) {
                                    AlarmHistoryModel alarmHistoryModel = alarmHistoryService.getAlarmByParameter(deviceParameterModel.getId());
                                    if (alarmHistoryModel == null) {
                                        alarmHistoryModel = new AlarmHistoryModel();
                                        alarmHistoryModel.setDeviceModel(deviceDiagnosisModel.getDeviceModel());
                                        alarmHistoryModel.setCreatedate(deviceDiagnosisModel.getCreatedate());
                                        alarmHistoryModel.setDeviceParameterModel(deviceParameterModel);
                                        alarmHistoryModel.setCount(1L);
                                        alarmHistoryService.create(alarmHistoryModel);
                                    }
                                } else {
                                    AlarmHistoryModel alarmHistoryModel = alarmHistoryService.getAlarmByParameter(deviceParameterModel.getId());
                                    if (alarmHistoryModel != null) {
                                        alarmHistoryModel.setResolvedate(deviceDiagnosisModel.getReceivedate());
                                        alarmHistoryService.update(alarmHistoryModel);
                                    }
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        deviceDiagnosisModel.setAlarmCalculated(true);
                        deviceDiagnosisService.update(deviceDiagnosisModel);
                    }
                }
            }
            jobDataMap.put("Records", deviceParameterModelsList.size());
        }
        jobDataMap.put(Constant.TOTAL_TIME_TAKEN_BY_JOB, System.currentTimeMillis() - startTime);
        return;
    }

}
