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

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.screen.model.ScreenModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * This is User Password model which maps user password table to class.
 *
 * @author Milan.Gohil
 * @since 29/07/2024
 */

@Entity(name = "userScreenModel")
@Table(name = "userscreen")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserScreenModel extends IdentifierModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkuserid")
    private UserModel userModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkscreenid")
    private ScreenModel screenModel;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ScreenModel getScreenModel() {
        return screenModel;
    }

    public void setScreenModel(ScreenModel screenModel) {
        this.screenModel = screenModel;
    }

    public static class UserRoleId implements Serializable {
        private Long userModel;
        private Long screenModel;

        public Long getUserModel() {
            return userModel;
        }

        public void setUserModel(Long userModel) {
            this.userModel = userModel;
        }

        public Long getScreenModel() {
            return screenModel;
        }

        public void setScreenModel(Long screenModel) {
            this.screenModel = screenModel;
        }
    }
}