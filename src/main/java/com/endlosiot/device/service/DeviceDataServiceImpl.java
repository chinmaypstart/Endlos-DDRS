package com.endlosiot.device.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.device.model.DeviceDataModel;
import com.endlosiot.device.view.DeviceDataView;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "deviceDataService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceDataServiceImpl extends AbstractService<DeviceDataModel> implements DeviceDataService {
    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<DeviceDataModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<DeviceDataModel> rootEntity, List<Predicate> commonPredicates) {
        DeviceDataView deviceDataView = (DeviceDataView) searchObject;
        if (deviceDataView.getType() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("type"), deviceDataView.getType()));
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<DeviceDataModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<DeviceDataModel> getEntityClass() {
        return DeviceDataModel.class;
    }
}
