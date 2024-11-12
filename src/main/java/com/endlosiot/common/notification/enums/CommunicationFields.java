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

import com.endlosiot.common.user.enums.ModelEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * This is enum type of all email common fields.
 *
 * @author Vishwa.Shah
 * @since 10/08/2018
 */
public enum CommunicationFields implements ModelEnum {

    USER_NAME(1, "name"), RESET_PASSWORD_LINK(2, "resetpasswordlink"), EMAIL_TO(3, "email"), PASSWORD(4, "password"),
    URL(5, "url"), ATTACHMENT_PATH(6, "attachmentPath"), PATH(7, "Path"), VERIFICATION_LINK(8, "verification-link"),
    LINK_EXPIRE_TIME(9, "link-expire-time"), GUEST_NAME(10, "guestNames"), AMOUNT(11, "amount"),
    EMAIL_CC(12, "emailCC"), MOBILE(13, "mobile"), RESET_PASSWORD_CODE(14, "reset-password-code"),
    PRODUCT_OTP(15, "product-order-otp"), ORDER_ID(16, "order-id"), TOTAL_SVP(17, "total-svp"),
    VERIFICATION_TIME(18, "verification-time"), VALUE(19, "value"), HOTEL_OTP(20, "hotel-otp"),
    HOTEL_BOOKING_ID(21, "hotel-booking-id"), POD_NUMBER(22, "pod-number"),
    DELIVERY_PARTNER_NAME(23, "delivery-partner-name"), DELIVERY_PARTNER_URL(23, "delivery-partner-url"), CLIENT(24, "client");

    private final Integer id;
    private final String name;

    public static final Map<Integer, CommunicationFields> MAP = new HashMap<>();

    static {
        for (CommunicationFields communicationFields : values()) {
            MAP.put(communicationFields.getId(), communicationFields);
        }
    }

    CommunicationFields(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static CommunicationFields fromId(Integer id) {
        return MAP.get(id);
    }
}