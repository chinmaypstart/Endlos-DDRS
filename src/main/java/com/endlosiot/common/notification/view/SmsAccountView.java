package com.endlosiot.common.notification.view;

import com.endlosiot.common.view.AuditableView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This Class is used to represent sms object in json/in client response
 *
 * @author neha
 * @since 15/02/2023
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = SmsAccountView.SmsAccountViewBuilder.class)
public class SmsAccountView extends AuditableView {
    private static final long serialVersionUID = 1L;
    private final String mobile;
    private final String password;
    private final String senderId;
    private final String peId;

    public final String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getPeId() {
        return peId;
    }

    @Override
    public String toString() {
        return "SmsAccountView [mobile=" + mobile + ", password=" + password + ", senderId=" + senderId + ", peId="
                + peId + "]";
    }

    private SmsAccountView(SmsAccountViewBuilder smsAccountViewBuilder) {
        this.setId(smsAccountViewBuilder.id);
        this.mobile = smsAccountViewBuilder.mobile;
        this.password = smsAccountViewBuilder.password;
        this.senderId = smsAccountViewBuilder.senderId;
        this.peId = smsAccountViewBuilder.peId;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class SmsAccountViewBuilder {
        private Long id;
        private String mobile;
        private String password;
        private String senderId;
        private String peId;

        public SmsAccountViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public SmsAccountViewBuilder setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        public SmsAccountViewBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public SmsAccountViewBuilder setSenderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public SmsAccountViewBuilder setPeId(String peId) {
            this.peId = peId;
            return this;
        }

        public SmsAccountView build() {
            return new SmsAccountView(this);
        }
    }
}
