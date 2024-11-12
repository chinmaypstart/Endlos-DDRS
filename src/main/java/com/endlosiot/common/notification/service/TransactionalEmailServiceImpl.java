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

import com.endlosiot.common.notification.enums.TransactionStatusEnum;
import com.endlosiot.common.notification.model.TransactionalEmailModel;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * This is definition of Email service which defines database operation which
 * can be performed on this table.
 *
 * @author Nirav.Shah
 * @since 12/08/2017
 */
@Service(value = "transactionEmailService")
public class TransactionalEmailServiceImpl extends AbstractService<TransactionalEmailModel>
        implements TransactionalEmailService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<TransactionalEmailModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<TransactionalEmailModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<TransactionalEmailModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    public Class<TransactionalEmailModel> getEntityClass() {
        return TransactionalEmailModel.class;
    }


    @Override
    public List<TransactionalEmailModel> getEmailList(int limit) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TransactionalEmailModel> criteriaQuery = getCriteriaQuery();
        Root<TransactionalEmailModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatusEnum.NEW.getId()));
        predicates.add(criteriaBuilder.equal(root.get("dateSend"), Instant.now().toEpochMilli()));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(ID)));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TransactionalEmailModel> emailList = entityManager.createQuery(criteriaQuery)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).setMaxResults(limit).getResultList();
        return updateStatus(emailList, false);
    }

    @Override
    public List<TransactionalEmailModel> getFailedEmailList(int limit) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TransactionalEmailModel> criteriaQuery = getCriteriaQuery();
        Root<TransactionalEmailModel> root = criteriaQuery.from(getEntityClass());

        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatusEnum.FAILED.getId()));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get(ID)), criteriaBuilder.asc(root.get("retryCount")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TransactionalEmailModel> emailList = entityManager.createQuery(criteriaQuery).setMaxResults(limit)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        return updateStatus(emailList, true);
    }

    private List<TransactionalEmailModel> updateStatus(List<TransactionalEmailModel> emailList, boolean isRetryAttempt) {
        if (emailList != null && !emailList.isEmpty()) {
            for (TransactionalEmailModel email : emailList) {
                email.setStatus((TransactionStatusEnum.SENT.getId()));
                email.setDateSent(Instant.now().toEpochMilli());
                if (isRetryAttempt) {
                    email.setRetryCount(email.getRetryCount() + 1);
                }
                update(email);
            }
        }
        return emailList;
    }
}
