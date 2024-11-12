/*******************************************************************************
 | * Copyright -2019 @intentlabs
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
package com.endlosiot.common.setting.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.setting.model.SystemSettingModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
 * performed on system setting table.
 *
 * @author Nirav
 * @since 20/12/2019
 */
@Service(value = "systemSettingService")
public class SystemSettingServiceImpl extends AbstractService<SystemSettingModel>
        implements SystemSettingService, CustomInitializationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<SystemSettingModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<SystemSettingModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<SystemSettingModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    public Class<SystemSettingModel> getEntityClass() {
        return SystemSettingModel.class;
    }


    @Override
    public void onStartUp() throws EndlosiotAPIException {
        List<SystemSettingModel> systemSettingModels = findAll();
        for (SystemSettingModel systemSettingModel : systemSettingModels) {
            SystemSettingModel.add(systemSettingModel);
        }
    }

    @Override
    public List<SystemSettingModel> findAll() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<SystemSettingModel> criteriaQuery = getCriteriaQuery();
        Root<SystemSettingModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public SystemSettingModel get(String key) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<SystemSettingModel> criteriaQuery = getCriteriaQuery();
        Root<SystemSettingModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("key"), key));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}