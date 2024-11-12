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
package com.endlosiot.common.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.List;

/**
 * This is Page result view which used to send grid response in json format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
@JsonInclude(Include.NON_NULL)
public class PageResultView extends IdentifierView {

    private static final long serialVersionUID = 1026428637764248569L;
    private List<? extends View> list;
    private long records;

    private PageResultView(long records, List<? extends View> list) {
        this.records = records;
        this.list = list;
    }

    public static PageResultView create(long records, List<? extends View> list) {
        return new PageResultView(records, list);
    }

    public static PageResultView create(int responseCode, String message, long records, List<? extends View> list) {
        return new PageResultView(records, list);
    }

    public List<? extends View> getList() {
        return list;
    }

    public long getRecords() {
        return records;
    }
}
