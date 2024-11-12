package com.endlosiot.device.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.device.model.RawDataModel;
import com.endlosiot.device.view.RawDataView;
import jakarta.persistence.EntityManager;
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

@Service(value = "rawDataService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RawDataServiceImpl extends AbstractService<RawDataModel> implements RawDataService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<RawDataModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<RawDataModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<RawDataModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(RawDataModel.class);
    }

    @Override
    protected Class<RawDataModel> getEntityClass() {
        return RawDataModel.class;
    }

    @Override
    public PageModel getByDevice(String deviceId, int start, int recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RawDataModel> criteriaQuery = getCriteriaQuery();
        Root<RawDataModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("deviceId"), deviceId));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<Predicate> searchPredicates = getSearchPredicates(new RawDataView(), criteriaBuilder, root, predicates);
        List<RawDataModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<RawDataModel> countRoot = countQuery.from(RawDataModel.class);
        countQuery.select(criteriaBuilder.count(countRoot))
                .where(criteriaBuilder.equal(countRoot.get("deviceModel").get("deviceId"), deviceId));
        long records = entityManager.createQuery(countQuery).getSingleResult();
        return PageModel.create(results, records);
    }

    @Override
    public List<RawDataModel> getTodaysData(String deviceId, Long startEpoch, Long endEpoch) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RawDataModel> criteriaQuery = getCriteriaQuery();
        Root<RawDataModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("deviceId"), deviceId));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startEpoch));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endEpoch));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<RawDataModel> getAllByDevice(Long deviceId, Long startDate, Long endDate) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RawDataModel> criteriaQuery = getCriteriaQuery();
        Root<RawDataModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), deviceId));
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
