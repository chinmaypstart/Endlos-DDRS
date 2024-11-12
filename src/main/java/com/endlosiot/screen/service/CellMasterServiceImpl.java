package com.endlosiot.screen.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.device.model.DeviceParameterModel;
import com.endlosiot.screen.model.CellModel;
import com.endlosiot.screen.model.ColumnModel;
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

@Service(value = "cellMasterService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CellMasterServiceImpl extends AbstractService<CellModel> implements CellMasterService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<CellModel> rootEntity) {
        /*List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isFalse(rootEntity.get(ARCHIVE)));
        predicates.add(criteriaBuilder.isTrue(rootEntity.get(ACTIVE)));
        return predicates;*/
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<CellModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<CellModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(CellModel.class);
    }
    @Override
    protected Class<CellModel> getEntityClass() {
        return CellModel.class;
    }


    @Override
    public List<CellModel> getByScreen(Long screenId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CellModel> criteriaQuery = getCriteriaQuery();
        Root<CellModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("screenModel").get("id"), (screenId)));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<CellModel> findByDeviceParameterId(Long id) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<CellModel> criteriaQuery = getCriteriaQuery();
        Root<CellModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("deviceParameterModel").get("id"), (id)));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }


}
