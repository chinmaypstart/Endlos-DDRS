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
 * This is Archive view which used to send delete by user details in json
 * format.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
@JsonInclude(Include.NON_NULL)
public abstract class ArchiveView extends ActivationView {

    private static final long serialVersionUID = 5678365620021239583L;

    private Boolean archive;
    private String archiveDate;
    private String archiveBy;

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
    }

    public String getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(String archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getArchiveBy() {
        return archiveBy;
    }

    public void setArchiveBy(String archiveBy) {
        this.archiveBy = archiveBy;
    }
}
