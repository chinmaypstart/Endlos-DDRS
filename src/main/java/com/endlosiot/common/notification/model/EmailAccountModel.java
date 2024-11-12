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

import com.endlosiot.common.model.ActivationModel;
import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.notification.enums.EmailAuthenticationMethod;
import com.endlosiot.common.notification.enums.EmailAuthenticationSecurity;
import com.endlosiot.common.notification.enums.ProviderType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is Email account model which maps email account table.
 *
 * @author Dhruvang.Joshi
 * @since 26/07/2017
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "emailAccountModel")
@Table(name = "emailaccount")
public class EmailAccountModel extends ActivationModel {

    private static final long serialVersionUID = 4948296735319653785L;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "host", nullable = false, length = 500)
    private String host;

    @Column(name = "intport", nullable = true)
    private Long port;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 500)
    private String password;

    @Column(name = "replytoemail", nullable = false, length = 100)
    private String replyToEmail;

    @Column(name = "emailfrom", nullable = false, length = 500)
    private String emailFrom;

    @Column(name = "intrateperhour", nullable = true)
    private Long ratePerHour;

    @Column(name = "intupdaterateperhour", nullable = true)
    private Long updateRatePerHour;

    @Column(name = "intrateperday", nullable = true)
    private Long ratePerDay;

    @Column(name = "intupdaterateperday", nullable = true)
    private Long updateRatePerDay;

    @Column(name = "enumauthmethod", nullable = false)
    private int authenticationMethod;

    @Column(name = "enumauthsecurity", nullable = false)
    private int authenticationSecurity;

    @Column(name = "inttimeout", nullable = false)
    private Long timeOut;

    @Transient
    private static Map<Long, EmailAccountModel> MAP = new ConcurrentHashMap<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Long getPort() {
        return port;
    }

    public void setPort(Long port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmailAuthenticationMethod getAuthenticationMethod() {
        return EmailAuthenticationMethod.fromId(authenticationMethod);
    }

    public void setAuthenticationMethod(Integer authenticationMethod) {
        this.authenticationMethod = authenticationMethod;
    }

    public EmailAuthenticationSecurity getAuthenticationSecurity() {
        return EmailAuthenticationSecurity.fromId(authenticationSecurity);
    }

    public void setAuthenticationSecurity(Integer authenticationSecurity) {
        this.authenticationSecurity = authenticationSecurity;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public Long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Long timeOut) {
        this.timeOut = timeOut;
    }

    public String getReplyToEmail() {
        return replyToEmail;
    }

    public void setReplyToEmail(String replyToEmail) {
        this.replyToEmail = replyToEmail;
    }

    public static void addEmailAccount(EmailAccountModel emailAccountModel) {
        MAP.put(emailAccountModel.getId(), emailAccountModel);
    }

    public static void removeEmailAccount(EmailAccountModel emailAccountModel) {
        MAP.remove(emailAccountModel.getId());
    }

    public static void updateEmailAccount(EmailAccountModel emailAccountModel) {
        MAP.replace(emailAccountModel.getId(), emailAccountModel);
    }

    public Long getRatePerHour() {
        return ratePerHour;
    }

    public void setRatePerHour(Long ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    public Long getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(Long ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public Long getUpdateRatePerHour() {
        return updateRatePerHour;
    }

    public void setUpdateRatePerHour(Long updateRatePerHour) {
        this.updateRatePerHour = updateRatePerHour;
    }

    public Long getUpdateRatePerDay() {
        return updateRatePerDay;
    }

    public void setUpdateRatePerDay(Long updateRatePerDay) {
        this.updateRatePerDay = updateRatePerDay;
    }

    public static Map<Long, EmailAccountModel> getMAP() {
        return MAP;
    }

    public ProviderType getProviderType() {
        if (EmailAuthenticationSecurity.TLS.getId().equals(authenticationSecurity)) {
            return ProviderType.TLS_SECURE;
        } else if (EmailAuthenticationSecurity.SSL.getId().equals(authenticationSecurity)) {
            return ProviderType.SSL_SECURE;
        } else {
            return null;
        }
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