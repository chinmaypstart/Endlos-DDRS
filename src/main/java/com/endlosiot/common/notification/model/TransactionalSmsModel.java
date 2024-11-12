package com.endlosiot.common.notification.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.notification.enums.TransactionStatusEnum;
import jakarta.persistence.*;

/**
 * This is Transaction Sms model which maps Transaction sms table of database.
 * Transaction sms like (Password generation, activation link etc) will fall
 * under this category. User action related mail fall under this category.
 *
 * @author neha
 * @since 15/02/2023
 */

@Entity(name = "transactionalSmsModel")
@Table(name = "transactionalsms")
public class TransactionalSmsModel extends IdentifierModel {

    private static final long serialVersionUID = 1L;
    @Version
    @Column(name = "lockversion", nullable = true)
    private Long lockVersion;

    @Column(name = "smsto", nullable = false)
    private String smsTo;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "datesend", nullable = false)
    private Long dateSend;

    @Column(name = "datesent", nullable = true)
    private Long dateSent;

    @Column(name = "enumstatus", nullable = false)
    private int status;

    @Column(name = "numberretrycount", nullable = false, columnDefinition = "smallint default 0")
    private Long retryCount;

    @Column(name = "error", nullable = true)
    private String error;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fksmsaccountid")
    private SmsAccountModel smsAccountModel;

    @Column(name = "messageid", nullable = true, length = 34)
    private String messageId;

    @Column(name = "templateid", nullable = true, length = 20)
    private String templateId;

    public Long getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Long lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getSmsTo() {
        return smsTo;
    }

    public void setSmsTo(String smsTo) {
        this.smsTo = smsTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDateSend() {
        return dateSend;
    }

    public void setDateSend(Long dateSend) {
        this.dateSend = dateSend;
    }

    public Long getDateSent() {
        return dateSent;
    }

    public void setDateSent(Long dateSent) {
        this.dateSent = dateSent;
    }

    public TransactionStatusEnum getStatus() {
        return TransactionStatusEnum.fromId(status);
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Long retryCount) {
        this.retryCount = retryCount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public SmsAccountModel getSmsAccountModel() {
        return smsAccountModel;
    }

    public void setSmsAccountModel(SmsAccountModel smsAccountModel) {
        this.smsAccountModel = smsAccountModel;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
