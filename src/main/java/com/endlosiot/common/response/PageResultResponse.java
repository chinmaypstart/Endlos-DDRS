/*******************************************************************************
 * Copyright -2019 @intentlabs
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
package com.endlosiot.common.response;

import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.common.view.View;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

/**
 * This is used to render grid data on screen.
 *
 * @author Core team.
 * @version 1.0
 * @since 21/09/2016
 */
@JsonInclude(Include.NON_NULL)
public class PageResultResponse extends CommonResponse {

    private static final long serialVersionUID = -1698438611739275048L;
    private List<? extends View> list;
    private List<KeyValueView> keyValueList;
    private long records;

    private PageResultResponse(int responseCode, String message, long records, List<? extends View> list) {
        super(responseCode, message);
        this.records = records;
        this.list = list;
    }

    public static PageResultResponse create(int responseCode, String message, long records, List<? extends View> list) {
        return new PageResultResponse(responseCode, message, records, list);
    }

    public List<? extends View> getList() {
        return list;
    }

    public long getRecords() {
        return records;
    }

    public List<KeyValueView> getKeyValueList() {
        return keyValueList;
    }
}
