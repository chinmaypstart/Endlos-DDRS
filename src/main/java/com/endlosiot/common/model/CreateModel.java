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
package com.endlosiot.common.model;


import com.endlosiot.common.user.model.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * This is create model which maps create by references for every table.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public abstract class CreateModel extends IdentifierModel {

    @Serial
    private static final long serialVersionUID = 7484427777263634009L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkcreateby")
    private UserModel createBy;

    @Column(name = "datecreate", nullable = true)
    private Long createDate;
}
