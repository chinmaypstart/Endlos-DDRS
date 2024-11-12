package com.endlosiot.device.service;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.device.view.DeviceParameterView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chetanporwal
 * @since 06/09/2023
 */
@Service(value = "deviceParameterService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceParameterServiceImpl extends AbstractService<DeviceParameterModel> implements DeviceParameterService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<DeviceParameterModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<DeviceParameterModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<DeviceParameterModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(DeviceParameterModel.class);
    }

    @Override
    protected Class<DeviceParameterModel> getEntityClass() {
        return DeviceParameterModel.class;
    }

    @Override
    public List<DeviceParameterModel> getByDeviceModel(Long deviceId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceParameterModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceParameterModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), (deviceId)));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<DeviceParameterModel> getByDeviceModelDeviceId(String deviceId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceParameterModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceParameterModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), (deviceId)));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void hardDelete(List<DeviceParameterModel> deviceParameterModels) throws EndlosiotAPIException {
        try {
            for (DeviceParameterModel deviceParameterModel : deviceParameterModels) {
                CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
                CriteriaDelete<DeviceParameterModel> query = criteriaBuilder.
                        createCriteriaDelete(DeviceParameterModel.class);
                Root<DeviceParameterModel> root = query.from(DeviceParameterModel.class);
                query.where(root.get("id").in(deviceParameterModel.getId()));
                entityManager.createQuery(query).executeUpdate();
            }
        } catch (PersistenceException e) {
            throw new EndlosiotAPIException(ResponseCode.CANT_DELETE_PARAMETER.getCode(), ResponseCode.CANT_DELETE_PARAMETER.getMessage());
        }
    }

    @Override
    public DeviceParameterModel getByDeviceAndParameter(Long deviceId, Long parameterId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceParameterModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceParameterModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceModel").get("id"), deviceId));
        predicates.add(criteriaBuilder.equal(root.get("parameterMasterModel").get("id"), parameterId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<DeviceParameterModel> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }
    }

    @Override
    public List<DeviceParameterModel> getByDeviceParameter(List<DeviceParameterView> deviceParameterViewList) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceParameterModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceParameterModel> root = criteriaQuery.from(getEntityClass());

        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);

        // Extract the list of IDs from deviceParameterViewList
        List<Long> ids = deviceParameterViewList.stream()
                .map(DeviceParameterView::getId)
                .collect(Collectors.toList());

        // Add a predicate to check if the ID is in the list of IDs
        if (!ids.isEmpty()) {
            predicates.add(root.get(BaseService.ID).in(ids));
        }

        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<DeviceParameterModel> getRecordForAlarm() {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceParameterModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceParameterModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("needToLogAlarm"), true));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<DeviceParameterModel> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList;
        }
    }
}
