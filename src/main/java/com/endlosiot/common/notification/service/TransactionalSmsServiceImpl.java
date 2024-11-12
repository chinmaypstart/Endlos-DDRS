package com.endlosiot.common.notification.service;

import com.endlosiot.common.notification.enums.TransactionStatusEnum;
import com.endlosiot.common.notification.model.TransactionalSmsModel;
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
 * This is definition of Sms service which defines database operation which can
 * be performed on this table.
 *
 * @author neha
 * @since 15/02/2023
 */
@Service(value = "transactionalSmsService")
public class TransactionalSmsServiceImpl extends AbstractService<TransactionalSmsModel>
        implements TransactionalSmsService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TransactionalSmsModel> getSmsList(int limit) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TransactionalSmsModel> criteriaQuery = getCriteriaQuery();
        Root<TransactionalSmsModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatusEnum.NEW.getId()));
        predicates.add(criteriaBuilder.le(root.get("dateSend"), Instant.now().toEpochMilli()));
        setOrder(criteriaBuilder, criteriaQuery, root);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TransactionalSmsModel> results;
        if (limit != 0) {
            results = entityManager.createQuery(criteriaQuery).setMaxResults(limit)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE).getResultList();
        } else {
            results = entityManager.createQuery(criteriaQuery).setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
        }
        return results;
    }

    @Override
    public List<TransactionalSmsModel> getFailedSmsList(int limit) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TransactionalSmsModel> criteriaQuery = getCriteriaQuery();
        Root<TransactionalSmsModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatusEnum.FAILED.getId()));
        setOrder(criteriaBuilder, criteriaQuery, root);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("retryCount")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TransactionalSmsModel> results;
        if (limit != 0) {
            results = entityManager.createQuery(criteriaQuery).setMaxResults(limit).getResultList();
        } else {
            results = entityManager.createQuery(criteriaQuery).getResultList();
        }
        return results;
    }

    @Override
    public List<TransactionalSmsModel> getInprogressSmsList(int limit) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TransactionalSmsModel> criteriaQuery = getCriteriaQuery();
        Root<TransactionalSmsModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatusEnum.INPROCESS.getId()));
        predicates.add(criteriaBuilder.le(root.get("dateSend"), Instant.now().toEpochMilli()));
        setOrder(criteriaBuilder, criteriaQuery, root);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TransactionalSmsModel> results;
        if (limit != 0) {
            results = entityManager.createQuery(criteriaQuery).setMaxResults(limit).getResultList();
        } else {
            results = entityManager.createQuery(criteriaQuery).getResultList();
        }
        return results;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder,
                                                  Root<TransactionalSmsModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<TransactionalSmsModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<TransactionalSmsModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<TransactionalSmsModel> getEntityClass() {
        return TransactionalSmsModel.class;
    }
}
