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

import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.view.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

/**
 * This class is used to represent module object in json/in vendor response.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = ModuleView.ModuleViewBuilder.class)
public class ModuleView implements View {
    private static final long serialVersionUID = 4740087385931669057L;
    private final Integer id;
    private final String name;
    private final List<RightsView> rightsViews;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RightsView> getRightsViews() {
        return rightsViews;
    }

    @Override
    public String toString() {
        return "ModuleView [id=" + id + ", name=" + name + ", rightsViews=" + rightsViews + "]";
    }

    private ModuleView(ModuleViewBuilder moduleViewBuilder) {
        this.id = moduleViewBuilder.id;
        this.name = moduleViewBuilder.name;
        this.rightsViews = moduleViewBuilder.rightsViews;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class ModuleViewBuilder {
        private Integer id;
        private String name;
        private List<RightsView> rightsViews;

        public ModuleViewBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public ModuleViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ModuleViewBuilder setRightsViews(List<RightsView> rightsViews) {
            this.rightsViews = rightsViews;
            return this;
        }

        public ModuleView build() {
            return new ModuleView(this);
        }
    }

    public static ModuleView setModuleView(ModuleEnum moduleEnum) {
        return new ModuleViewBuilder().setId(moduleEnum.getId()).setName(moduleEnum.getName()).build();
    }
}