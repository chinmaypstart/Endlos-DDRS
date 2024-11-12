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
 * This is Country model which maps country list table to class.
 *
 * @author Nirav
 * @since 06/06/2018
 */

@Entity(name = "countryModel")
@Table(name = "country")
public class CountryModel extends IdentifierModel {

    private static final long serialVersionUID = 6653648434546572167L;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "sortname", nullable = false, length = 3)
    private String sortName;

    @Column(name = "phonecode", nullable = false)
    private Integer phoneCode;

    @OneToMany(mappedBy = "countryModel",fetch = FetchType.EAGER)
    private Set<StateModel> states;

    @Transient
    private static Map<Long, CountryModel> MAP = new ConcurrentHashMap<>();

    @Transient
    private static Map<Long, Set<StateModel>> COUNTRY_WISE_STATE = new ConcurrentHashMap<>();

    public CountryModel() {
        super();
    }

    public CountryModel(Long id, String sortName, String name, Integer phoneCode) {
        super(id);
        this.sortName = sortName;
        this.name = name;
        this.phoneCode = phoneCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<StateModel> getStates() {
        return states;
    }

    public void setStates(Set<StateModel> states) {
        this.states = states;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public Integer getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(Integer phoneCode) {
        this.phoneCode = phoneCode;
    }

    public static void addCountry(CountryModel countryModel) {
        MAP.put(countryModel.getId(), countryModel);
    }

    public static void removeCountry(CountryModel countryModel) {
        MAP.remove(countryModel.getId());
    }

    public static Map<Long, CountryModel> getMAP() {
        return MAP;
    }

    public static void addCountryState(Long id, Set<StateModel> stateModelList) {
        COUNTRY_WISE_STATE.put(id, stateModelList);
    }

    public static Map<Long, Set<StateModel>> getCountryStateMap() {
        return COUNTRY_WISE_STATE;
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