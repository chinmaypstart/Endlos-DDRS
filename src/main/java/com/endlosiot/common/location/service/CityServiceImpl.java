/*******************************************************************************
 | * Copyright -2017 @intentlabs
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
import com.endlosiot.common.location.model.CityModel;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * This class used to implement all database related operation that will be
 * performed on state table.
 *
 * @author Nirav
 * @since 10/11/2017
 */
@Service(value = "cityService")
public class CityServiceImpl extends AbstractService<CityModel> implements CityService, CustomInitializationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<CityModel> getEntityClass() {
        return CityModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<CityModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<CityModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<CityModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(CityModel.class);
    }

    @Override
    public void onStartUp() throws EndlosiotAPIException {
        List<CityModel> cityModels = findAll();
        for (CityModel cityModel : cityModels) {
            CityModel.addCity(cityModel);
        }
    }



    @Override
    public CityModel findByName(String name, Long stateId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CityModel> criteriaQuery = getCriteriaQuery();
        Root<CityModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.join("stateModel").get("id"), stateId));
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<CityModel> cityModels = entityManager.createQuery(criteriaQuery).getResultList();
        return cityModels == null || cityModels.isEmpty() ? null : cityModels.get(0);
    }

    @Override
    public CityModel load(Long key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CityModel> criteriaQuery = getCriteriaQuery();
        Root<CityModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("id"), key));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<CityModel> cityModels = entityManager.createQuery(criteriaQuery).getResultList();
        return cityModels == null || cityModels.isEmpty() ? null : cityModels.get(0);
    }
}
