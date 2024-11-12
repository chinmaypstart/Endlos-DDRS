package com.endlosiot.screen.service;

import com.endlosiot.common.service.AbstractService;
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

@Service(value = "columnMasterService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ColumnMasterServiceImpl extends AbstractService<ColumnModel> implements ColumnMasterService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<ColumnModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isFalse(rootEntity.get(ARCHIVE)));
        predicates.add(criteriaBuilder.isTrue(rootEntity.get(ACTIVE)));
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<ColumnModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<ColumnModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(ColumnModel.class);
    }
    @Override
    protected Class<ColumnModel> getEntityClass() {
        return ColumnModel.class;
    }

    @Override
    public ColumnModel getByScreenIdAndColumnName(Long screenId, String column) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ColumnModel> criteriaQuery = criteriaBuilder.createQuery(ColumnModel.class);
        Root<ColumnModel> root = criteriaQuery.from(ColumnModel.class);

        // Constructing list of predicates
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("screenModel").get("id"), screenId));
        predicates.add(criteriaBuilder.equal(root.get("columnName"), column));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

        // Applying predicates to the query
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

        // Executing the query
        List<ColumnModel> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList.isEmpty() ? null : resultList.get(0);
    }


}
