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
package com.endlosiot.common.notification.view;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.RegexEnum;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.ArchiveView;
import com.endlosiot.common.view.KeyValueView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * This class is used to represent emailcontent object in json/in vendor
 * response
 *
 * @author Nirav.Shah
 * @Since 12/07/2018
 */

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = EmailContentView.EmailContentViewBuilder.class)
public class EmailContentView extends ArchiveView {
    private static final long serialVersionUID = -7632495476077413752L;
    private final KeyValueView emailAccountView;
    private final String content;
    private final String subject;
    private final String emailBcc;
    private final String emailCc;
    private final KeyValueView notificationView;

    public KeyValueView getEmailAccountView() {
        return emailAccountView;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    public String getEmailBcc() {
        return emailBcc;
    }

    public String getEmailCc() {
        return emailCc;
    }

    public KeyValueView getNotificationView() {
        return notificationView;
    }

    @Override
    public String toString() {
        return "EmailContentView [emailAccountView=" + emailAccountView + ", content=" + content + ", subject="
                + subject + ", emailBcc=" + emailBcc + ", emailCc=" + emailCc + ", notificationView=" + notificationView
                + "]";
    }

    private EmailContentView(EmailContentViewBuilder emailContentViewBuilder) {
        this.setId(emailContentViewBuilder.id);
        this.emailAccountView = emailContentViewBuilder.emailAccountView;
        this.content = emailContentViewBuilder.content;
        this.subject = emailContentViewBuilder.subject;
        this.emailBcc = emailContentViewBuilder.emailBcc;
        this.emailCc = emailContentViewBuilder.emailCc;
        this.notificationView = emailContentViewBuilder.notificationView;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class EmailContentViewBuilder {
        private Long id;
        private KeyValueView emailAccountView;
        private String content;
        private String subject;
        private String emailBcc;
        private String emailCc;
        private KeyValueView notificationView;

        public EmailContentViewBuilder setEmailAccountView(KeyValueView emailAccountView) {
            this.emailAccountView = emailAccountView;
            return this;
        }

        public EmailContentViewBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public EmailContentViewBuilder setSubject(String subject) {
            this.subject = subject;
            return this;
        }

        public EmailContentViewBuilder setEmailBcc(String emailBcc) {
            this.emailBcc = emailBcc;
            return this;
        }

        public EmailContentViewBuilder setEmailCc(String emailCc) {
            this.emailCc = emailCc;
            return this;
        }

        public EmailContentViewBuilder setNotificationView(KeyValueView notificationView) {
            this.notificationView = notificationView;
            return this;
        }

        public EmailContentViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public EmailContentView build() {
            return new EmailContentView(this);
        }
    }

    public static void isValid(EmailContentView emailContentView) throws EndlosiotAPIException {
        if (emailContentView.getEmailAccountView() == null
                || (emailContentView.getEmailAccountView() != null
                && emailContentView.getEmailAccountView().getKey() == null)
                || emailContentView.getEmailAccountView().getKey() == 0) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_ACCOUNT_IS_MISSING.getCode(),
                    ResponseCode.EMAIL_ACCOUNT_IS_MISSING.getMessage());
        }
        if (StringUtils.isBlank(emailContentView.getContent())) {
            throw new EndlosiotAPIException(ResponseCode.EMAIL_CONTENT_IS_MISSING.getCode(),
                    ResponseCode.EMAIL_CONTENT_IS_MISSING.getMessage());
        }
        Validator.STRING.isValid(new InputField("SUBJECT", emailContentView.getSubject(), true, 1000));
        Validator.STRING.isValid(new InputField("EMAIL_BCC", emailContentView.getEmailBcc(), false, RegexEnum.EMAIL));
        Validator.STRING.isValid(new InputField("EMAIL_CC", emailContentView.getEmailCc(), false, RegexEnum.EMAIL));
        if (emailContentView.getNotificationView() == null || (emailContentView.getNotificationView() != null
                && emailContentView.getNotificationView().getKey() == null)) {
            throw new EndlosiotAPIException(ResponseCode.NOTIFICATION_IS_MISSING.getCode(),
                    ResponseCode.NOTIFICATION_IS_MISSING.getMessage());
        }
        if (NotificationEnum.fromId(emailContentView.getNotificationView().getKey().intValue()) == null) {
            throw new EndlosiotAPIException(ResponseCode.NOTIFICATION_IS_INVALID.getCode(),
                    ResponseCode.NOTIFICATION_IS_INVALID.getMessage());
        }
    }
}