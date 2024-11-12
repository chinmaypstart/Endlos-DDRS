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
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.user.model.UserSearchModel;
import com.endlosiot.common.user.view.UserView;

import java.util.List;

public interface UserService extends BaseService<UserModel> {

    /**
     * This method is used to find user through their email id.
     *
     * @param email
     * @return
     */
    UserModel findByEmail(String email);

    /**
     * This method is used to find user through their mobile.
     *
     * @param mobile
     * @return
     */
    UserModel findByMobile(String mobile);

    /**
     * This method is used to get non verified user
     *
     * @param id
     * @return
     */
    UserModel nonVerifiedUser(Long id);

    /**
     * This method is used to insert search param of user.
     *
     * @param userId
     */
    void insertSearchParam(Long userId);

    /**
     * This method is used to update search param of user.
     *
     * @param userId
     */
    void updateSearchParam(Long userId);

    /**
     * This method is used to search user id based on given search parameter using
     * postgresql's full text search.
     *
     * @param searchParam
     * @return
     */
    List<UserSearchModel> fullTextSearch(String searchParam);

    /**
     * This method is used to find user based on verification token.
     *
     * @param token
     * @return
     */
    UserModel findByToken(String token);

    /**
     * This method is used to find user based on reset password token.
     *
     * @param token
     * @return
     */
    UserModel findByResetPasswordToken(String token);

    /**
     * This method is used to search user list using multiple filter parameters.
     *
     * @param userView
     * @param userSearchModels
     * @param start
     * @param recordSize
     * @return
     */
    PageModel searchLight(UserView userView, List<UserSearchModel> userSearchModels, Integer start, Integer recordSize);

    /**
     * This method is used to find user based on email.
     *
     * @param email
     * @return
     */
    UserModel findByEmailActive(String email);

    /**
     * This method is used to find customer based on email.
     *
     * @param email
     * @return
     */
    UserModel findByCustomerEmail(String email);

    /**
     * This method is used to find customer based on mobile.
     *
     * @param email
     * @return
     */
    UserModel findByCustomerMobile(String mobile);

    //UserModel getClientAdmin(ClientModel clientModel);
}