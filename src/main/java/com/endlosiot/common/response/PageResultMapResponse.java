package com.endlosiot.common.response;

import lombok.Getter;

import java.io.Serial;
import java.util.List;
import java.util.Map;

/**
 * @author chetanporwal
 * @since 29/08/2023
 */
@Getter
public class PageResultMapResponse extends CommonResponse {

    @Serial
    private static final long serialVersionUID = -5100961406172646483L;
    private final List<Map<String, Object>> list;
    private final long records;

    protected PageResultMapResponse(
            int responseCode, String message, long records, List<Map<String, Object>> list) {
        super(responseCode, message);
        this.records = records;
        this.list = list;
    }

    public static PageResultMapResponse create(
            int responseCode, String message, long records, List<Map<String, Object>> list) {
        return new PageResultMapResponse(responseCode, message, records, list);
    }

}
