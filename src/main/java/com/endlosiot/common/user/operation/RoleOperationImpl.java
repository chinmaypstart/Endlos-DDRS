/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.user.operation;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.RoleModel;
import com.endlosiot.common.user.model.RoleModuleRightsKey;
import com.endlosiot.common.user.model.RoleModuleRightsModel;
import com.endlosiot.common.user.service.ModuleService;
import com.endlosiot.common.user.service.RoleService;
import com.endlosiot.common.user.view.ModuleView;
import com.endlosiot.common.user.view.RightsView;
import com.endlosiot.common.user.view.RoleModuleRightsView;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.user.view.RoleView.RoleViewBuilder;
import com.endlosiot.common.view.KeyValueView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class used to perform all business operation on Announcement model.
 *
 * @author Dhruvang.Joshi
 * @since 26/12/2018
 */
@Component(value = "roleOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RoleOperationImpl extends AbstractOperation<RoleModel, RoleView> implements RoleOperation {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModuleService moduleService;

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        RoleModel roleModel = roleService.get(id);
        if (roleModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        RoleViewBuilder roleViewBuilder = fromModelBuilder(roleModel);
        if (roleModel.getRoleModuleRightsModels() != null) {
            setRoleModuleRightsViewBuilder(roleViewBuilder, roleModel);
        }
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                roleViewBuilder.build());
    }

    @Override
    public Response doSave(RoleView roleView) throws EndlosiotAPIException {
        RoleModel roleModel = toModel(getNewModel(), roleView);
        roleService.create(roleModel);
        setRoleModuleRights(roleModel, roleView);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(),
                ResponseCode.SAVE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doUpdate(RoleView roleView) throws EndlosiotAPIException {
        RoleModel roleModel = roleService.get(roleView.getId());
        if (roleModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        roleModel = toModel(roleModel, roleView);
        roleService.update(roleModel);
        setRoleModuleRights(roleModel, roleView);
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(),
                ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        RoleModel roleModel = roleService.get(id);
        if (roleModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        moduleService.deleteRMRFromRoleId(roleModel.getId());
        roleService.hardDelete(roleModel.getId());
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(),
                ResponseCode.DELETE_SUCCESSFULLY.getMessage());
    }

    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public Response doSearch(RoleView roleView, Integer start, Integer recordSize) {
        PageModel pageModel = roleService.searchByLight(roleView, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                pageModel.getRecords(), fromModelList((List<RoleModel>) pageModel.getList()));
    }

    private void setRoleModuleRightsViewBuilder(RoleViewBuilder roleViewBuilder, RoleModel roleModel) {
        List<RoleModuleRightsView> roleModuleRightsViews = new ArrayList<>();
        for (RoleModuleRightsModel roleModuleRightsModel : roleModel.getRoleModuleRightsModels()) {
            RightsView rightsView = new RightsView.RightsViewBuilder().build();
            rightsView = RightsView
                    .setRightView(RightsEnum.fromId(roleModuleRightsModel.getRightsModel().getId().intValue()));
            ModuleView moduleView = new ModuleView.ModuleViewBuilder().build();
            moduleView = ModuleView
                    .setModuleView(ModuleEnum.fromId(roleModuleRightsModel.getModuleModel().getId().intValue()));
            RoleModuleRightsView roleModuleRightsView = new RoleModuleRightsView.RoleModuleRightsViewBuilder()
                    .setModuleView(moduleView).setRightsView(rightsView).build();

            roleModuleRightsViews.add(roleModuleRightsView);
        }
        roleViewBuilder.setRoleModuleRightsViews(roleModuleRightsViews).build();
    }

    @Override
    public RoleModel toModel(RoleModel roleModel, RoleView roleView) throws EndlosiotAPIException {
        roleModel.setDescription(roleView.getDescription());
        roleModel.setName(roleView.getName());
        if (roleView.getTypeId() != null && roleView.getTypeId().getKey() != null) {
            RoleTypeEnum roleTypeEnum = RoleTypeEnum.fromId(roleView.getTypeId().getKey().intValue());
            if (roleTypeEnum != null) {
                roleModel.setTypeId(roleTypeEnum.getId());
            }
        }
        return roleModel;
    }

    @Override
    protected RoleModel getNewModel() {
        return new RoleModel();
    }

    @Override
    public RoleView fromModel(RoleModel roleModel) {
        RoleViewBuilder builder = new RoleView.RoleViewBuilder().setId(roleModel.getId()).setName(roleModel.getName())
                .setDescription(roleModel.getDescription());
        if (Long.valueOf(roleModel.getTypeId()) != null) {
            RoleTypeEnum roleTypeEnum = RoleTypeEnum.fromId(roleModel.getTypeId());
            if (roleTypeEnum != null) {
                builder.setTypeId(KeyValueView.create(roleTypeEnum.getId(), roleTypeEnum.getName()));
            }
        }
        return builder.build();
    }

    public RoleViewBuilder fromModelBuilder(RoleModel roleModel) {
        RoleViewBuilder builder = new RoleView.RoleViewBuilder().setId(roleModel.getId()).setName(roleModel.getName())
                .setDescription(roleModel.getDescription());
        if (Long.valueOf(roleModel.getTypeId()) != null) {
            RoleTypeEnum roleTypeEnum = RoleTypeEnum.fromId(roleModel.getTypeId());
            if (roleTypeEnum != null) {
                builder.setTypeId(KeyValueView.create(roleTypeEnum.getId(), roleTypeEnum.getName()));
            }
        }
        return builder;
    }

    @Override
    public BaseService<RoleModel> getService() {
        return roleService;
    }

    private void setRoleModuleRights(RoleModel roleModel, RoleView roleView) throws EndlosiotAPIException {
        RoleModuleRightsModel tempRoleModuleRightsModel;
        Set<RoleModuleRightsModel> existRoleModuleRightsModels = new HashSet<>();
        Set<RoleModuleRightsModel> toDeleteRoleModuleRightsModels = new HashSet<>();
        Set<RoleModuleRightsModel> toAddRoleModuleRightsModels = new HashSet<>();

        for (RoleModuleRightsView roleModuleRightsView : roleView.getRoleModuleRightsViews()) {
            tempRoleModuleRightsModel = new RoleModuleRightsModel();
            ModuleEnum moduleEnum = ModuleEnum.fromId(roleModuleRightsView.getModuleView().getId());
            if (moduleEnum == null) {
                throw new EndlosiotAPIException(ResponseCode.MODULE_IS_INVALID.getCode(),
                        ResponseCode.MODULE_IS_INVALID.getMessage());
            }
            RightsEnum rightsEnum = RightsEnum.fromId(roleModuleRightsView.getRightsView().getId());
            if (rightsEnum == null) {
                throw new EndlosiotAPIException(ResponseCode.RIGHT_IS_INVALID.getCode(),
                        ResponseCode.RIGHT_IS_INVALID.getMessage());
            }
            RoleModuleRightsKey roleModuleRightsKey = new RoleModuleRightsKey();
            roleModuleRightsKey.setModuleId((long) moduleEnum.getId());
            roleModuleRightsKey.setRightsId(Long.valueOf(rightsEnum.getId()));
            roleModuleRightsKey.setRoleId(roleModel.getId());
            tempRoleModuleRightsModel.setId(roleModuleRightsKey);

            if (roleModel.getRoleModuleRightsModels().stream().anyMatch(o -> o.getId().equals(roleModuleRightsKey))) {
                existRoleModuleRightsModels.add(tempRoleModuleRightsModel);
            } else {
                toAddRoleModuleRightsModels.add(tempRoleModuleRightsModel);
            }
        }
        moduleService.saveRoleModuleRights(toAddRoleModuleRightsModels);
        for (RoleModuleRightsModel roleModuleRightsModel : roleModel.getRoleModuleRightsModels()) {
            if (!existRoleModuleRightsModels.stream().anyMatch(o -> o.getId().equals(roleModuleRightsModel.getId()))) {
                toDeleteRoleModuleRightsModels.add(roleModuleRightsModel);
            }
        }
        for (RoleModuleRightsModel roleModuleRightsModel : toDeleteRoleModuleRightsModels) {
            moduleService.deleteRoleModuleRights(roleModuleRightsModel);
        }
        for (RoleModuleRightsModel roleModuleRightsModel : toAddRoleModuleRightsModels) {
            roleModel.addRoleModuleRightsModels(roleModuleRightsModel);
        }
        roleService.update(roleModel);
    }

/*    @Override
    public Response doDropdown(RoleView roleView) {
        PageModel pageModel = roleService.searchByLight(roleView, null, null);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        List<RoleModel> results = pageModel.getList().stream().map(role -> (RoleModel) role)
                .filter(r -> r.getTypeId() == RoleTypeEnum.MASTER_ADMIN.getId()).collect(Collectors.toList());
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                results.size(), fromModelList(results));
    }*/
@Override
public Response doDropdown() throws EndlosiotAPIException {
    List<RoleModel> roleModels = roleService.findAll();
    if (roleModels == null || roleModels.isEmpty()) {
        throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
    }
    List<RoleView> roleViews = new ArrayList<>();
    for (RoleModel roleModel : roleModels) {
        RoleView roleView = fromModel(roleModel);
        roleViews.add(roleView);
    }
    return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
            roleViews.size(), roleViews);
}
}