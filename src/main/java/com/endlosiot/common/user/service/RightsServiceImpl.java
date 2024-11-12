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
package com.endlosiot.common.user.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.model.RightsModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class used to implement all database related operation that will be
 * performed on rights table.
 *
 * @author Nirav.Shah
 * @since 26/02/2018
 */
@Service(value = "rightsService")
@DependsOn({"moduleService"})
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RightsServiceImpl extends AbstractService<RightsModel>
        implements RightsService, CustomInitializationBean {

    @Override
    public Class<RightsModel> getEntityClass() {
        return RightsModel.class;
    }

    @Override
    public void onStartUp() throws EndlosiotAPIException {
        for (Entry<Integer, RightsEnum> rightsmap : RightsEnum.MAP.entrySet()) {
            RightsModel rightsModel = get(rightsmap.getKey());
            if (rightsModel == null) {
                rightsModel = new RightsModel();
                rightsModel.setId(rightsmap.getKey().longValue());
                rightsModel.setName(rightsmap.getValue().getName());
                create(rightsModel);
            }
        }
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<RightsModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<RightsModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<RightsModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(RightsModel.class);
    }
}