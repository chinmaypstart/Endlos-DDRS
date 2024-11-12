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

import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.view.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class is used to represent rights object in json/in vendor response.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = RightsView.RightsViewBuilder.class)
public class RightsView implements View {
    private static final long serialVersionUID = 4740087385931669057L;
    private final Integer id;
    private final String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RightsView [id=" + id + ", name=" + name + "]";
    }

    private RightsView(RightsViewBuilder rightsViewBuilder) {
        this.id = rightsViewBuilder.id;
        this.name = rightsViewBuilder.name;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class RightsViewBuilder {
        private Integer id;
        private String name;

        public RightsViewBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public RightsViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public RightsView build() {
            return new RightsView(this);
        }
    }

    public static RightsView setRightView(RightsEnum rightsEnum) {
        return new RightsViewBuilder().setId(rightsEnum.getId()).setName(rightsEnum.getName()).build();
    }
}
