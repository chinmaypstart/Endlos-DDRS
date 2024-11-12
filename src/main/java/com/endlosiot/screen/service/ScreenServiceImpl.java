package com.endlosiot.screen.service;

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.screen.model.ScreenModel;
import com.endlosiot.screen.view.ScreenView;
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

@Service(value = "screenService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScreenServiceImpl extends AbstractService<ScreenModel> implements ScreenService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<ScreenModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<ScreenModel> rootEntity, List<Predicate> commonPredicates) {
        //return commonPredicates;
        if (searchObject instanceof ScreenView) {
            ScreenView screenView = (ScreenView) searchObject;
            if (!StringUtils.isEmpty(screenView.getScreenName())) {
                commonPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootEntity.get("screenName")), "%"+ screenView.getScreenName().toLowerCase() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<ScreenModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(ScreenModel.class);
    }
    @Override
    protected Class<ScreenModel> getEntityClass() {
        return ScreenModel.class;
    }


    @Override
    public void hardDelete(Long screenId) {
        entityManager.createQuery("delete from cellModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
        entityManager.createQuery("delete from columnModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
        entityManager.createQuery("delete from rowModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
        entityManager.createQuery("delete from screenModel c where c.id = :screenId").setParameter("screenId", screenId).executeUpdate();
    }

    @Override
    public void hardDeleteRowColumnCell(Long screenId) {
        entityManager.createQuery("delete from cellModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
        entityManager.createQuery("delete from columnModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
        entityManager.createQuery("delete from rowModel c where c.screenModel.id = :screenId").setParameter("screenId", screenId).executeUpdate();
    }
}