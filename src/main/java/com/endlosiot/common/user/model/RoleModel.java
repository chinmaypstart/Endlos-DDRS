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

import com.endlosiot.common.model.AuditableModel;
import com.endlosiot.common.model.IdentifierModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

/**
 * Role model defines different types of stack holders.
 *
 * @author Nirav.Shah
 * @since 05/08/2018
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "roleModel")
@Table(name = "role")
public class RoleModel extends AuditableModel {

    private static final long serialVersionUID = -958699503810782913L;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "description", nullable = true, length = 256)
    private String description;

    @Column(name = "enumtype", nullable = false)
    private int typeId;

    @OneToMany(mappedBy = "roleModel")
    private Set<RoleModuleRightsModel> roleModuleRightsModels = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Set<RoleModuleRightsModel> getRoleModuleRightsModels() {
        return roleModuleRightsModels;
    }

    public void setRoleModuleRightsModels(Set<RoleModuleRightsModel> roleModuleRightsModels) {
        this.roleModuleRightsModels = roleModuleRightsModels;
    }

    public void addRoleModuleRightsModels(RoleModuleRightsModel roleModuleRightsModels) {
        this.roleModuleRightsModels.add(roleModuleRightsModels);
    }

    public void removeRoleModuleRightsModels(RoleModuleRightsModel roleModuleRightsModels) {
        this.roleModuleRightsModels.remove(roleModuleRightsModels);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        IdentifierModel other = (IdentifierModel) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}
