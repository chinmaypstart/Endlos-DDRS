package com.endlosiot.common.notification.operation;

import com.endlosiot.common.notification.view.SmsAccountView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;

/**
 * @author neha
 * @since 15/02/2023
 */
public interface SmsAccountOperation extends BaseOperation<SmsAccountView> {
    /**
     * This method is used to prepare a key value view of sms account.
     *
     * @return
     */
    Response doDropdown();

    /**
     * this method is used to save data to MAP.
     *
     * @param smsAccountView
     */
    void doSaveMap(SmsAccountView smsAccountView);

    /**
     * this method is used to update data to MAP.
     *
     * @param smsAccountView
     */
    void doUpdateMap(SmsAccountView smsAccountView);
}
