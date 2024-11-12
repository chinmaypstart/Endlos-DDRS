package com.endlosiot.devicediagnosis.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.util.DateUtility;
import com.endlosiot.device.view.GraphDataView;
import com.endlosiot.devicediagnosis.model.AlarmHistoryModel;
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

import java.util.ArrayList;
import java.util.List;

@Service(value = "alarmHistoryService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AlarmHistoryServiceImpl extends AbstractService<AlarmHistoryModel> implements AlarmHistoryService {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<AlarmHistoryModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<AlarmHistoryModel> rootEntity, List<Predicate> commonPredicates) {
        DeviceDiagnosisView deviceDiagnosisView = (DeviceDiagnosisView) searchObject;
        /*if (deviceDiagnosisView.getType() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("type"), deviceDiagnosisView.getType()));
        }*/
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<AlarmHistoryModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<AlarmHistoryModel> getEntityClass() {
        return AlarmHistoryModel.class;
    }

    private List<Predicate> buildPredicates(CriteriaBuilder criteriaBuilder, Root<AlarmHistoryModel> root, String deviceId, Long startDate, Long endDate) {
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
    public AlarmHistoryModel getAlarmByParameter(Long deviceParameterId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AlarmHistoryModel> criteriaQuery = criteriaBuilder.createQuery(AlarmHistoryModel.class);
        Root<AlarmHistoryModel> root = criteriaQuery.from(AlarmHistoryModel.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("deviceParameterModel").get("id"), deviceParameterId));
        predicates.add(criteriaBuilder.isNull(root.get("resolvedate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        AlarmHistoryModel result;

        try {
            result = entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            result = null;
        }
        return result;
    }
    @Override
    public PageModel getByDevice(String deviceId, Long startDate, Long endDate, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();

        // Main query for fetching results
        CriteriaQuery<AlarmHistoryModel> criteriaQuery = criteriaBuilder.createQuery(AlarmHistoryModel.class);
        Root<AlarmHistoryModel> root = criteriaQuery.from(AlarmHistoryModel.class);

        // Build predicates for main query
        List<Predicate> predicates = buildPredicates(criteriaBuilder, root, deviceId, startDate, endDate);

        // Apply predicates and ordering
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdate")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        // Fetch paginated results
        List<AlarmHistoryModel> results = getResults(criteriaBuilder, criteriaQuery, root, predicates, start, recordSize);

        // Count query
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<AlarmHistoryModel> countRoot = countQuery.from(AlarmHistoryModel.class);

        // Apply same predicates for count query
        List<Predicate> countPredicates = buildPredicates(criteriaBuilder, countRoot, deviceId, startDate, endDate);
        countQuery.select(criteriaBuilder.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        long records = entityManager.createQuery(countQuery).getSingleResult();

        // Return the paginated result
        return PageModel.create(results, records);
    }
}
