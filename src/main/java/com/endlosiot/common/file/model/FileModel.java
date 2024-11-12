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
package com.endlosiot.common.file.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.common.util.Utility;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * This is model for File Uploading, Downloading, View.
 *
 * @author Dhruvang Joshi.
 * @version 1.0
 * @since 25/11/2017
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity(name = "fileModel")
@Table(name = "file")
public class FileModel extends IdentifierModel {
    private static final long serialVersionUID = -8868918297210365891L;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "originalname", nullable = true, length = 200)
    private String originalName;

    @Column(name = "fileid", nullable = false, length = 100)
    private String fileId;

    @Column(name = "fkmoduleid", nullable = false)
    private Long moduleId;

    @Column(name = "dateupload", nullable = false)
    private Long upload;

    @Column(name = "ispublic", columnDefinition = "boolean default false")
    private boolean publicfile;

    @Column(name = "compressname", nullable = true)
    private String compressName;

    @Column(name = "path", nullable = false, length = 5000)
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public ModuleEnum getModule() {
        return ModuleEnum.fromId(moduleId.intValue());
    }

    public void setModule(Long moduleId) {
        this.moduleId = moduleId;
    }

    public Long getUpload() {
        return upload;
    }

    public void setUpload(Long upload) {
        this.upload = upload;
    }

    public boolean isPublicfile() {
        return publicfile;
    }

    public void setPublicfile(boolean publicfile) {
        this.publicfile = publicfile;
    }

    public String getCompressName() {
        return compressName;
    }

    public void setCompressName(String compressName) {
        this.compressName = compressName;
    }

    public void setFileModel(String name, ModuleEnum moduleEnum, boolean isPublic) {
        this.setFileId(Utility.generateUuid());
        this.setName(name);
        this.setModule(Long.valueOf(moduleEnum.getId()));
        this.setPublicfile(isPublic);
        this.setUpload(DateUtility.getCurrentEpoch());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FileModel other = (FileModel) obj;
        if (fileId == null) {
            if (other.fileId != null)
                return false;
        } else if (!fileId.equals(other.fileId))
            return false;
        return true;
    }
}
