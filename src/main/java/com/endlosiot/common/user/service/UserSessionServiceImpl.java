package com.endlosiot.common.user.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.model.UserSessionModel;
import com.endlosiot.common.user.view.UserSessionView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * This class used to implement all database related operation that will be
 * performed on user session table.
 *
 * @author Nirav.Shah
 * @since 22/06/2018
 */
@Service(value = "userSessionService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserSessionServiceImpl extends AbstractService<UserSessionModel> implements UserSessionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<UserSessionModel> getEntityClass() {
        return UserSessionModel.class;
    }

    @Override
    public PageModel searchLight(UserSessionView userSessionView, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserSessionModel> criteriaQuery = getCriteriaQuery();
        Root<UserSessionModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        List<Predicate> searchPredicates = getSearchPredicates(userSessionView, criteriaBuilder, root, predicates);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<UserSessionModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(userSessionView);
        return PageModel.create(results, records);
    }

    @Override
    public Long deviceUsed(Long userId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserSessionModel> criteriaQuery = getCriteriaQuery();
        CriteriaQuery<Long> criteriaQ = criteriaBuilder.createQuery(Long.class);
        Root<UserSessionModel> root = criteriaQ.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.join("userModel").get("id"), userId));
        Expression<Long> month = criteriaBuilder.count(root.get("id"));
        criteriaQ.select(month).where(criteriaBuilder.equal(root.join("userModel").get("id"), userId));
        try {
            return entityManager.createQuery(criteriaQ).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public boolean isDeviceRegistered(String deviceCookie) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserSessionModel> criteriaQuery = getCriteriaQuery();
        CriteriaQuery<Long> criteriaQ = criteriaBuilder.createQuery(Long.class);
        Root<UserSessionModel> root = criteriaQ.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceCookie"), deviceCookie));
        Expression<Long> count = criteriaBuilder.count(root.get("id"));
        criteriaQ.select(count).where(criteriaBuilder.equal(root.get("deviceCookie"), deviceCookie));
        return entityManager.createQuery(criteriaQ).getSingleResult() != null
                && entityManager.createQuery(criteriaQ).getSingleResult() > 0;
    }

    @Override
    public UserSessionModel getByDeviceCookie(String deviceCookie, Long userId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserSessionModel> criteriaQuery = getCriteriaQuery();
        Root<UserSessionModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceCookie"), deviceCookie));
        predicates.add(criteriaBuilder.equal(root.join("userModel").get("id"), userId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void deleteLeastUnused(Long userId) {
        entityManager.createNativeQuery(
                        "delete from usersession where pkid in (select pkid from usersession where fkuserid = " + userId
                                + " order by datelastlogin limit 1)")
                .executeUpdate();
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<UserSessionModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<UserSessionModel> rootEntity, List<Predicate> commonPredicates) {
        UserSessionView userSessionView = (UserSessionView) searchObject;
        if (userSessionView.getUserView() != null && userSessionView.getUserView().getId() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.join("userModel").get("id"),
                    userSessionView.getUserView().getId()));
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<UserSessionModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(UserSessionModel.class);
    }
}