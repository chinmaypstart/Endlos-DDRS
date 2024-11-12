package com.endlosiot.common.model;

import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * This is PageModel which contains list of models.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
@Data
public class PageModelString implements Model {

    @Serial
    private static final long serialVersionUID = 9014141070685701820L;

    private List<String> list;
    private long records;

    public PageModelString(List<String> list, long records) {
        this.list = list;
        this.records = records;
    }

    public static PageModelString create(List<String> list, long records) {
        return new PageModelString(list, records);
    }

}
