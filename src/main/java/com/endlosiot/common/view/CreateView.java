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

/**
 * This is Create view which used to send create by user details in json format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
@JsonInclude(Include.NON_NULL)
public abstract class CreateView extends IdentifierView {

    private static final long serialVersionUID = -6553054616121310042L;

    private Long createDate;
    private String createBy;
    private Long createById;
    private String thumbNailId;

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Long getCreateById() {
        return createById;
    }

    public void setCreateById(Long createById) {
        this.createById = createById;
    }

    public String getThumbNailId() {
        return thumbNailId;
    }

    public void setThumbNailId(String thumbNailId) {
        this.thumbNailId = thumbNailId;
    }
}
