package com.endlosiot.common.user.service;


import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.model.UserSessionModel;
import com.endlosiot.common.user.view.UserSessionView;

public interface UserSessionService extends BaseService<UserSessionModel> {
    /**
     * This method is used to search user session list using multiple filter
     * parameters.
     *
     * @param userSessionView
     * @param start
     * @param recordSize
     * @return user session list
     */
    PageModel searchLight(UserSessionView userSessionView, Integer start, Integer recordSize);

    /**
     * This method is used to get device count which used by particular user.
     *
     * @param userId
     * @return
     */
    Long deviceUsed(Long userId);

    /**
     * This method is used to check that any one has accessed a system through that
     * device.
     *
     * @param deviceCookie
     * @return
     */
    boolean isDeviceRegistered(String deviceCookie);

    /**
     * This method is used to validate new and current device based on cookie.
     *
     * @param deviceCookie
     * @param userId
     * @return
     */
    UserSessionModel getByDeviceCookie(String deviceCookie, Long userId);

    /**
     * This method is used to get least unused device to over right in case of max
     * allowed device count is bridged.
     *
     * @param userId
     * @return
     */
    void deleteLeastUnused(Long userId);
}