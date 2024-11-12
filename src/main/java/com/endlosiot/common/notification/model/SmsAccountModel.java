package com.endlosiot.common.notification.model;

import com.endlosiot.common.model.AuditableModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is sms account model which maps sms account table.
 *
 * @author neha
 * @since 15/02/2023
 */

@Entity(name = "smsAccountModel")
@Table(name = "smsaccount")
public class SmsAccountModel extends AuditableModel {

    private static final long serialVersionUID = 1L;

    @Column(name = "mobile", nullable = false, length = 15)
    private String mobile;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @Column(name = "senderid", nullable = false, length = 6)
    private String senderId;

    @Column(name = "peid", nullable = true, length = 20)
    private String peId;

    private static Map<Long, SmsAccountModel> map = new ConcurrentHashMap<>();

    public String getMobile() {
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

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setPeId(String peId) {
        this.peId = peId;
    }

    public static Map<Long, SmsAccountModel> getMap() {
        return map;
    }

    public static void setMap(Map<Long, SmsAccountModel> map) {
        SmsAccountModel.map = map;
    }

    public static void addSmsAccount(SmsAccountModel smsAccountModel) {
        map.put(smsAccountModel.getId(), smsAccountModel);
    }

    public static void removeSmsAccount(SmsAccountModel smsAccountModel) {
        map.remove(smsAccountModel.getId());
    }

    public static void updateSmsAccount(SmsAccountModel smsAccountModel) {
        map.replace(smsAccountModel.getId(), smsAccountModel);
    }
}
