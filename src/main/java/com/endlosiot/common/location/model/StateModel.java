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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is State model which maps state list table to class.
 *
 * @author Nirav
 * @since 06/06/2018
 */

@Entity(name = "stateModel")
@Table(name = "state")
public class StateModel extends IdentifierModel {
    private static final long serialVersionUID = 6653648434546572167L;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkcountryid")
    private CountryModel countryModel;

    @OneToMany(mappedBy = "stateModel", fetch = FetchType.EAGER)
    private Set<CityModel> cities;

    @Transient
    private static Map<Long, StateModel> MAP = new ConcurrentHashMap<>();

    @Transient
    private static Map<Long, Set<CityModel>> STATE_WISE_CITY = new ConcurrentHashMap<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountryModel getCountryModel() {
        return countryModel;
    }

    public void setCountryModel(CountryModel countryModel) {
        this.countryModel = countryModel;
    }

    public Set<CityModel> getCities() {
        return cities;
    }

    public void setCities(Set<CityModel> cities) {
        this.cities = cities;
    }

    public static void addState(StateModel stateModel) {
        MAP.put(stateModel.getId(), stateModel);
    }

    public static void removeState(StateModel stateModel) {
        MAP.remove(stateModel.getId());
    }

    public static Map<Long, StateModel> getMAP() {
        return MAP;
    }

    public static void addStateCity(Long id, Set<CityModel> cityModelList) {
        STATE_WISE_CITY.put(id, cityModelList);
    }

    public static Map<Long, Set<CityModel>> getStateCityMap() {
        return STATE_WISE_CITY;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
        IdentifierModel other = (IdentifierModel) obj;
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        return true;
    }
}