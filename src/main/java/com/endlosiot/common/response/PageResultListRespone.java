package com.endlosiot.common.response;

import com.endlosiot.common.view.View;

import java.util.List;

public class PageResultListRespone extends CommonResponse {

    private static final long serialVersionUID = -5100961406172646483L;
    private List<? extends View> list;
    private long records;

    protected PageResultListRespone(int responseCode, String message, long records, List<? extends View> list) {
        super(responseCode, message);
        this.records = records;
        this.list = list;

    }

    public static PageResultListRespone create(int responseCode, String message, long records,
                                               List<? extends View> list) {
        return new PageResultListRespone(responseCode, message, records, list);
    }

    public List<? extends View> getList() {
        return list;
    }

    public long getRecords() {
        return records;
    }

}
