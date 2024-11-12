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
 *****************************************************************************/
package com.endlosiot.common.file.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.enums.ImageFileExtensionEnum;
import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.file.operation.FileOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.util.FileUtility;
import com.endlosiot.common.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * This controller maps all file upload related apis.
 *
 * @author Dhruvang.Joshi
 * @since 07/12/2017
 */
@RestController
@RequestMapping("/public/file")
public class FilePublicController {
    @Autowired
    FileOperation fileOperation;

    @GetMapping(value = "/download-image")
    @AccessLog
    public ResponseEntity<Response> downloadImage(@RequestParam(value = "fileId") String fileId,
                                                  @RequestParam(value = "requireCompressImage", required = false) boolean requireCompressImage)
            throws EndlosiotAPIException {
        if (StringUtils.isBlank(fileId)) {
            throw new EndlosiotAPIException(ResponseCode.FILE_ID_IS_MISSING.getCode(),
                    ResponseCode.FILE_ID_IS_MISSING.getMessage());
        }
        FileModel fileModel = fileOperation.get(fileId);
        if (fileModel == null) {
            throw new EndlosiotAPIException(ResponseCode.FILE_ID_IS_INVALID.getCode(),
                    ResponseCode.FILE_ID_IS_INVALID.getMessage());
        }
        String filePath = null;
        if (requireCompressImage) {
            filePath = fileModel.getPath() + fileModel.getCompressName();
        } else {
            filePath = fileModel.getPath() + fileModel.getName();
        }
        WebUtil.setCookie(fileModel.getName(), fileModel.getUpload(), WebUtil.getCurrentResponse());
        FileUtility.download(filePath, true);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage()));
    }

    /**
     * This method is used to validate the image file.
     *
     * @param multipartFile
     * @throws EndlosiotAPIException
     */
    private void isValidImageFile(MultipartFile multipartFile) throws EndlosiotAPIException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new EndlosiotAPIException(ResponseCode.FILE_IS_MISSING.getCode(),
                    ResponseCode.FILE_IS_MISSING.getMessage());
        }
        String originalFileName = multipartFile.getOriginalFilename();
        if (!StringUtils.isBlank(originalFileName)) {
            if (!originalFileName.contains(".")) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_FILE_FORMAT.getCode(),
                        ResponseCode.INVALID_FILE_FORMAT.getMessage());
            } else {
                if (ImageFileExtensionEnum
                        .fromId(originalFileName.substring(originalFileName.lastIndexOf(".") + 1)) == null) {
                    throw new EndlosiotAPIException(ResponseCode.UPLOAD_IMAGE_ONLY.getCode(),
                            ResponseCode.UPLOAD_IMAGE_ONLY.getMessage() + " Only "
                                    + ImageFileExtensionEnum.getSupportedFormat() + " formats are supported.");
                }
            }
        }
    }
}