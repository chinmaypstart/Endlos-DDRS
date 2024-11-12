package com.endlosiot.common.notification.view;

import com.endlosiot.common.view.IdentifierView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class is used to represent notification object in json/in client
 * response
 *
 * @author Nirav.Shah
 * @Since 23/05/2020
 */

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = NotificationView.NotificationViewBuilder.class)
public class NotificationView extends IdentifierView {
    private static final long serialVersionUID = -7632495476077413752L;
    private final String name;
    private final boolean email;
    private final boolean push;

    public String getName() {
        return name;
    }

    public boolean isEmail() {
        return email;
    }

    public boolean isPush() {
        return push;
    }

    @Override
    public String toString() {
        return "NotificationView [name=" + name + ", email=" + email + ", push=" + push + "]";
    }

    private NotificationView(NotificationViewBuilder notificationViewBuilder) {
        this.setId(notificationViewBuilder.id);
        this.name = notificationViewBuilder.name;
        this.email = notificationViewBuilder.email;
        this.push = notificationViewBuilder.push;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class NotificationViewBuilder {
        private Long id;
        private String name;
        private boolean email;
        private boolean push;

        public NotificationViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public NotificationViewBuilder setEmail(boolean email) {
            this.email = email;
            return this;
        }

        public NotificationViewBuilder setPush(boolean push) {
            this.push = push;
            return this;
        }

        public NotificationViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public NotificationView build() {
            return new NotificationView(this);
        }
    }
}