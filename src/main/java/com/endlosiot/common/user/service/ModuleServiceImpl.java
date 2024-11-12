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
package com.endlosiot.common.user.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.model.ModuleModel;
import com.endlosiot.common.user.model.RoleModuleRightsKey;
import com.endlosiot.common.user.model.RoleModuleRightsModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class used to implement all database related operation that will be
 * performed on module table.
 *
 * @author Nirav.Shah
 * @since 14/02/2018
 */
@Service(value = "moduleService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ModuleServiceImpl extends AbstractService<ModuleModel>
        implements ModuleService, CustomInitializationBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    RoleService roleService;
    @Autowired
    RightsService rightsService;

    private static final String entityName = "roleModuleRightsModel";

    @Override
    public Class<ModuleModel> getEntityClass() {
        return ModuleModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<ModuleModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<ModuleModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void onStartUp() throws EndlosiotAPIException {
        for (Entry<Integer, ModuleEnum> modulemap : ModuleEnum.MAP.entrySet()) {
            ModuleModel moduleModel = get(modulemap.getKey());
            if (moduleModel == null) {
                moduleModel = new ModuleModel();
                moduleModel.setId(modulemap.getKey().longValue());
                moduleModel.setName(modulemap.getValue().getName());
                create(moduleModel);
                setRoleModuleRights(moduleModel);
            }
        }
    }

    /**
     * This method is used to insert a new created modules and related role at time
     * of server startup. So no need to write migration query to assign module and
     * rights to each role.
     *
     * @param moduleModel
     */
    private void setRoleModuleRights(ModuleModel moduleModel) {
        ModuleEnum moduleEnum = ModuleEnum.fromId(moduleModel.getId().intValue());
        switch (moduleEnum) {
            case USER:
                setRoleModuleRights(moduleEnum, moduleModel, 1l);
                setRoleModuleViewRights(moduleEnum, moduleModel, 2l);
                break;
            case REPORT, DASHBOARD:
                setRoleModuleRights(moduleEnum, moduleModel, 1l);
                setRoleModuleRights(moduleEnum, moduleModel, 2l);
                setRoleModuleRights(moduleEnum, moduleModel, 3l);
                break;
            case ROLE:
            case SYSTEM_SETTING:
            case ALARM_HISTORY:
            case DEVICE:
            case SCREEN:
            case DEVICE_DIAGNOSIS:
                setRoleModuleRights(moduleEnum, moduleModel, 1l);
                break;
        }
    }

    /**
     * This method is used to setup module wise rights for global admin role.
     *
     * @param moduleEnum
     * @param moduleModel
     */
    private void setRoleModuleRights(ModuleEnum moduleEnum, ModuleModel moduleModel, long roleId) {
        for (RightsEnum rightsEnum : moduleEnum.getAssignedRights()) {
            RoleModuleRightsKey roleModuleRightsKey = new RoleModuleRightsKey();
            roleModuleRightsKey.setModuleId(moduleModel.getId());
            roleModuleRightsKey.setRightsId(Long.valueOf(rightsEnum.getId()));
            roleModuleRightsKey.setRoleId(roleId);
            RoleModuleRightsModel roleModuleRightsModel = new RoleModuleRightsModel();
            roleModuleRightsModel.setId(roleModuleRightsKey);
            getSession().save(entityName, roleModuleRightsModel);
        }
    }

    private void setRoleModuleViewRights(ModuleEnum moduleEnum, ModuleModel moduleModel, long roleId) {
        moduleEnum.getAssignedRights().forEach(rightsEnum -> {
            if (RightsEnum.VIEW.equals(rightsEnum)) {
                RoleModuleRightsKey roleModuleRightsKey = new RoleModuleRightsKey();
                roleModuleRightsKey.setModuleId(moduleModel.getId());
                roleModuleRightsKey.setRightsId(Long.valueOf(rightsEnum.getId()));
                roleModuleRightsKey.setRoleId(roleId);
                RoleModuleRightsModel roleModuleRightsModel = new RoleModuleRightsModel();
                roleModuleRightsModel.setId(roleModuleRightsKey);
                getSession().save(entityName, roleModuleRightsModel);
            }
        });
    }

    @Override
    public void saveRoleModuleRights(Set<RoleModuleRightsModel> roleModuleRightsModels) {
        for (RoleModuleRightsModel roleModuleRightsModel : roleModuleRightsModels) {
            getSession().save(entityName, roleModuleRightsModel);
        }
    }

    @Override
    public void deleteRMRFromRoleId(Long id) {
        entityManager.createQuery("delete from roleModuleRightsModel r where r.roleModel.id = :id")
                .setParameter("id", id).executeUpdate();
    }

    @Override
    public void deleteRoleModuleRights(RoleModuleRightsModel roleModuleRightsModel) {
        getSession().delete(roleModuleRightsModel);
    }

    @Override
    protected CriteriaQuery<ModuleModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(ModuleModel.class);
    }
}