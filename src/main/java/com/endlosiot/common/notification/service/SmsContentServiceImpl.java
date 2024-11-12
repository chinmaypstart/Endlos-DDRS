package com.endlosiot.common.notification.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.model.SmsContentModel;
import com.endlosiot.common.notification.view.SmsContentView;
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
 * This is definition of sms Content service which defines database operation
 * which can be performed on this table.
 *
 * @author neha
 * @since 15/02/2023
 */
@Service(value = "smsContentService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SmsContentServiceImpl extends AbstractService<SmsContentModel> implements SmsContentService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PageModel searchLight(SmsContentView smsContentView, Integer start, Integer recordSize) {
        return search(smsContentView, start, recordSize);
    }

    @Override
    public void hardDelete(SmsContentModel smsContentModel) {
        getSession().delete(smsContentModel);
    }

    @Override
    public SmsContentModel findByNotification(NotificationModel notificationModel) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<SmsContentModel> criteriaQuery = getCriteriaQuery();
        Root<SmsContentModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.join("notificationModel").get("id"), notificationModel.getId()));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<SmsContentModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<SmsContentModel> rootEntity, List<Predicate> commonPredicates) {
        SmsContentView smsContentView = (SmsContentView) searchObject;
        if (smsContentView.getNotificationView() != null && smsContentView.getNotificationView().getKey() != 0) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.join("notificationModel").get("id"),
                    smsContentView.getNotificationView().getKey()));
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<SmsContentModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<SmsContentModel> getEntityClass() {
        return SmsContentModel.class;
    }
}
