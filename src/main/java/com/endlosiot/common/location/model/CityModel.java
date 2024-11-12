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
package com.endlosiot.common.location.model;

import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is City model which maps city list table to class.
 *
 * @author Nirav
 * @since 06/06/2018
 */

@Entity(name = "cityModel")
@Table(name = "city")
@EqualsAndHashCode(callSuper = true)
@Data
public class CityModel extends IdentifierModel {
    @Serial
    private static final long serialVersionUID = 6653648434546572167L;

    @Column(name = "name", nullable = false, length = 260)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkstateid")
    private StateModel stateModel;

    private static Map<Long, CityModel> MAP = new ConcurrentHashMap<>();
    public static void addCity(CityModel cityModel) {
        MAP.put(cityModel.getId(), cityModel);
    }

    public static void removeCity(CityModel cityModel) {
        MAP.remove(cityModel.getId());
    }

    public static Map<Long, CityModel> getMAP() {
        return MAP;
    }
}