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

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.RoleModel;
import com.endlosiot.common.user.view.RoleView;

import java.util.List;

/**
 * @author Nirav.Shah
 * @since 08/02/2018
 */
public interface RoleService extends BaseService<RoleModel> {

    /**
     * This method is used to delete a role vendorModel.getId()
     *
     * @param roleId
     */
    void hardDelete(Long roleId);

    RoleModel getByRoleType(RoleTypeEnum roleTypeEnum);

    /**
     * This method is used to search role based on light entity.
     *
     * @param roleView
     * @param start
     * @param recordSize
     * @return
     */
    PageModel searchByLight(RoleView roleView, Integer start, Integer recordSize);

    /**
     * This method is used to fetch role base rights.
     *
     * @param id @apiNote
     * @return {@return }
     */
    List<RoleModel> getRights(List<Long> id);

    List<RoleModel> getByName(String name);

    List<RoleModel> getByNameExcludingId(String name, Long currentId);
}