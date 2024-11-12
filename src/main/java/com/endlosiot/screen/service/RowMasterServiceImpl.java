package com.endlosiot.screen.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.screen.model.ColumnModel;
import com.endlosiot.screen.model.RowModel;
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

@Service(value = "rowMasterService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RowMasterServiceImpl extends AbstractService<RowModel> implements RowMasterService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<RowModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isFalse(rootEntity.get(ARCHIVE)));
        predicates.add(criteriaBuilder.isTrue(rootEntity.get(ACTIVE)));
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<RowModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<RowModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(RowModel.class);
    }
    @Override
    protected Class<RowModel> getEntityClass() {
        return RowModel.class;
    }


}
