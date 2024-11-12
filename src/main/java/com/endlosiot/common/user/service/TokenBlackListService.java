package com.endlosiot.common.user.service;

import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.user.model.TokenBlackListModel;

/**
 * @author chetanporwal
 * @since 18/09/2023
 */
public interface TokenBlackListService extends BaseService<TokenBlackListModel> {

    /**
     * This method is used to find list by user.
     *
     * @param userId @apiNote
     * @return {@return }
     */
    TokenBlackListModel findByUserAndToken(Long userId, String token);

    /**
     * This method is used to delete from session.
     *
     * @param userId @apiNote
     */
    void hardDelete(Long userId);
}
