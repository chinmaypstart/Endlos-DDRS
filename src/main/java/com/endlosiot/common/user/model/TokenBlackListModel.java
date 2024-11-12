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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * This is Token BlackList model which maps Token BlackList table to class.
 *
 * @author Vidhi Maheta
 * @since 12/04/2021
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "tokenBlackListModel")
@Table(name = "tokenblacklist")
public class TokenBlackListModel extends IdentifierModel {

    private static final long serialVersionUID = -5764068071467332650L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkuserid")
    private UserModel userModel;

    @Column(name = "jwttoken", nullable = false)
    private String jwtToken;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
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
        if (getClass() != obj.getClass())
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