package com.endlosiot.devicediagnosis.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.model.DeviceDiagnosisModel;
import com.endlosiot.devicediagnosis.view.DeviceDiagnosisView;
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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service(value = "deviceDiagnosisService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceDiagnosisServiceImpl extends AbstractService<DeviceDiagnosisModel> implements DeviceDiagnosisService {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<DeviceDiagnosisModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<DeviceDiagnosisModel> rootEntity, List<Predicate> commonPredicates) {
        DeviceDiagnosisView deviceDiagnosisView = (DeviceDiagnosisView) searchObject;
        /*if (deviceDiagnosisView.getType() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("type"), deviceDiagnosisView.getType()));
        }*/
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<DeviceDiagnosisModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<DeviceDiagnosisModel> getEntityClass() {
        return DeviceDiagnosisModel.class;
    }

    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<DeviceDiagnosisModel> root, String deviceId, Long startDate, Long endDate) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), deviceId));

        // Date filtering
        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.between(root.get("createdate"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdate"), startDate));
        } else if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdate"), endDate));
        }

        return predicates;
    }

    @Override
    public PageModel getByDevice(String deviceId, Long startDate, Long endDate, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        // Main query for fetching results
        CriteriaQuery<DeviceDiagnosisModel> criteriaQuery = criteriaBuilder.createQuery(DeviceDiagnosisModel.class);
        Root<DeviceDiagnosisModel> root = criteriaQuery.from(DeviceDiagnosisModel.class);

        // Build predicates for main query
        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, deviceId, startDate, endDate);

        // Apply predicates and ordering
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        // Fetch paginated results
        List<DeviceDiagnosisModel> results = getResults(criteriaBuilder, criteriaQuery, root, predicates, start, recordSize);

        // Count query
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<DeviceDiagnosisModel> countRoot = countQuery.from(DeviceDiagnosisModel.class);

        // Apply same predicates for count query
        List<Predicate> countPredicates = buildPredicates(criteriaBuilder, countRoot, deviceId, startDate, endDate);
        countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        long records = entityManager.createQuery(countQuery).getSingleResult();

        // Return the paginated result
        return PageModel.create(results, records);
    }

    @Override
    public DeviceDiagnosisModel getLatestDiagnosisDataByDeviceId(String deviceId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeviceDiagnosisModel> criteriaQuery = criteriaBuilder.createQuery(DeviceDiagnosisModel.class);
        Root<DeviceDiagnosisModel> root = criteriaQuery.from(DeviceDiagnosisModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), deviceId));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        DeviceDiagnosisModel result;

        try {

            result = entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult();

        } catch (NoResultException e) {
            result = null;
        }

        return result;
    }

    @Override
    public PageModel getDignosisByDevice(GraphDataView graphDataView) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceDiagnosisModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceDiagnosisModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), graphDataView.getDeviceView().getId()));

        // Date range predicate
        if (graphDataView.getStartDate() != null && graphDataView.getEndDate() != null) {
            predicates.add(criteriaBuilder.between(root.get("createdate"), graphDataView.getStartDate(), graphDataView.getEndDate()));
        } else if (graphDataView.getStartDate() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdate"), graphDataView.getStartDate()));
        } else if (graphDataView.getEndDate() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdate"), graphDataView.getEndDate()));
        }

        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        List<DeviceDiagnosisModel> results = entityManager.createQuery(criteriaQuery).getResultList();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<DeviceDiagnosisModel> countRoot = countQuery.from(DeviceDiagnosisModel.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(criteriaBuilder.equal(countRoot.get("deviceModel").get("id"), graphDataView.getDeviceView().getId()));
        long records = entityManager.createQuery(countQuery).getSingleResult();
        return PageModel.create(results, records);
    }

    @Override
    public List<DeviceDiagnosisModel> getByDeviceByStartDate(Long deviceId) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeviceDiagnosisModel> criteriaQuery = criteriaBuilder.createQuery(DeviceDiagnosisModel.class);
        Root<DeviceDiagnosisModel> root = criteriaQuery.from(DeviceDiagnosisModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), deviceId));
        predicates.add(criteriaBuilder.equal(root.get("alarmCalculated"),false));
        predicates.add(criteriaBuilder.ge(root.get("createdate"), DateUtility.getTodayStartEpoch()));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        List<DeviceDiagnosisModel> result;

        try {
            result = entityManager.createQuery(criteriaQuery).getResultList();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }
}
