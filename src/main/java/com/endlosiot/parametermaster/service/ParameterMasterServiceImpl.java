package com.endlosiot.parametermaster.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.endlosiot.parametermaster.view.ParameterMasterView;
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

/**
 * @author chetanporwal
 * @since 31/08/2023
 */
@Service(value = "parameterMasterService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ParameterMasterServiceImpl extends AbstractService<ParameterMasterModel> implements ParameterMasterService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<ParameterMasterModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        //predicates.add(criteriaBuilder.equal(rootEntity.get("archive"),false));
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<ParameterMasterModel> rootEntity, List<Predicate> commonPredicates) {
        ParameterMasterView parameterMasterView = (ParameterMasterView) searchObject;
        if (parameterMasterView.getName() != null
                && !parameterMasterView.getName().isEmpty()
                && !parameterMasterView.getName().isBlank()) {
            commonPredicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(rootEntity.get("name")), "%" + parameterMasterView.getName().toLowerCase() + "%"));
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<ParameterMasterModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(ParameterMasterModel.class);
    }
    @Override
    protected Class<ParameterMasterModel> getEntityClass() {
        return ParameterMasterModel.class;
    }

    @Override
    public ParameterMasterModel getParameterMasterByName(String name) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<ParameterMasterModel> criteriaQuery = getCriteriaQuery();
        Root<ParameterMasterModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(root.get("name").in(name));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<ParameterMasterModel> result = entityManager.createQuery(criteriaQuery).getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
