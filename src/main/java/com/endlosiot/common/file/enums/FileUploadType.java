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
package com.endlosiot.common.file.enums;

/**
 * This is enum type of which files which are allowed to be uploaded.
 *
 * @author Nirav.Shah
 * @since 06/04/2018
 */
public enum FileUploadType {

    pdf("pdf"),
    jpeg("jpeg"),
    png("png"),
    jpg("jpg"),
    html("html"),
    xls("xls"),
    xlsx("xlsx"),
    csv("csv"),
    gif("gif"),
    avi("avi"),
    wmv("wmv"),
    mp4("mp4"),
    mpg("mpg"),
    flv("flv"),
    ppt("ppt"),
    pptx("pptx"),
    json("json"),
    jfif("jfif"),
    heif("heif"),
    svg("svg"),
    heic("heic");

    private final String name;

    FileUploadType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FileUploadType fromId(String name) {
        for (FileUploadType fileUploadType : values()) {
            if (fileUploadType.getName().equals(name.toLowerCase())) {
                return fileUploadType;
            }
        }
        return null;
    }
}
