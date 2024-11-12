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
package com.endlosiot.common.threadlocal;

import com.endlosiot.common.model.*;
import com.endlosiot.common.user.model.UserModel;

import java.time.Instant;

/**
 * This class prepares data which needs to be logged using log4j2.
 *
 * @author chetanporwal
 * @since 31/08/2023
 */
public class Auditor {

    private static final ThreadLocal<UserModel> userAuditor = new ThreadLocal<>();

    private Auditor() {
    }

    /**
     * It is used to store activation by user details when any model is
     * activated/de-activated.
     *
     * @param model @apiNote
     */
    public static void activationAudit(Model model, boolean isActive) {
        UserModel userModel = Auditor.userAuditor.get();
        ((ActivationModel) model).setActive(isActive);
        ((ActivationModel) model).setActivationChangeBy(userModel);
        ((ActivationModel) model).setActivationDate(Instant.now().getEpochSecond());
    }

    /**
     * It is used to audit user details when any module data is getting updated.
     *
     * @param model @apiNote
     */
    public static void updateAudit(Model model) {
        if (model instanceof AuditableModel) {
            UserModel userModel = Auditor.userAuditor.get();
            ((AuditableModel) model).setUpdateBy(userModel);
            ((AuditableModel) model).setUpdateDate(Instant.now().getEpochSecond());
        }
    }

    /**
     * It is used to store create by user details when any model is getting created.
     *
     * @param model @apiNote
     */
    public static void createAudit(Model model) {
        if (model instanceof CreateModel) {
            UserModel userModel = Auditor.userAuditor.get();
            if (userModel != null) {
                ((CreateModel) model).setCreateBy(userModel);
            }
            ((CreateModel) model).setCreateDate(Instant.now().getEpochSecond());
        }
    }

    /**
     * It is used to store archive by user details when any model is
     * archived/non-archived.
     *
     * @param model @apiNote
     */
    public static void archiveAudit(Model model) {
        if (model instanceof ArchiveModel) {
            UserModel userModel = Auditor.userAuditor.get();
            ((ArchiveModel) model).setArchiveBy(userModel);
            ((ArchiveModel) model).setArchiveDate(Instant.now().getEpochSecond());
        }
    }

    /**
     * To get current auditor details
     *
     * @return User
     */
    public static UserModel getAuditor() {
        return userAuditor.get();
    }

    /**
     * To set user details into thread local.
     *
     * @return User @apiNote
     */
    public static void setAuditor(UserModel userModel) {
        userAuditor.set(userModel);
    }

    public static void removeAuditor() {
        userAuditor.remove();
    }
}
