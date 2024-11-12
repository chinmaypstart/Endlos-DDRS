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
package com.endlosiot.common.file.service;

import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.service.BaseService;

/**
 * @author Dhruvang.Joshi
 * @since 30/11/2017
 */
public interface FileService extends BaseService<FileModel> {

    /**
     * This method is used to get File value base on given file id.
     *
     * @param fileId
     * @return
     */
    FileModel getByFileId(String fileId);

    /**
     * This method is used to delete model from session.
     *
     * @param fileId
     */
    void hardDelete(String fileId);
}
