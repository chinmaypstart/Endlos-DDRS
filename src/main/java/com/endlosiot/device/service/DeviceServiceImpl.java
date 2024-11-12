package com.endlosiot.device.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.view.DeviceView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "deviceService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeviceServiceImpl extends AbstractService<DeviceModel> implements DeviceService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<DeviceModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isFalse(rootEntity.get(ARCHIVE)));
        predicates.add(criteriaBuilder.isTrue(rootEntity.get(ACTIVE)));
       /* if (Auditor.getAuditor().getClientModel() != null && Auditor.getAuditor().getClientModel().getId() != null) {
            predicates.add(criteriaBuilder.equal(rootEntity.get("clientModel").get(ID),
                    Auditor.getAuditor().getClientModel().getId()));
        }*/
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<DeviceModel> rootEntity, List<Predicate> commonPredicates) {
        //return commonPredicates;
        if (searchObject instanceof DeviceView) {
            DeviceView deviceView = (DeviceView) searchObject;
            if (!StringUtils.isEmpty(deviceView.getName())) {
                commonPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootEntity.get("name")), "%"+ deviceView.getName().toLowerCase() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<DeviceModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(DeviceModel.class);
    }
    @Override
    protected Class<DeviceModel> getEntityClass() {
        return DeviceModel.class;
    }
    @Override
    public List<DeviceModel> getByLocation(Long locationId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<DeviceModel> criteriaQuery = getCriteriaQuery();
        Root<DeviceModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        //predicates.add(criteriaBuilder.equal(root.get("locationModel").get("id"), (locationId)));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public void hardDelete(Long deviceId) {
        entityManager.createQuery("delete from deviceParameterModel d where d.deviceModel.id = :deviceId").setParameter("deviceId", deviceId).executeUpdate();
        entityManager.createQuery("delete from deviceModel d where d.id = :deviceId").setParameter("deviceId", deviceId).executeUpdate();
    }
}
