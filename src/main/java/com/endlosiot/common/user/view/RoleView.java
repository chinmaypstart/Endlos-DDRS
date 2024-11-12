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
package com.endlosiot.common.user.view;

import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.RoleModel;
import com.endlosiot.common.view.AuditableView;
import com.endlosiot.common.view.KeyValueView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;
import java.util.Set;

/**
 * This class is used to represent role object in json/in vendor response.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = RoleView.RoleViewBuilder.class)
public class RoleView extends AuditableView {
    private static final long serialVersionUID = -4444717308537621033L;
    private final String name;
    private final String description;
    private final List<RoleModuleRightsView> roleModuleRightsViews;
    private final KeyValueView typeId;
    private final Long searchGroupId;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RoleModuleRightsView> getRoleModuleRightsViews() {
        return roleModuleRightsViews;
    }

    public KeyValueView getTypeId() {
        return typeId;
    }

    public Long getSearchGroupId() {
        return searchGroupId;
    }

    @Override
    public String toString() {
        return "RoleView [name=" + name + ", description=" + description + ", roleModuleRightsViews="
                + roleModuleRightsViews + ", typeId=" + typeId + ", searchGroupId=" + searchGroupId + "]";
    }

    private RoleView(RoleViewBuilder roleViewBuilder) {
        this.setId(roleViewBuilder.id);
        this.name = roleViewBuilder.name;
        this.description = roleViewBuilder.description;
        this.roleModuleRightsViews = roleViewBuilder.roleModuleRightsViews;
        this.typeId = roleViewBuilder.typeId;
        this.searchGroupId = roleViewBuilder.searchGroupId;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RoleViewBuilder {
        private Long id;
        private String name;
        private String description;
        private List<RoleModuleRightsView> roleModuleRightsViews;
        private KeyValueView typeId;
        private Long searchGroupId;

        public RoleViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public RoleViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RoleViewBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RoleViewBuilder setRoleModuleRightsViews(List<RoleModuleRightsView> roleModuleRightsViews) {
            this.roleModuleRightsViews = roleModuleRightsViews;
            return this;
        }

        public RoleViewBuilder setTypeId(KeyValueView typeId) {
            this.typeId = typeId;
            return this;
        }

        public RoleViewBuilder setSearchGroupId(Long searchGroupId) {
            this.searchGroupId = searchGroupId;
            return this;
        }

        public RoleView build() {
            return new RoleView(this);
        }
    }

    /*public static RoleView setView(RoleModel roleModel) {
        RoleViewBuilder builder = new RoleViewBuilder().setId(roleModel.getId()).setName(roleModel.getName());
        if (Long.valueOf(roleModel.getTypeId()) != null) {
            RoleTypeEnum roleTypeEnum = RoleTypeEnum.fromId(roleModel.getTypeId());
            builder.setTypeId(KeyValueView.create(roleTypeEnum.getId(), roleTypeEnum.getName()));
        }
        return builder.build();
    }*/

    public static RoleView setView(RoleModel roleModel) {
        RoleViewBuilder builder = new RoleViewBuilder()
                .setId(roleModel.getId())
                .setName(roleModel.getName());

        if (Long.valueOf(roleModel.getTypeId()) != null) {
            RoleTypeEnum roleTypeEnum = RoleTypeEnum.fromId(roleModel.getTypeId());
            if (roleTypeEnum != null) {
                builder.setTypeId(KeyValueView.create(roleTypeEnum.getId(), roleTypeEnum.getName()));
            }
        }

        return builder.build();
    }

    public void setViewList(Set<RoleModel> roleModels, List<RoleView> roleViews) {
        for (RoleModel roleModel : roleModels) {
            roleViews.add(setView(roleModel));
        }
    }
}
