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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * This is user full text search model which is used to search user based on
 * name, email & mobile number. This is very specific to postgres so when you
 * try to implement any other database please remove this class related code.
 *
 * @author Nirav.Shah
 * @since 28/12/2019
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "usersearch")
@NamedNativeQuery(name = "insertUserSearchParam", query = "insert into usersearch values (:id, (select to_tsvector('simple',coalesce(tu.name,'') || ' ' || coalesce(tu.email,'') || ' ' || coalesce(tu.mobile,'')) from users tu where tu.pkid = :id));", resultClass = UserSearchModel.class)
@NamedNativeQuery(name = "updateUserSearchParam", query = "update usersearch set searchparam = (select to_tsvector('simple',coalesce(tu.name,'') || ' ' || coalesce(tu.email,'') || ' ' || coalesce(tu.mobile,'')) from users tu where tu.pkid = :id) where fkuserId = :id", resultClass = UserSearchModel.class)
public class UserSearchModel implements Model {

    private static final long serialVersionUID = -5764068071467332650L;

    @Id
    @Column(name = "fkuserid", nullable = true)
    private Long userId;

    @Column(name = "searchparam", nullable = true)
    private String searchParam;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }

}