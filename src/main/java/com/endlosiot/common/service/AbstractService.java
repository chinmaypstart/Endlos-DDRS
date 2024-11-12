package com.endlosiot.common.service;


import com.endlosiot.common.model.Model;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.threadlocal.Auditor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is abstract service class used to provide implementation or definition of basic services
 * which are used to perform database related activities.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@MappedSuperclass
public abstract class AbstractService<M extends Model> {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * It is used to set a common Predicate for any entity. For example, Logged In
     * User ID, Activate status base, Archive status base.
     *
     * @param {@link CriteriaBuilder} criteriaBuilder
     * @param {@link Root<M>} rootEntity
     * @return {@link Predicate}
     */
    protected abstract List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<M> rootEntity);

    /**
     * It is used to set a search Predicate for any entity.This is based on given
     * search parameters.
     *
     * @param {@link CriteriaBuilder} criteriaBuilder
     * @param {@link Root<M>} rootEntity
     * @param {@link Predicate} commonPredicates
     * @return {@link Predicate}
     */
    protected abstract List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                           Root<M> rootEntity, List<Predicate> commonPredicates);

    /**
     * Return a Criteria Query for Entity class using Criteria Builder.
     *
     * @return {@link CriteriaQuery}
     */
    protected abstract CriteriaQuery<M> getCriteriaQuery();

    /**
     * It is used to get entity class.
     *
     * @return
     */
    protected abstract Class<M> getEntityClass();

    /**
     * It's used to get session binded with Hibernate Session factory. This session
     * object is used to perform database queries.
     *
     * @return
     */
    protected Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    /**
     * It returns a criteria builder.
     *
     * @return {@link CriteriaBuilder}
     */
    protected CriteriaBuilder getCriteriaBuilder() {
        return getSession().getCriteriaBuilder();
    }

    /**
     * It is used to insert a single record into database using default class.
     *
     * @param model
     */
    public void create(M model) {
        Auditor.createAudit(model);
        entityManager.persist(model);
    }

    /**
     * It is used to insert bulk record into database using default class.
     *
     * @param modelList
     */
    public void createBulk(List<M> modelList) {
        for (M model : modelList) {
            create(model);
        }
    }

    /**
     * It is used to update single record into database using default entity.
     *
     * @param model
     */
    public void update(M model) {
        Auditor.updateAudit(model);
        entityManager.merge(model);
    }

    /**
     * It is used to update bulk record into database using default class.
     *
     * @param modelList
     */
    public void updateBulk(List<M> modelList) {
        for (M model : modelList) {
            update(model);
        }
    }

    /**
     * It is used to get single record base on given id using default class.
     *
     * @param id unique value to identify model
     * @return model
     */
    public M get(long id) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<M> criteriaQuery = getCriteriaQuery();
        Root<M> root = criteriaQuery.from(getEntityClass());

        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get(BaseService.ID), id));

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    /**
     * It is used to delete single record base on given id using default class.
     *
     * @param model
     */
    public void delete(M model) {
        Auditor.archiveAudit(model);
        entityManager.merge(model);
    }

    /**
     * It is used to delete bulk record base on given model using default class.
     *
     * @param modelList
     */
    public void deleteBulk(List<M> modelList) {
        for (M model : modelList) {
            delete(model);
        }
    }

    /**
     * This method will be used to load all data from table. This will be very
     * useful to load small amount of data from table into map.
     *
     * @return
     */
    public List<M> findAll() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<M> criteriaQuery = getCriteriaQuery();
        Root<M> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * This is used to search model data on given criteria using default entity.
     *
     * @param searchObject
     * @param start        starting row from where to fetch record
     * @param recordSize   end row of record
     * @return {@link PageModel}
     */
    public PageModel search(Object searchObject, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<M> criteriaQuery = getCriteriaQuery();
        Root<M> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        List<Predicate> searchPredicates = getSearchPredicates(searchObject, criteriaBuilder, root, predicates);

        List<M> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(searchObject);
        return PageModel.create(results, records);
    }

    protected List<M> getResults(CriteriaBuilder criteriaBuilder, CriteriaQuery<M> criteriaQuery, Root<M> root,
                                 List<Predicate> searchPredicates, Integer startNo, Integer recordSize) {
        criteriaQuery.select(root).where(searchPredicates.toArray(new Predicate[]{}));
        setOrder(criteriaBuilder, criteriaQuery, root);
        List<M> results;
        if (startNo != null && recordSize != null) {
            results = entityManager.createQuery(criteriaQuery).setFirstResult(startNo).setMaxResults(recordSize)
                    .getResultList();
        } else {
            results = entityManager.createQuery(criteriaQuery).getResultList();
        }
        return results;
    }

    protected void setOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery<M> criteriaQuery, Root<M> root) {
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(BaseService.ID)));
    }

    protected long getCount(Object searchObject) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<M> root = criteriaQuery.from(getEntityClass());
        criteriaQuery.select(criteriaBuilder.count(root)).where(
                getSearchPredicates(searchObject, criteriaBuilder, root, getCommonPredicates(criteriaBuilder, root))
                        .toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return 0;
        }
    }
}
