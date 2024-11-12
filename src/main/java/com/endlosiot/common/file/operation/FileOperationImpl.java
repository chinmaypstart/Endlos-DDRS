/*******************************************************************************
 * Copyright -2018 @intentlabs
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

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.file.service.FileService;
import com.endlosiot.common.file.view.FileView;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.common.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * This class used to perform all business operation on file model.
 *
 * @author Dhruvang.Joshi
 * @since 30/11/2017
 */
@Component(value = "fileOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FileOperationImpl implements FileOperation {
    @Autowired
    private FileService fileService;

    @Override
    public FileService getService() {
        return fileService;
    }

    @Override
    public Response doSave(String fileName, String originalName, String compressImage, Integer moduleId,
                           boolean isPublic, String path) throws EndlosiotAPIException {
        FileModel fileModel = toModel(getNewModel(), fileName, originalName, moduleId, isPublic, compressImage, path);
        fileService.create(fileModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(),
                fromModel(fileModel));
    }

    @Override
    public FileModel toModel(FileModel fileModel, String fileName, String originalName, Integer moduleId,
                             boolean isPublic, String compressName, String path) throws EndlosiotAPIException {
        fileModel.setFileId(Utility.generateUuid());
        fileModel.setName(fileName);
        fileModel.setOriginalName(originalName);
        fileModel.setModule(moduleId.longValue());
        fileModel.setPublicfile(isPublic);
        fileModel.setUpload(DateUtility.getCurrentEpoch());
        fileModel.setCompressName(compressName);
        fileModel.setPath(path);
        return fileModel;
    }

    @Override
    public FileModel getModel(FileView fileView) {
        return fileService.getByFileId(fileView.getFileId());
    }

    @Override
    public FileModel getNewModel() {
        return new FileModel();
    }

    @Override
    public FileView fromModel(FileModel fileModel) {
        return new FileView.FileViewBuilder().setName(fileModel.getName()).setOriginalName(fileModel.getOriginalName())
                .setCompressName(fileModel.getCompressName()).setFileId(fileModel.getFileId())
                .setPublicfile(fileModel.isPublicfile()).build();
    }

    @Override
    public List<FileView> fromModelList(List<FileModel> fileModels) {
        List<FileView> fileViews = new ArrayList<>(fileModels.size());
        for (FileModel fileModel : fileModels) {
            fileViews.add(fromModel(fileModel));
        }
        return fileViews;
    }

    @Override
    public FileModel get(String fileId) throws EndlosiotAPIException {
        FileModel fileModel = fileService.getByFileId(fileId);
        if (fileModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(),
                    ResponseCode.NO_DATA_FOUND.getMessage());
        }
        return fileModel;
    }

    @Override
    public Response doSavePhoto(String fileName, String originalName, String compressImage, Integer moduleId,
                                boolean isPublic, String path, Long albumId) throws EndlosiotAPIException {
        FileModel fileModel = toModel(getNewModel(), fileName, originalName, moduleId, isPublic, compressImage, path);
        fileService.create(fileModel);
        FileView fileView = fromModel(fileModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), fileView);
    }
}