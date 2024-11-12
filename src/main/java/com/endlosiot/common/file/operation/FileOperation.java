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
package com.endlosiot.common.file.operation;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.file.service.FileService;
import com.endlosiot.common.file.view.FileView;
import com.endlosiot.common.operation.Operation;
import com.endlosiot.common.response.Response;

import java.util.List;

/**
 * @author Dhruvang Joshi.
 * @version 1.0
 * @since 25/11/2017
 */
public interface FileOperation extends Operation {

    /**
     * This method is used to save file name with its details.
     *
     * @param fileName
     * @param originalName
     * @param moduleId
     * @param isPublic
     * @param path
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSave(String fileName, String originalName, String compressImage, Integer moduleId, boolean isPublic,
                    String path) throws EndlosiotAPIException;

    /**
     * This method is used to prepare model from view.
     *
     * @param fileModel
     * @param fileName
     * @param originalName
     * @param moduleId
     * @param isPublic
     * @param compressName
     * @return
     */
    FileModel toModel(FileModel fileModel, String originalName, String fileName, Integer moduleId, boolean isPublic,
                      String compressName, String path) throws EndlosiotAPIException;

    /**
     * This method is used to prepare model from view.
     *
     * @param fileView
     * @return
     */
    FileModel getModel(FileView fileView);

    /**
     * This method used when require new model for view
     *
     * @return model
     */
    FileModel getNewModel();

    /**
     * This method used when need to convert model to view
     *
     * @param fileModel
     * @return view
     */
    FileView fromModel(FileModel fileModel);

    /**
     * This method convert list of model to list of view
     *
     * @param fileModels list of model
     * @return list of view
     */
    List<FileView> fromModelList(List<FileModel> fileModels);

    /**
     * This method use for get Service with respected operation
     *
     * @return FileService
     */
    FileService getService();

    /**
     * This method is used to get file details using file Id.
     *
     * @param fileId
     * @return
     * @throws EndlosiotAPIException
     */
    FileModel get(String fileId) throws EndlosiotAPIException;

    /**
     * This method is used to save image with photo module.
     *
     * @param fileName
     * @param originalName
     * @param compressImage
     * @param moduleId
     * @param isPublic
     * @param path
     * @param albumId
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSavePhoto(String fileName, String originalName, String compressImage, Integer moduleId, boolean isPublic,
                         String path, Long albumId) throws EndlosiotAPIException;
}