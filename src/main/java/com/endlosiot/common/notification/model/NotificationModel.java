/*******************************************************************************
 * Copyright -2018 @intentlabs
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
package com.endlosiot.common.notification.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.model.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is notification model which contains all types of notification event and
 * trigger related to email/push.
 *
 * @author Nirav.Shah
 * @since 23/05/2020
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "notificationModel")
@Table(name = "notification")
public class NotificationModel implements Model {

    private static final long serialVersionUID = 4365449160483605482L;

    @Id
    @Column(name = "pkid", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "isemail", columnDefinition = "boolean default false")
    private boolean email;

    @Column(name = "ispush", columnDefinition = "boolean default false")
    private boolean push;

    @Column(name = "issms", columnDefinition = "boolean default false")
    private boolean sms;

    @Transient
    private static Map<Long, NotificationModel> MAP = new ConcurrentHashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEmail() {
        return email;
    }

    public void setEmail(boolean email) {
        this.email = email;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isSms() {
        return sms;
    }

    public void setSms(boolean sms) {
        this.sms = sms;
    }

    public static Map<Long, NotificationModel> getMAP() {
        return MAP;
    }

    public static void setMAP(Map<Long, NotificationModel> mAP) {
        MAP = mAP;
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