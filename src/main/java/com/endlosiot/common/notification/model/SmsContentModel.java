package com.endlosiot.common.notification.model;

import com.endlosiot.common.model.AuditableModel;
import jakarta.persistence.*;

/**
 * This is SmsContentModel model which maps SmsContentModel table to class.
 *
 * @author neha
 * @since 15/02/2023
 */
@Entity(name = "smsContentModel")
@Table(name = "smscontent")
public class SmsContentModel extends AuditableModel {

    private static final long serialVersionUID = -3182686400336723872L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fksmsaccountid")
    private SmsAccountModel smsAccountModel;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fknotificationid")
    private NotificationModel notificationModel;

    @Column(name = "templateid", nullable = true, length = 20)
    private String templateId;

    public SmsAccountModel getSmsAccountModel() {
        return smsAccountModel;
    }

    public void setSmsAccountModel(SmsAccountModel smsAccountModel) {
        this.smsAccountModel = smsAccountModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationModel getNotificationModel() {
        return notificationModel;
    }

    public void setNotificationModel(NotificationModel notificationModel) {
        this.notificationModel = notificationModel;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
