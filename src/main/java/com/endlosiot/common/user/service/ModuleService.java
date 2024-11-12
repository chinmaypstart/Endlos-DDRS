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

import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.model.ModuleModel;
import com.endlosiot.common.user.model.RoleModuleRightsModel;

import java.util.Set;

/**
 * @author Nirav.Shah
 * @since 14/02/2018
 */
public interface ModuleService extends BaseService<ModuleModel> {

    /**
     * This method is used to save role module rights.
     *
     * @param roleModuleRightsModels
     */
    void saveRoleModuleRights(Set<RoleModuleRightsModel> roleModuleRightsModels);

    /**
     * This method is used to delete role module rights.
     *
     * @param roleId
     */
    void deleteRMRFromRoleId(Long roleId);

    /**
     * This method is used to delete role module rights.
     *
     * @param roleModuleRightsModel
     */
    void deleteRoleModuleRights(RoleModuleRightsModel roleModuleRightsModel);
}
