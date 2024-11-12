package com.endlosiot.common.notification.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.SmsAccountModel;
import com.endlosiot.common.notification.view.SmsAccountView;
import com.endlosiot.common.service.BaseService;

import java.util.List;


/**
 * @author neha
 * @since 15/02/2023
 */
public interface SmsAccountService extends BaseService<SmsAccountModel> {
    /**
     * This method is used to get sms account details.
     *
     * @param id
     * @return
     */
    SmsAccountModel getLight(Long id);

    /**
     * This method is used to search sms accounts.
     *
     * @return
     */
    List<SmsAccountModel> findAllByLight();

    /**
     * This method is used to search Sms accounts based on light entity.
     *
     * @param roleView
     * @param start
     * @param recordSize
     * @return
     */
    PageModel searchByLight(SmsAccountView smsAccountView, Integer start, Integer recordSize);

    /**
     * This method is used to find model by mobile.
     *
     * @param name
     * @return
     */
    SmsAccountModel getByMobile(String mobile);

    /**
     * To delete an account.
     *
     * @param smsAccountModel
     */
    void hardDelete(SmsAccountModel smsAccountModel);
}
