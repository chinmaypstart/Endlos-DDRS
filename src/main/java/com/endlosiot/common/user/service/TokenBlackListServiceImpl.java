package com.endlosiot.common.user.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.model.TokenBlackListModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "tokenBlackListService")
public class TokenBlackListServiceImpl extends AbstractService<TokenBlackListModel>
        implements TokenBlackListService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<TokenBlackListModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<TokenBlackListModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<TokenBlackListModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(TokenBlackListModel.class);
    }

    @Override
    public Class<TokenBlackListModel> getEntityClass() {
        return TokenBlackListModel.class;
    }

    @Override
    public TokenBlackListModel findByUserAndToken(Long userId, String token) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<TokenBlackListModel> criteriaQuery = getCriteriaQuery();
        Root<TokenBlackListModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("userModel").get("id"), userId));
        predicates.add(criteriaBuilder.equal(root.get("jwtToken"), token));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<TokenBlackListModel> tokenBlackListModelList = entityManager.createQuery(criteriaQuery).getResultList();
        return tokenBlackListModelList.isEmpty() ? null : tokenBlackListModelList.get(0);
    }

    @Override
    public void hardDelete(Long userId) {
        entityManager
                .createQuery("delete from tokenBlackListModel t where t.userModel.id = :id")
                .setParameter("id", userId)
                .executeUpdate();
    }
}
