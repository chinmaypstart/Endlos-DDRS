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
package com.endlosiot.common.notification.view;

import com.endlosiot.common.view.ArchiveView;
import com.endlosiot.common.view.KeyValueView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This Class is used to represent email object in json/in vendor response
 *
 * @author Nirav.Shah
 * @Since 12/07/2018
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = EmailAccountView.EmailAccountViewBuilder.class)
public class EmailAccountView extends ArchiveView {
    private static final long serialVersionUID = 2396198127017769592L;
    private final String name;
    private final String host;
    private final Long port;
    private final String userName;
    private final String password;
    private final String replyToEmail;
    private final String emailFrom;
    private final Long ratePerHour;
    private final Long updateRatePerHour;
    private final Long ratePerDay;
    private final Long updateRatePerDay;
    private final KeyValueView authenticationMethod;
    private final KeyValueView authenticationSecurity;
    private final Long timeOut;

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public Long getPort() {
        return port;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public Long getRatePerHour() {
        return ratePerHour;
    }

    public Long getUpdateRatePerHour() {
        return updateRatePerHour;
    }

    public Long getRatePerDay() {
        return ratePerDay;
    }

    public Long getUpdateRatePerDay() {
        return updateRatePerDay;
    }

    public KeyValueView getAuthenticationMethod() {
        return authenticationMethod;
    }

    public KeyValueView getAuthenticationSecurity() {
        return authenticationSecurity;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    @Override
    public String toString() {
        return "EmailAccountView [name=" + name + ", host=" + host + ", port=" + port + ", userName=" + userName
                + ", password=" + password + ", replyToEmail=" + replyToEmail + ", emailFrom=" + emailFrom
                + ", ratePerHour=" + ratePerHour + ", updateRatePerHour=" + updateRatePerHour + ", ratePerDay="
                + ratePerDay + ", updateRatePerDay=" + updateRatePerDay + ", authenticationMethod="
                + authenticationMethod + ", authenticationSecurity=" + authenticationSecurity + ", timeOut=" + timeOut
                + "]";
    }

    private EmailAccountView(EmailAccountViewBuilder emailAccountViewBuilder) {
        this.setId(emailAccountViewBuilder.id);
        this.name = emailAccountViewBuilder.name;
        this.host = emailAccountViewBuilder.host;
        this.port = emailAccountViewBuilder.port;
        this.userName = emailAccountViewBuilder.userName;
        this.password = emailAccountViewBuilder.password;
        this.replyToEmail = emailAccountViewBuilder.replyToEmail;
        this.emailFrom = emailAccountViewBuilder.emailFrom;
        this.ratePerHour = emailAccountViewBuilder.ratePerHour;
        this.updateRatePerHour = emailAccountViewBuilder.updateRatePerHour;
        this.ratePerDay = emailAccountViewBuilder.ratePerDay;
        this.updateRatePerDay = emailAccountViewBuilder.updateRatePerDay;
        this.authenticationMethod = emailAccountViewBuilder.authenticationMethod;
        this.authenticationSecurity = emailAccountViewBuilder.authenticationSecurity;
        this.timeOut = emailAccountViewBuilder.timeOut;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class EmailAccountViewBuilder {
        private Long id;
        private String name;
        private String host;
        private Long port;
        private String userName;
        private String password;
        private String replyToEmail;
        private String emailFrom;
        private Long ratePerHour;
        private Long updateRatePerHour;
        private Long ratePerDay;
        private Long updateRatePerDay;
        private KeyValueView authenticationMethod;
        private KeyValueView authenticationSecurity;
        private Long timeOut;

        public EmailAccountViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EmailAccountViewBuilder setHost(String host) {
            this.host = host;
            return this;
        }

        public EmailAccountViewBuilder setPort(Long port) {
            this.port = port;
            return this;
        }

        public EmailAccountViewBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public EmailAccountViewBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public EmailAccountViewBuilder setReplyToEmail(String replyToEmail) {
            this.replyToEmail = replyToEmail;
            return this;
        }

        public EmailAccountViewBuilder setEmailFrom(String emailFrom) {
            this.emailFrom = emailFrom;
            return this;
        }

        public EmailAccountViewBuilder setRatePerHour(Long ratePerHour) {
            this.ratePerHour = ratePerHour;
            return this;
        }

        public EmailAccountViewBuilder setUpdateRatePerHour(Long updateRatePerHour) {
            this.updateRatePerHour = updateRatePerHour;
            return this;
        }

        public EmailAccountViewBuilder setRatePerDay(Long ratePerDay) {
            this.ratePerDay = ratePerDay;
            return this;
        }

        public EmailAccountViewBuilder setUpdateRatePerDay(Long updateRatePerDay) {
            this.updateRatePerDay = updateRatePerDay;
            return this;
        }

        public EmailAccountViewBuilder setAuthenticationMethod(KeyValueView authenticationMethod) {
            this.authenticationMethod = authenticationMethod;
            return this;
        }

        public EmailAccountViewBuilder setAuthenticationSecurity(KeyValueView authenticationSecurity) {
            this.authenticationSecurity = authenticationSecurity;
            return this;
        }

        public EmailAccountViewBuilder setTimeOut(Long timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        public EmailAccountViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public EmailAccountView build() {
            return new EmailAccountView(this);
        }
    }
}