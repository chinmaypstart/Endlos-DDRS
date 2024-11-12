package com.endlosiot.common.user.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.model.UserSessionModel;
import com.endlosiot.common.user.service.UserSessionService;
import com.endlosiot.common.user.view.UserSessionView;
import com.endlosiot.common.user.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Component(value = "userSessionOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserSessionOperationImpl extends AbstractOperation<UserSessionModel, UserSessionView>
        implements UserSessionOperation {

    @Autowired
    private UserSessionService userSessionService;

    @Override
    public Response doSave(UserSessionView view) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        UserSessionModel userSessionModel = userSessionService.get(id);
        if (userSessionModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        UserSessionView userSessionView = fromModel(userSessionModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                userSessionView);
    }

    @Override
    public Response doUpdate(UserSessionView view) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        return null;
    }

    @Override
    public Response doSearch(UserSessionView userSessionView, Integer start, Integer recordSize) {
        PageModel pageModel = userSessionService.searchLight(userSessionView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                pageModel.getRecords(), fromModelList((List<UserSessionModel>) pageModel.getList()));
    }

    @Override
    public UserSessionModel toModel(UserSessionModel model, UserSessionView view) throws EndlosiotAPIException {
        return null;
    }

    @Override
    protected UserSessionModel getNewModel() {
        return new UserSessionModel();
    }

    @Override
    public UserSessionView fromModel(UserSessionModel userSessionModel) {
        return new UserSessionView.UserSessionViewBuilder().setId(userSessionModel.getId())
                .setBrowser(userSessionModel.getBrowser()).setIp(userSessionModel.getIp())
                .setOs(userSessionModel.getOs()).setLoginDate(userSessionModel.getLoginDate())
                .setUserView(UserView.setView(userSessionModel.getUserModel())).build();
    }

    @Override
    public BaseService getService() {
        return userSessionService;
    }
}