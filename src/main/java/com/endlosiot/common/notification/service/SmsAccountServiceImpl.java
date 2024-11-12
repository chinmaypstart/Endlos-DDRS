package com.endlosiot.common.notification.service;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.notification.model.SmsAccountModel;
import com.endlosiot.common.notification.view.SmsAccountView;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * This is definition of sms Account service which defines database operation
 * which can be performed on this table.
 *
 * @author neha
 * @since 15/02/2023
 */
@Service(value = "smsAccountService")
@Transactional
public class SmsAccountServiceImpl extends AbstractService<SmsAccountModel>
        implements SmsAccountService, CustomInitializationBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void onStartUp() throws EndlosiotAPIException {
        for (SmsAccountModel smsAccountModel : findAllByLight()) {
            SmsAccountModel.getMap().put(smsAccountModel.getId(), smsAccountModel);
        }
    }

    @Override
    public SmsAccountModel getLight(Long id) {
        return get(id);
    }

    @Override
    public List<SmsAccountModel> findAllByLight() {
        return findAll();
    }

    @Override
    public PageModel searchByLight(SmsAccountView smsAccountView, Integer start, Integer recordSize) {
        return search(smsAccountView, start, recordSize);
    }

    @Override
    public SmsAccountModel getByMobile(String mobile) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<SmsAccountModel> criteriaQuery = getCriteriaQuery();
        Root<SmsAccountModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("mobile"), mobile));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public void hardDelete(SmsAccountModel smsAccountModel) {
        getSession().delete(smsAccountModel);
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<SmsAccountModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<SmsAccountModel> rootEntity, List<Predicate> commonPredicates) {
        SmsAccountView smsAccountView = (SmsAccountView) searchObject;
        if (!StringUtils.isBlank(smsAccountView.getMobile())) {
            commonPredicates
                    .add(criteriaBuilder.equal(rootEntity.get("mobile"), Long.valueOf(smsAccountView.getMobile())));
        }
        return commonPredicates;
    }

    @Override
    protected Class<SmsAccountModel> getEntityClass() {
        return SmsAccountModel.class;
    }

    @Override
    protected CriteriaQuery<SmsAccountModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }
}
