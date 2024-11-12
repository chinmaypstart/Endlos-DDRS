package com.endlosiot.common.user.view;

import com.endlosiot.common.view.IdentifierView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = UserSessionView.UserSessionViewBuilder.class)
public class UserSessionView extends IdentifierView {
    private static final long serialVersionUID = 1L;
    private final String browser;
    private final String os;
    private final String ip;
    private final Long loginDate;
    private final UserView userView;

    public String getBrowser() {
        return browser;
    }

    public String getOs() {
        return os;
    }

    public String getIp() {
        return ip;
    }

    public Long getLoginDate() {
        return loginDate;
    }

    public UserView getUserView() {
        return userView;
    }

    @Override
    public String toString() {
        return "UserSessionView [browser=" + browser + ", os=" + os + ", ip=" + ip + ", loginDate=" + loginDate
                + ", userView=" + userView + "]";
    }

    private UserSessionView(UserSessionViewBuilder userSessionViewBuilder) {
        this.browser = userSessionViewBuilder.browser;
        this.os = userSessionViewBuilder.os;
        this.ip = userSessionViewBuilder.ip;
        this.loginDate = userSessionViewBuilder.loginDate;
        this.userView = userSessionViewBuilder.userView;
        this.setId(userSessionViewBuilder.id);
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class UserSessionViewBuilder {
        private Long id;
        private String browser;
        private String os;
        private String ip;
        private Long loginDate;
        private UserView userView;

        public UserSessionViewBuilder setBrowser(String browser) {
            this.browser = browser;
            return this;
        }

        public UserSessionViewBuilder setOs(String os) {
            this.os = os;
            return this;
        }

        public UserSessionViewBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public UserSessionViewBuilder setLoginDate(Long loginDate) {
            this.loginDate = loginDate;
            return this;
        }

        public UserSessionViewBuilder setUserView(UserView userView) {
            this.userView = userView;
            return this;
        }

        public UserSessionViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public UserSessionView build() {
            return new UserSessionView(this);
        }
    }
}