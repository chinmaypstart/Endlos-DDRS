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
package com.endlosiot.common.notification.enums;

import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.model.*;
import com.endlosiot.common.notification.service.EmailContentService;
import com.endlosiot.common.notification.service.SmsContentService;
import com.endlosiot.common.notification.service.TransactionalEmailService;
import com.endlosiot.common.notification.service.TransactionalSmsService;
import com.endlosiot.common.user.enums.ModelEnum;
import com.endlosiot.common.util.DateUtility;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum specifies the list of notification event and triggers
 * email/sms/website base notification.
 *
 * @author Nirav.Shah
 * @since 23/05/2020
 */
public enum NotificationEnum implements ModelEnum {

    MASTER_ADMIN_USER_CREATE(1, "Create master admin", "emailScreenCreateMasterAdmin") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.MASTER_ADMIN_USER_CREATE);
            }
            if (notificationModel.isPush()) {

            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.MASTER_ADMIN_USER_CREATE);
            }
        }
    },
    USER_RESET_PASSWORD(2, "Reset Password", "emailScreenResetPassword") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.USER_RESET_PASSWORD);
            }
            if (notificationModel.isPush()) {

            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.USER_RESET_PASSWORD);
            }
        }
    },
    RESEND_VERIFICATION_CODE(3, "Resend Verification Code", "resendVerificationCode") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.RESEND_VERIFICATION_CODE);
            }
            if (notificationModel.isPush()) {

            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.RESEND_VERIFICATION_CODE);
            }
        }
    },
    RESEND_PASSWORD_CODE(4, "Resend Password Code", "resendPasswordCode") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.RESEND_PASSWORD_CODE);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.RESEND_PASSWORD_CODE);
            }
        }
    },
    PRODUCT_ORDER_OTP(5, "Product Order Otp", "productOrderOtp") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.PRODUCT_ORDER_OTP);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.PRODUCT_ORDER_OTP);
            }
        }
    },
    RESEND_VERIFICATION_EMAIL(6, "Resend Verification Email", "resendVerificationEmail") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.RESEND_VERIFICATION_EMAIL);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.RESEND_VERIFICATION_EMAIL);
            }
        }
    },
    CONFIRM_ORDER(7, "Confirm Order", "confirmOrder") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.CONFIRM_ORDER);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.CONFIRM_ORDER);
            }
        }
    },
    WAREHOUSE_ENTRY(8, "WareHouse Entry", "wareHouseEntry") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, WAREHOUSE_ENTRY);
            }
        }
    },
    HOTEL_OTP(9, "Hotel Otp", "hotelOtp") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, HOTEL_OTP);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, HOTEL_OTP);
            }

        }
    },
    CONFIRM_BOOKING(10, "Confirm Booking", "confirmBooking") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, CONFIRM_BOOKING);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, CONFIRM_BOOKING);
            }

        }
    },
    CANCEL_ORDER(11, "Cancel Order", "cancelOrder") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.CANCEL_ORDER);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.CANCEL_ORDER);
            }
        }
    },
    BOOKING_FAILED_EMAIL(12, "Booking Failed Email", "bookingFailedEmail") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, BOOKING_FAILED_EMAIL);
            }
        }
    },
    CANCEL_BOOKING(13, "Cancel Booking", "cancelBooking") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.CANCEL_BOOKING);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.CANCEL_BOOKING);
            }
        }
    },
    ORDER_SHIPPED(14, "Order Shipped", "orderShipped") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.ORDER_SHIPPED);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.ORDER_SHIPPED);
            }
        }
    }, USER_CREATE(15, "User Create", "userCreate") {
        @Override
        public void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields) {
            if (notificationModel.isEmail()) {
                sendEmail(dynamicFields, NotificationEnum.USER_CREATE);
            }
            if (notificationModel.isSms()) {
                sendSms(dynamicFields, NotificationEnum.USER_CREATE);
            }
        }
    };

    @Component
    public static class EmailContentServiceInjector {
        @Autowired
        private EmailContentService emailContentService;
        @Autowired
        private SmsContentService smsContentService;

        @PostConstruct
        public void postConstruct() {
            for (NotificationEnum notificationEnum : EnumSet.allOf(NotificationEnum.class)) {
                notificationEnum.setEmailContentService(emailContentService);
                notificationEnum.setSmsContentService(smsContentService);
            }
        }
    }

    @Component
    public static class TransactionEmailServiceInjector {
        @Autowired
        private TransactionalEmailService transactionalEmailService;
        @Autowired
        private TransactionalSmsService transactionalSmsService;

        @PostConstruct
        public void postConstruct() {
            for (NotificationEnum notificationEnum : EnumSet.allOf(NotificationEnum.class)) {
                notificationEnum.setTransactionEmailService(transactionalEmailService);
                notificationEnum.setTransactionSmsService(transactionalSmsService);
            }
        }
    }

    private final Integer id;
    private final String name;
    private final String screenName;
    public static EmailContentService emailContentService;
    public static TransactionalEmailService transactionalEmailService;
    public static SmsContentService smsContentService;
    public static TransactionalSmsService transactionalSmsService;

    public static final Map<Integer, NotificationEnum> MAP = new HashMap<>();

    static {
        for (NotificationEnum communicationTriggerEnum : values()) {
            MAP.put(communicationTriggerEnum.getId(), communicationTriggerEnum);
        }
    }

    NotificationEnum(Integer id, String name, String screenName) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
    }

    public void setEmailContentService(EmailContentService emailContentService) {
        NotificationEnum.emailContentService = emailContentService;
    }

    public void setTransactionEmailService(TransactionalEmailService transactionalEmailService) {
        NotificationEnum.transactionalEmailService = transactionalEmailService;
    }

    public void setSmsContentService(SmsContentService smsContentService) {
        NotificationEnum.smsContentService = smsContentService;
    }

    public void setTransactionSmsService(TransactionalSmsService transactionalSmsService) {
        NotificationEnum.transactionalSmsService = transactionalSmsService;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public static NotificationEnum fromId(Integer id) {
        return MAP.get(id);
    }

    public abstract void sendNotification(NotificationModel notificationModel, Map<String, String> dynamicFields);

    /**
     * This method is used to fetch email content and replace the content with
     * dynamic data and insert into a table.
     *
     * @param dynamicFields
     * @param notificationEnum
     */
    private static void sendEmail(Map<String, String> dynamicFields, NotificationEnum notificationEnum) {
        EmailContentModel emailContentModel = emailContentService
                .findByNotification(NotificationModel.getMAP().get(Long.valueOf(notificationEnum.getId())));
        if (emailContentModel == null) {
            LoggerService.error("Unable to find email template for " + notificationEnum.getName() + " trigger");
            return;
        }
        StrSubstitutor sub = new StrSubstitutor(dynamicFields);
        String content = sub.replace(emailContentModel.getContent());
        TransactionalEmailModel transactionalEmailModel = new TransactionalEmailModel(
                emailContentModel.getEmailAccountId(), dynamicFields.get(CommunicationFields.EMAIL_TO.getName()),
                emailContentModel.getEmailCc(), emailContentModel.getEmailBcc(), emailContentModel.getSubject(),
                content, TransactionStatusEnum.NEW.getId(), dynamicFields.get(CommunicationFields.ATTACHMENT_PATH.getName()),
                DateUtility.getCurrentEpoch());
        transactionalEmailService.create(transactionalEmailModel);
    }

    /**
     * This method is used to fetch sms content and replace the content with
     * dynamic data and insert into a table.
     *
     * @param dynamicFields
     * @param notificationEnum
     */
    private static void sendSms(Map<String, String> dynamicFields, NotificationEnum notificationEnum) {
        SmsContentModel smsContentModel = smsContentService
                .findByNotification(NotificationModel.getMAP().get(Long.valueOf(notificationEnum.getId())));
        if (smsContentModel == null) {
            LoggerService.error("Unable to find sms template for " + notificationEnum.getName() + " trigger");
            return;
        }
        StrSubstitutor sub = new StrSubstitutor(dynamicFields);
        String content = sub.replace(smsContentModel.getContent());
        smsSingleInsert(content, dynamicFields.get(CommunicationFields.MOBILE.getName()), smsContentModel,
                transactionalSmsService);
    }

    private static void smsSingleInsert(String content, String mobile, SmsContentModel smsContentModel,
                                        TransactionalSmsService transactionalSmsService) {
        TransactionalSmsModel transactionalSmsModel = new TransactionalSmsModel();
        transactionalSmsModel.setSmsTo(mobile);
        transactionalSmsModel.setStatus(TransactionStatusEnum.NEW.getId());
        transactionalSmsModel.setContent(content);
        transactionalSmsModel.setDateSend(DateUtility.getCurrentEpoch());
        transactionalSmsModel.setSmsAccountModel(smsContentModel.getSmsAccountModel());
        transactionalSmsModel.setRetryCount(Long.valueOf(0));
        transactionalSmsModel.setTemplateId(smsContentModel.getTemplateId());
        transactionalSmsService.create(transactionalSmsModel);
    }

}