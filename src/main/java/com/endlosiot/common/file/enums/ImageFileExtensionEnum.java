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
public enum ImageFileExtensionEnum {

    jpeg("jpeg"), png("png"), jpg("jpg"), gif("gif"), mp4("mp4"), svg("svg"), jfif("jfif"), heif("heif"), heic("heic"),XLS("xls"), XLSX("xlsx");

    private final String name;

    ImageFileExtensionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ImageFileExtensionEnum fromId(String name) {
        for (ImageFileExtensionEnum imageFileExtensionEnum : values()) {
            if (imageFileExtensionEnum.getName().equalsIgnoreCase(name)) {
                return imageFileExtensionEnum;
            }
        }
        return null;
    }

    public static String getSupportedFormat() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ImageFileExtensionEnum imageFileExtensionEnum : values()) {
            stringBuilder.append(imageFileExtensionEnum.getName().toUpperCase()).append(", ");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
