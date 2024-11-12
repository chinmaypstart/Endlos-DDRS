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
package com.endlosiot.common.model;

import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * This is PageModel which contains list of models.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
@Data
public class PageModel implements Model {

    @Serial
    private static final long serialVersionUID = 9014141070685701820L;

    private List<? extends Model> list;
    private long records;

    public PageModel(List<? extends Model> list, long records) {
        this.list = list;
        this.records = records;
    }

    public static PageModel create(List<? extends Model> list, long records) {
        return new PageModel(list, records);
    }
}
