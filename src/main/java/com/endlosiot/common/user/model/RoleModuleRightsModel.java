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
package com.endlosiot.common.user.model;

import com.endlosiot.common.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Role Module is used to map role module rights table.
 *
 * @author Nirav.Shah
 * @since 05/08/2018
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "rolemoduleright")
@Entity(name = "roleModuleRightsModel")
public class RoleModuleRightsModel implements Model {

    private static final long serialVersionUID = 8817523517179583864L;

    @EmbeddedId
    RoleModuleRightsKey id;

    @ManyToOne
    @JoinColumn(name = "fkmoduleid", referencedColumnName = "pkid", insertable = false, updatable = false)
    ModuleModel moduleModel;

    @ManyToOne
    @JoinColumn(name = "fkroleid", referencedColumnName = "pkid", insertable = false, updatable = false)
    RoleModel roleModel;

    @ManyToOne
    @JoinColumn(name = "fkrightsid", referencedColumnName = "pkid", insertable = false, updatable = false)
    RightsModel rightsModel;

    @Transient
    private Long moduleId;
    @Transient
    private Long roleId;
    @Transient
    private Long rightsId;

    public RoleModuleRightsKey getId() {
        return id;
    }

    public void setId(RoleModuleRightsKey id) {
        this.id = id;
    }

    public ModuleModel getModuleModel() {
        return moduleModel;
    }

    @JsonIgnore
    public void setModuleModel(ModuleModel moduleModel) {
        this.moduleModel = moduleModel;
    }

    public RoleModel getRoleModel() {
        return roleModel;
    }

    public void setRoleModel(RoleModel roleModel) {
        this.roleModel = roleModel;
    }

    public RightsModel getRightsModel() {
        return rightsModel;
    }

    public void setRightsModel(RightsModel rightsModel) {
        this.rightsModel = rightsModel;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRightsId() {
        return rightsId;
    }

    public void setRightsId(Long rightsId) {
        this.rightsId = rightsId;
    }
}