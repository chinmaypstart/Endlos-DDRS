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
 * @author Vishwa.Shah
 * @since 21/01/2020
 */
public enum DocumentExtensionEnum {

    pdf("pdf"), docx("docx"), doc("doc");

    private final String name;

    DocumentExtensionEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static DocumentExtensionEnum fromId(String name) {
        for (DocumentExtensionEnum documentExtensionEnum : values()) {
            if (documentExtensionEnum.getName().equals(name)) {
                return documentExtensionEnum;
            }
        }
        return null;
    }
}
