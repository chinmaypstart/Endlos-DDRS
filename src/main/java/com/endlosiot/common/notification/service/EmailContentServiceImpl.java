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

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.EmailContentModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.view.EmailContentView;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * This is definition of Email Content service which defines database operation
 * which can be performed on this table.
 *
 * @author Nirav.Shah
 * @since 12/08/2017
 */
@Service(value = "emailContentService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EmailContentServiceImpl extends AbstractService<EmailContentModel> implements EmailContentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<EmailContentModel> getEntityClass() {
        return EmailContentModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<EmailContentModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<EmailContentModel> rootEntity, List<Predicate> commonPredicates) {
        if (searchObject instanceof EmailContentView) {
            EmailContentView emailContentView = (EmailContentView) searchObject;
            if (emailContentView.getNotificationView() != null
                    && emailContentView.getNotificationView().getKey() != null) {
                commonPredicates.add(criteriaBuilder.equal(rootEntity.join("notificationModel").get("id"),
                        emailContentView.getNotificationView().getKey()));
            }
            if (emailContentView.getSubject() != null) {
                commonPredicates
                        .add(criteriaBuilder.like(rootEntity.get("subject"), emailContentView.getSubject() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    public PageModel searchLight(EmailContentView emailContentView, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<EmailContentModel> criteriaQuery = getCriteriaQuery();
        Root<EmailContentModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> searchPredicates = getSearchPredicates(emailContentView, criteriaBuilder, root, predicates);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<EmailContentModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(emailContentView);
        return PageModel.create(results, records);
    }

    @Override
    public void hardDelete(Long emailContentId) {
        entityManager.createQuery("delete from emailContentModel e where e.id = :id").setParameter("id", emailContentId)
                .executeUpdate();
    }

    @Override
    public EmailContentModel findByNotification(NotificationModel notificationModel) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<EmailContentModel> criteriaQuery = getCriteriaQuery();
        Root<EmailContentModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.join("notificationModel").get("id"), notificationModel.getId()));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected CriteriaQuery<EmailContentModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(EmailContentModel.class);
    }
}