package com.endlosiot.parametermaster.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.endlosiot.parametermaster.service.ParameterMasterService;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * For Parameter Mapping Operation
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
@Component(value = "parameterMasterOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ParameterMasterOperationImpl extends AbstractOperation<ParameterMasterModel, ParameterMasterView>
        implements ParameterMasterOperation {

    @Autowired
    ParameterMasterService parameterMasterService;

    @Override
    public BaseService<ParameterMasterModel> getService() {
        return parameterMasterService;
    }

    @Override
    public ParameterMasterModel toModel(ParameterMasterModel parameterMasterModel, ParameterMasterView parameterMasterView) {
        parameterMasterModel.setName(parameterMasterView.getName());
        parameterMasterModel.setJsonCode(parameterMasterView.getJsonCode());
        parameterMasterModel.setParameterUnit(parameterMasterView.getParamUnit());
        return  parameterMasterModel;
    }
    @Override
    public ParameterMasterView fromModel(ParameterMasterModel parameterMasterModel) {
        ParameterMasterView parameterMasterView = new ParameterMasterView();
        parameterMasterView.setId(parameterMasterModel.getId());
        parameterMasterView.setName(parameterMasterModel.getName());
        parameterMasterView.setParamUnit(parameterMasterModel.getParameterUnit());
        parameterMasterView.setJsonCode(parameterMasterModel.getJsonCode());
        return parameterMasterView;
    }
    @Override
    protected ParameterMasterModel getNewModel() {
        return new ParameterMasterModel();
    }

    @Override
    public Response doSave(ParameterMasterView parameterMasterView) throws EndlosiotAPIException {
        ParameterMasterModel parameterMasterModel = toModel(new ParameterMasterModel(), parameterMasterView);
        parameterMasterService.create(parameterMasterModel);
        return ViewResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage(),
                fromModel(parameterMasterModel));
    }

    public Response doUpdate(ParameterMasterView parameterMasterView) throws EndlosiotAPIException {
        ParameterMasterModel parameterMasterModel= parameterMasterService.get(parameterMasterView.getId());
        if (parameterMasterModel == null) {
            return CommonResponse.create(
                    ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        parameterMasterModel= toModel(parameterMasterModel,parameterMasterView);
        parameterMasterService.update(parameterMasterModel);
        return ViewResponse.create(
                ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage(), fromModel(parameterMasterModel));
    }
    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        ParameterMasterModel parameterMasterModel = parameterMasterService.get(id);
        if (Objects.isNull(parameterMasterModel)) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(),
                fromModel(parameterMasterModel));
    }
    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        ParameterMasterModel parameterMasterModel = parameterMasterService.get(id);
        if (Objects.isNull(parameterMasterModel)) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        //parameterMasterModel.setArchive(true);
        parameterMasterService.delete(parameterMasterModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        return null;
    }

    public Response doSearch(ParameterMasterView parameterMasterView, Integer start, Integer recordSize) {
        PageModel result = parameterMasterService.search(parameterMasterView, start, recordSize);
        if (result.getRecords() == 0) {
            return PageResultResponse.create(
                    ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(),
                    0,
                    Collections.emptyList());
        }
        return PageResultResponse.create(
                ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(),
                result.getRecords(),
                fromModelList((List<ParameterMasterModel>) result.getList()));
    }

    @Override
    public Response getDropDown() {
        List<ParameterMasterView> parameterMasterViewList = new ArrayList<>();
        List<ParameterMasterModel> parameterMasterModels = parameterMasterService.findAll();
        for (ParameterMasterModel parameterMasterModel : parameterMasterModels) {
            ParameterMasterView parameterMasterView =fromModel(parameterMasterModel);
            parameterMasterViewList.add(parameterMasterView);
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(),
                parameterMasterViewList.size(),
                parameterMasterViewList);
    }
}
