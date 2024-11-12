package com.endlosiot.gateway.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.gateway.model.GatewayModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "gatewayService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class GatewayServiceImpl extends AbstractService<GatewayModel> implements GatewayService {
    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<GatewayModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(rootEntity.get("archive"),false));
        return predicates;
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<GatewayModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<GatewayModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    protected Class<GatewayModel> getEntityClass() {
        return GatewayModel.class;
    }
}
