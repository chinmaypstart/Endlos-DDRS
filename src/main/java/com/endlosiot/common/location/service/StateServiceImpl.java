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
package com.endlosiot.common.location.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.location.model.StateModel;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


/**
 * This class used to implement all database related operation that will be
 * performed on state table.
 *
 * @author Nirav
 * @since 10/11/2017
 */
@Repository
public class StateServiceImpl extends AbstractService<StateModel> implements StateService, CustomInitializationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<StateModel> getEntityClass() {
        return StateModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<StateModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<StateModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<StateModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(StateModel.class);
    }


    @Override
    public void onStartUp() throws EndlosiotAPIException {
        List<StateModel> stateModelList = findAll();
        for (StateModel stateModel : stateModelList) {
            StateModel.addState(stateModel);
            StateModel.addStateCity(stateModel.getId(), stateModel.getCities());
        }
    }

    @Override
    public void hardDelete(Long stateId) {
        entityManager.createQuery("delete from stateModel s where s.id = :id").setParameter("id", stateId)
                .executeUpdate();
    }

    @Override
    public StateModel getByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<StateModel> criteriaQuery = getCriteriaQuery();
        Root<StateModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<StateModel> stateModels = entityManager.createQuery(criteriaQuery).getResultList();
        return stateModels == null || stateModels.isEmpty() ? null : stateModels.get(0);
    }

    @Override
    public StateModel load(Long key) {
        return null;
    }

}