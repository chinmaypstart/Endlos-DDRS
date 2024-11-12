package com.endlosiot.mongocall.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.model.PageModelString;
import com.endlosiot.common.response.PageResultMapResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.mongocall.service.MongoCommunicationService;
import com.endlosiot.mongocall.view.DiagnosisParameter;
import com.endlosiot.mongocall.view.DiagnosisView;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.endlosiot.parametermaster.service.ParameterMasterService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class GatewayCommunicationOperation {

    @Value("${mongo.databasename}")
    private String databaseName;

    @Value("${mongo.collectionname}")
    private String collectionName;

    @Autowired
    ParameterMasterService parameterMasterService;

    @Autowired
    MongoCommunicationService mongoCommunicationService;

    public Response gatewayInfo(String deviceId, Integer start, Integer recordSize) throws IOException {
        PageModelString communicationViews = mongoCommunicationService.
                fetchRecordsWithPagination(databaseName, collectionName, deviceId, start, recordSize);
//        for (String responseString : communicationViews.getList()) {
//            System.out.println(responseString);
//        }
        List<DiagnosisView> diagnosisViews = getDiagnosisViewList(communicationViews);
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                communicationViews.getRecords(), diagnosisViews);
    }

    private List<DiagnosisView> getDiagnosisViewList(PageModelString communicationViews) {
        List<DiagnosisView> diagnosisViews = new ArrayList<>();
        for (String responseString : communicationViews.getList()) {
            List<ParameterMasterModel> parameterMasterModelList = parameterMasterService.findAll();
            List<DiagnosisParameter> diagnosisParameterList = new ArrayList<>();
            DiagnosisView diagnosisView = new DiagnosisView();
            for (ParameterMasterModel parameterMasterModel : parameterMasterModelList) {
                JSONObject jsonObject = new JSONObject(responseString);
                diagnosisView.setDeviceId(jsonObject.getString("key"));
                JSONObject json1 = jsonObject.getJSONObject("value");
                JSONObject json2 = json1.getJSONArray("Data").getJSONObject(0);
                for (Iterator<String> it = json2.keys(); it.hasNext(); ) {
                    String field = it.next();
                    DiagnosisParameter diagnosisParameter = new DiagnosisParameter();
                    if (parameterMasterModel.getJsonCode().equals(field)) {
                        diagnosisParameter.setName(parameterMasterModel.getName());
                        diagnosisParameter.setValue(json2.getString(parameterMasterModel.getJsonCode()));
                        diagnosisParameterList.add(diagnosisParameter);
                    }
                }
                for (Iterator<String> it = json1.keys(); it.hasNext(); ) {
                    String field = it.next();
                    DiagnosisParameter diagnosisParameter = new DiagnosisParameter();
                    if (parameterMasterModel.getJsonCode().equals(field)) {
                        diagnosisParameter.setName(parameterMasterModel.getName());
                        diagnosisParameter.setValue(json1.getString(parameterMasterModel.getJsonCode()));
                        diagnosisParameterList.add(diagnosisParameter);
                    }
                }
                if (json1.has("Time stamp")) {
                    diagnosisView.setTime(json1.getString("Time stamp"));
                } else {
                    diagnosisView.setTime(null);
                }
                diagnosisView.setParameterDetail(diagnosisParameterList);
            }
            diagnosisViews.add(diagnosisView);
        }
        return diagnosisViews;
    }

    public Response processedData(String deviceId, Integer start, Integer recordSize) throws IOException {
        PageModelString calculateData = mongoCommunicationService.
                processRecordsWithPagination(databaseName, "calculatedata", deviceId, start, recordSize);
        PageModelString calculateRange = mongoCommunicationService.
                processRecordsWithPagination(databaseName, "calculaterange", deviceId, start, recordSize);

        List<Map<String, Object>> list1 = convertJsonStringsToList(calculateData.getList());
        List<Map<String, Object>> list2 = convertJsonStringsToList(calculateRange.getList());

        List<Map<String, Object>> result = mergeLists(list1, list2);
        return PageResultMapResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), result.size(), mergeLists(list1, list2));
    }

    private static List<Map<String, Object>> convertJsonStringsToList(List<String> jsonStrings) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (String responseString : jsonStrings) {
            Map<String, Object> map = objectMapper.readValue(responseString, new TypeReference<Map<String, Object>>() {
            });
            resultList.add(map);
        }

        return resultList;
    }

    private static List<Map<String, Object>> mergeLists(List<Map<String, Object>> list1, List<Map<String, Object>> list2) {
        Map<Object, Map<String, Object>> mergedMap = new HashMap<>();

        list1.forEach(map -> mergedMap.put(map.get("dId"), excludeIdField(map)));

        // Merge list2 into the mergedMap, updating existing entries based on dId or adding new entries
        list2.forEach(map -> mergedMap.merge(map.get("dId"), excludeIdField(map), (existing, incoming) -> {
            existing.putAll(incoming);
            return existing;
        }));

        // Convert the values of the mergedMap back to a List
        return new ArrayList<>(mergedMap.values());
    }

    private static Map<String, Object> excludeIdField(Map<String, Object> map) {
        // Create a new map excluding the "_id" field
        Map<String, Object> result = new HashMap<>(map);
        result.remove("_id");
        return result;
    }

}
