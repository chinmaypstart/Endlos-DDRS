/*******************************************************************************
 * Copyright -2017 @intentlabs
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
package com.endlosiot.common.file.view;

import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.view.IdentifierView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * This class is used to represent file object in json/in vendor response.
 *
 * @author Nirav
 * @since 30/11/2017
 */
@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = FileView.FileViewBuilder.class)
public class FileView extends IdentifierView {
    private static final long serialVersionUID = 8692674149531174388L;
    private final Integer moduleId;
    private final String fileId;
    private final String name;
    private final String originalName;
    private final String thumbNailName;
    private final boolean publicfile;
    private final String compressName;

    public Integer getModuleId() {
        return moduleId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getName() {
        return name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getThumbNailName() {
        return thumbNailName;
    }

    public boolean isPublicfile() {
        return publicfile;
    }

    public String getCompressName() {
        return compressName;
    }

    @Override
    public String toString() {
        return "FileView [moduleId=" + moduleId + ", fileId=" + fileId + ", name=" + name + ", originalName="
                + originalName + ", thumbNailName=" + thumbNailName + ", publicfile=" + publicfile + ", compressName="
                + compressName + "]";
    }

    private FileView(FileViewBuilder fileViewBuilder) {
        this.setId(fileViewBuilder.id);
        this.moduleId = fileViewBuilder.moduleId;
        this.fileId = fileViewBuilder.fileId;
        this.name = fileViewBuilder.name;
        this.originalName = fileViewBuilder.originalName;
        this.thumbNailName = fileViewBuilder.thumbNailName;
        this.publicfile = fileViewBuilder.publicfile;
        this.compressName = fileViewBuilder.compressName;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class FileViewBuilder {
        private Long id;
        private Integer moduleId;
        private String fileId;
        private String name;
        private String originalName;
        private String thumbNailName;
        private boolean publicfile;
        private String compressName;

        public FileViewBuilder setModuleId(Integer moduleId) {
            this.moduleId = moduleId;
            return this;
        }

        public FileViewBuilder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public FileViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public FileViewBuilder setOriginalName(String originalName) {
            this.originalName = originalName;
            return this;
        }

        public FileViewBuilder setThumbNailName(String thumbNailName) {
            this.thumbNailName = thumbNailName;
            return this;
        }

        public FileViewBuilder setPublicfile(boolean publicfile) {
            this.publicfile = publicfile;
            return this;
        }

        public FileViewBuilder setCompressName(String compressName) {
            this.compressName = compressName;
            return this;
        }

        public FileViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public FileView build() {
            return new FileView(this);
        }
    }

    public static FileView setView(FileModel fileModel) {
        return new FileViewBuilder().setId(fileModel.getId()).setFileId(fileModel.getFileId())
                .setName(fileModel.getName()).setCompressName(fileModel.getCompressName())
                .setOriginalName(fileModel.getOriginalName()).build();
    }
}
