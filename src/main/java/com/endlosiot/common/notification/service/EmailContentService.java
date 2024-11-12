/*******************************************************************************
 * Copyright -2017 @intentlabs
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
package com.endlosiot.common.notification.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.EmailContentModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.view.EmailContentView;
import com.endlosiot.common.service.BaseService;

/**
 * This is declaration of email content service which defines database operation
 * which can be performed on this table.
 *
 * @author Nirav.Shah
 * @since 12/08/2017
 */
public interface EmailContentService extends BaseService<EmailContentModel> {

    /**
     * Used to search light email content
     *
     * @param emailContentView
     * @param start
     * @param recordSize
     * @return
     */
    PageModel searchLight(EmailContentView emailContentView, Integer start, Integer recordSize);

    /**
     * This method is used to delete.
     *
     * @param emailContentId
     */
    void hardDelete(Long emailContentId);

    /**
     * Used to find email content by notification id.
     *
     * @param notificationModel
     * @return
     */
    EmailContentModel findByNotification(NotificationModel notificationModel);
}