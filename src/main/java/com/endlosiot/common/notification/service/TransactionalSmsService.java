package com.endlosiot.common.notification.service;

import com.endlosiot.common.notification.model.TransactionalSmsModel;
import com.endlosiot.common.service.BaseService;

import java.util.List;

/**
 * This is declaration of Transaction Sms service which defines database
 * operation which can be performed on Transaction Sms table.
 *
 * @author neha
 * @since 15/02/2023
 */
public interface TransactionalSmsService extends BaseService<TransactionalSmsModel> {
    /**
     * This method is used to get sms records list base on given count.
     *
     * @param limit
     * @return
     */
    List<TransactionalSmsModel> getSmsList(int limit);

    /**
     * This method is used to get failed email records list base on given count.
     *
     * @param limit
     * @return
     */
    List<TransactionalSmsModel> getFailedSmsList(int limit);

    /**
     * This method is used to get inprogress sms list based on given count.
     *
     * @param limit
     * @return
     */
    List<TransactionalSmsModel> getInprogressSmsList(int limit);
}
