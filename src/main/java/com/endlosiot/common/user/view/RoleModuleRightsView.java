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

import com.endlosiot.common.view.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class is used to represent rolemodulerights object in json/in vendor
 * response.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = RoleModuleRightsView.RoleModuleRightsViewBuilder.class)
public class RoleModuleRightsView implements View {
    private static final long serialVersionUID = -6639006849279463177L;
    private final RightsView rightsView;
    private final ModuleView moduleView;

    public RightsView getRightsView() {
        return rightsView;
    }

    public ModuleView getModuleView() {
        return moduleView;
    }

    @Override
    public String toString() {
        return "RoleModuleRightsView [rightsView=" + rightsView + ", moduleView=" + moduleView + "]";
    }

    private RoleModuleRightsView(RoleModuleRightsViewBuilder roleModuleRightsViewBuilder) {
        this.rightsView = roleModuleRightsViewBuilder.rightsView;
        this.moduleView = roleModuleRightsViewBuilder.moduleView;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RoleModuleRightsViewBuilder {
        private RightsView rightsView;
        private ModuleView moduleView;

        public RoleModuleRightsViewBuilder setRightsView(RightsView rightsView) {
            this.rightsView = rightsView;
            return this;
        }

        public RoleModuleRightsViewBuilder setModuleView(ModuleView moduleView) {
            this.moduleView = moduleView;
            return this;
        }

        public RoleModuleRightsView build() {
            return new RoleModuleRightsView(this);
        }
    }
}
