package com.endlosiot.parametermaster.controller;

import com.endlosiot.common.controller.AbstractController;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.parametermaster.enums.ParameterAggregationEnum;
import com.endlosiot.parametermaster.operation.ParameterMasterOperation;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This controller maps all user related apis.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */

@CrossOrigin(value = "*")
@Controller
@RequestMapping("private/parametermapping")
public class ParameterMasterController extends AbstractController<ParameterMasterView> {

    @Autowired
    ParameterMasterOperation parameterMasterOperation;

    public BaseOperation<ParameterMasterView> getOperation() {
        return parameterMasterOperation;
    }
    @Override
    public ResponseEntity<Response> save(@RequestBody ParameterMasterView parameterMasterView) throws EndlosiotAPIException {
        checkViewNotNull(parameterMasterView);
        isValidSaveData(parameterMasterView);
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doSave(parameterMasterView));
    }

    @Override
    public ResponseEntity<Response> update(@RequestBody ParameterMasterView parameterMasterView) throws EndlosiotAPIException {
        checkViewNotNull(parameterMasterView);
        checkIdNotNull(parameterMasterView.getId());
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doUpdate(parameterMasterView));
    }

    @Override
    public ResponseEntity<Response> view(@PathVariable(name = "id") Long id)
            throws EndlosiotAPIException {
        checkIdNotNull(id);
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doView(id));
    }

    @Override
    public ResponseEntity<Response> delete(@PathVariable(name = "id") Long id)
            throws EndlosiotAPIException {
        checkIdNotNull(id);
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doDelete(id));
    }

    @PostMapping(value = "/search")
    public ResponseEntity<Response> search(@RequestBody ParameterMasterView parameterMasterView,
                                           @RequestParam(name = "start", required = true) Integer start,
                                           @RequestParam(name = "recordSize", required = true) Integer recordSize)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(getOperation().doSearch(parameterMasterView, start, recordSize));
    }

    @GetMapping(value = "/drop-down")
    public ResponseEntity<Response> dropDown()
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(parameterMasterOperation.getDropDown());
    }

    private void checkViewNotNull(ParameterMasterView parameterMasterView) throws EndlosiotAPIException {
        if (parameterMasterView == null) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(parameterMasterView);
    }

    private static void checkIdNotNull(Long id) throws EndlosiotAPIException {
        if (id == null) {
            throw new EndlosiotAPIException(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
    }

    @Override
    public void isValidSaveData(ParameterMasterView view) throws EndlosiotAPIException {
    }

    @GetMapping(value = "/dropdown-aggregation")
    public ResponseEntity<Response> dropdownDataMode() {
        List<KeyValueView> keyValueViews = new ArrayList<>();
        ParameterAggregationEnum.getMap()
                .forEach((key, value) -> keyValueViews.add(KeyValueView.create(key, value.getName())));
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), keyValueViews.size(), keyValueViews));
    }
}
