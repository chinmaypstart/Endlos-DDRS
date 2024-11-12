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
import com.endlosiot.common.location.model.CountryModel;
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
 * performed on country table.
 *
 * @author Nirav
 * @since 10/11/2017
 */
@Repository
public class CountryServiceImpl extends AbstractService<CountryModel>
        implements CountryService, CustomInitializationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<CountryModel> getEntityClass() {
        return CountryModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<CountryModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<CountryModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<CountryModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(CountryModel.class);
    }

    @Override
    public void onStartUp() throws EndlosiotAPIException {
        List<CountryModel> countryModelList = findAll();
        for (CountryModel countryModel : countryModelList) {
            CountryModel.addCountry(new CountryModel(countryModel.getId(), countryModel.getSortName(),
                    countryModel.getName(), countryModel.getPhoneCode()));
            CountryModel.addCountryState(countryModel.getId(), countryModel.getStates());
        }
    }

    @Override
    public void hardDelete(Long countryId) {
        entityManager.createQuery("delete from countryModel c where c.id = :id").setParameter("id", countryId)
                .executeUpdate();
    }

    @Override
    public CountryModel getByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CountryModel> criteriaQuery = getCriteriaQuery();
        Root<CountryModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<CountryModel> countryModels = entityManager.createQuery(criteriaQuery).getResultList();
        return countryModels == null || countryModels.isEmpty() ? null : countryModels.get(0);
    }

    @Override
    public CountryModel load(Long key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CountryModel> criteriaQuery = getCriteriaQuery();
        Root<CountryModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("id"), key));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<CountryModel> countryModels = entityManager.createQuery(criteriaQuery).getResultList();
        return countryModels == null || countryModels.isEmpty() ? null : countryModels.get(0);
    }
}