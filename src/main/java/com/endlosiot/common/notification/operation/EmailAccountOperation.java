package com.endlosiot.common.notification.operation;

import com.endlosiot.common.notification.view.EmailAccountView;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;

/**
 * @author Nisha.Panchal
 * @since 17/07/2018
 */
public interface EmailAccountOperation extends BaseOperation<EmailAccountView> {
    /**
     * This method is used to prepare a key value view of email account.
     *
     * @return
     */
    Response doDropdown();
}
