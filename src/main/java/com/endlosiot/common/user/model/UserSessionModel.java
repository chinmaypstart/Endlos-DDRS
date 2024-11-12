package com.endlosiot.common.user.model;

import com.endlosiot.common.model.IdentifierModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "userSessionModel")
@Table(name = "usersession")
public class UserSessionModel extends IdentifierModel {

    private static final long serialVersionUID = 1L;

    @Column(name = "browser", nullable = true, length = 100)
    private String browser;

    @Column(name = "operatingsystem", nullable = true, length = 500)
    private String os;

    @Column(name = "ipaddress", nullable = true, length = 50)
    private String ip;

    @Column(name = "logindate", nullable = false)
    private long loginDate;

    @Column(name = "devicecookie", nullable = false, length = 100)
    private String deviceCookie;

    @Column(name = "datedevicecookie", nullable = false)
    private Long deviceCookieDate;

    @Column(name = "datelastlogin", nullable = false, length = 100)
    private Long lastLoginDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkuserid")
    private UserModel userModel;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(long loginDate) {
        this.loginDate = loginDate;
    }

    public String getDeviceCookie() {
        return deviceCookie;
    }

    public void setDeviceCookie(String deviceCookie) {
        this.deviceCookie = deviceCookie;
    }

    public Long getDeviceCookieDate() {
        return deviceCookieDate;
    }

    public void setDeviceCookieDate(Long deviceCookieDate) {
        this.deviceCookieDate = deviceCookieDate;
    }

    public Long getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Long lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserSessionModel(String browser, String os, String ip, String deviceCookie, Long deviceCookieDate,
                            Long loginDate, UserModel userModel) {
        super();
        this.browser = browser;
        this.os = os;
        this.ip = ip;
        this.deviceCookie = deviceCookie;
        this.deviceCookieDate = deviceCookieDate;
        this.loginDate = loginDate;
        this.userModel = userModel;
    }

    public UserSessionModel() {
        super();
    }

}
