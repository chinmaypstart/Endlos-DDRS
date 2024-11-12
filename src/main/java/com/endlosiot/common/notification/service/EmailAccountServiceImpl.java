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
package com.endlosiot.common.notification.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.EmailAccountModel;
import com.endlosiot.common.notification.view.EmailAccountView;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This is definition of Email Account service which defines database operation
 * which can be performed on this table.
 *
 * @author Nirav.Shah
 * @since 12/08/2017
 */
@Service(value = "emailAccountService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmailAccountServiceImpl extends AbstractService<EmailAccountModel>
        implements EmailAccountService, CustomInitializationBean {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<EmailAccountModel> getEntityClass() {
        return EmailAccountModel.class;
    }

    @Override
    public void onStartUp() throws EndlosiotAPIException {
        for (EmailAccountModel emailAccountModel : findAll()) {
            EmailAccountModel.getMAP().put(emailAccountModel.getId(), emailAccountModel);
        }
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<EmailAccountModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(rootEntity.get("archive"), false));
        predicates.add(criteriaBuilder.equal(rootEntity.get("active"), true));
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<EmailAccountModel> rootEntity, List<Predicate> commonPredicates) {
        if (searchObject instanceof EmailAccountView) {
            EmailAccountView emailAccountView = (EmailAccountView) searchObject;
            if (!StringUtils.isEmpty(emailAccountView.getName())) {
                commonPredicates.add(criteriaBuilder.like(rootEntity.get("name"), emailAccountView.getName() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    public PageModel searchByLight(EmailAccountView emailAccountView, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<EmailAccountModel> criteriaQuery = getCriteriaQuery();
        Root<EmailAccountModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> searchPredicates = getSearchPredicates(emailAccountView, criteriaBuilder, root, predicates);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<EmailAccountModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(emailAccountView);
        return PageModel.create(results, records);
    }

    @Override
    public EmailAccountModel getByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<EmailAccountModel> criteriaQuery = getCriteriaQuery();
        Root<EmailAccountModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void hardDelete(Long emailAccountId) {
        entityManager.createQuery("delete from emailAccountModel e where e.id = :id").setParameter("id", emailAccountId)
                .executeUpdate();
    }

    @Override
    protected CriteriaQuery<EmailAccountModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(EmailAccountModel.class);
    }
}