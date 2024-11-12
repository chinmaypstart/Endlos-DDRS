package com.endlosiot.common.notification.view;

import com.endlosiot.common.view.AuditableView;
import com.endlosiot.common.view.KeyValueView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class is used to represent smscontent object in json/in client response
 *
 * @author neha
 * @Since 15/02/2023
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = SmsContentView.SmsContentViewBuilder.class)
public class SmsContentView extends AuditableView {
    private static final long serialVersionUID = 1L;
    private final KeyValueView smsAccountView;
    private final String name;
    private final String content;
    private final KeyValueView notificationView;
    private final String templateId;

    public KeyValueView getSmsAccountView() {
        return smsAccountView;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public KeyValueView getNotificationView() {
        return notificationView;
    }

    public String getTemplateId() {
        return templateId;
    }

    @Override
    public String toString() {
        return "SmsContentView [smsAccountView=" + smsAccountView + ", name=" + name + ", content=" + content
                + ", notificationView=" + notificationView + ", templateId=" + templateId + "]";
    }

    private SmsContentView(SmsContentViewBuilder smsContentViewBuilder) {
        this.setId(smsContentViewBuilder.id);
        this.smsAccountView = smsContentViewBuilder.smsAccountView;
        this.name = smsContentViewBuilder.name;
        this.content = smsContentViewBuilder.content;
        this.notificationView = smsContentViewBuilder.notificationView;
        this.templateId = smsContentViewBuilder.templateId;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class SmsContentViewBuilder {
        private Long id;
        private KeyValueView smsAccountView;
        private String name;
        private String content;
        private KeyValueView notificationView;
        private String templateId;

        public SmsContentViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public SmsContentViewBuilder setSmsAccountView(KeyValueView smsAccountView) {
            this.smsAccountView = smsAccountView;
            return this;
        }

        public SmsContentViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public SmsContentViewBuilder setContent(String content) {
            this.content = content;
            return this;
        }

        public SmsContentViewBuilder setNotificationView(KeyValueView notificationView) {
            this.notificationView = notificationView;
            return this;
        }

        public SmsContentViewBuilder setTemplateId(String templateId) {
            this.templateId = templateId;
            return this;
        }

        public SmsContentView build() {
            return new SmsContentView(this);
        }
    }
}
