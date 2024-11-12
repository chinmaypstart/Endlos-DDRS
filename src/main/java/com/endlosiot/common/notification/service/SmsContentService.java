package com.endlosiot.common.notification.service;


import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.model.SmsContentModel;
import com.endlosiot.common.notification.view.SmsContentView;
import com.endlosiot.common.service.BaseService;

/**
 * This is declaration of sms content service which defines database operation which can be
 * performed on this table.
 *
 * @author neha
 * @since 15/02/2023
 */

public interface SmsContentService extends BaseService<SmsContentModel> {
    /**
     * Used to search light sms content
     *
     * @param smsContentView
     * @param start
     * @param recordSize
     * @return
     */
    PageModel searchLight(SmsContentView smsContentView, Integer start, Integer recordSize);

    /**
     * This method is used to delete.
     *
     * @param smsContentModel
     */
    void hardDelete(SmsContentModel smsContentModel);

    /**
     * Used to find sms content by notification id.
     *
     * @param notificationModel
     * @return
     */
    SmsContentModel findByNotification(NotificationModel notificationModel);
}
