package com.endlosiot.common.user.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.user.model.UserSearchModel;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.user.view.UserView;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserServiceImpl extends AbstractService<UserModel> implements UserService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<UserModel> getEntityClass() {
        return UserModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<UserModel> rootEntity) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(rootEntity.get("archive"), false));
        predicates.add(criteriaBuilder.equal(rootEntity.get("active"), true));
        return predicates;
    }

    @Override
    public UserModel findByEmail(String email) {
        TypedQuery<UserModel> userModelTypedQuery = entityManager.createQuery("SELECT u FROM userModel u LEFT JOIN FETCH u.roleModels v LEFT JOIN FETCH v.roleModuleRightsModels " + "WHERE archive = false AND active=true AND email = :email", UserModel.class);
        userModelTypedQuery.setParameter("email", email);
        try {
            return userModelTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserModel findByMobile(String mobile) {
        TypedQuery<UserModel> userModelTypedQuery = entityManager.createQuery("SELECT u FROM userModel u LEFT JOIN FETCH u.roleModels v LEFT JOIN FETCH v.roleModuleRightsModels " + "WHERE archive = false AND active=true AND mobile = :mobile", UserModel.class);
        userModelTypedQuery.setParameter("mobile", mobile);
        try {
            return userModelTypedQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserModel findByToken(String token) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("verifyToken"), token));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserModel findByResetPasswordToken(String token) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("resetPasswordToken"), token));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void insertSearchParam(Long userId) {
        Query query = getSession().getNamedQuery("insertUserSearchParam");
        query.setParameter("id", userId);
        query.executeUpdate();
    }

    @Override
    public void updateSearchParam(Long userId) {
        Query query = getSession().getNamedQuery("updateUserSearchParam");
        query.setParameter("id", userId);
        query.executeUpdate();
    }

    @Override
//	public List<UserSearchModel> fullTextSearch(String searchParam) {
//		searchParam = searchParam.trim();
//		if (searchParam.contains(" ")) {
//			searchParam = searchParam.replaceAll(" +", " | ");
//		}
//		String querystring = "select fkuserId as userId from tblusersearch where ts_rank_cd(to_tsvector('simple',txtsearchparam) ,to_tsquery('simple','"
//				+ searchParam
//				+ ":*')) > 0 order by ts_rank_cd(to_tsvector('simple',txtsearchparam) ,to_tsquery('simple','"
//				+ searchParam + ":*')) desc";
//		Query query = getSession().createSelectionQuery(querystring).("userId", StandardBasicTypes.LONG);
//		query.setResultTransformer(Transformers.aliasToBean(UserSearchModel.class));
//		return (List<UserSearchModel>) query.getResultList();
//	}
//    searchParam = searchParam.strip();
//    if (searchParam.contains(" ")) {
//        searchParam = searchParam.replaceAll(" +", " | ");
//    }

    public List<UserSearchModel> fullTextSearch(String searchParam) {
        searchParam = searchParam.trim();
        if (searchParam.contains(" ")) {
            searchParam = searchParam.replaceAll(" +", " | ");
        }

        String querystring = "select * from usersearch where ts_rank_cd(to_tsvector('simple',searchparam) ,to_tsquery('simple','"
                + searchParam
                + ":*')) > 0 order by ts_rank_cd(to_tsvector('simple',searchparam) ,to_tsquery('simple','"
                + searchParam + ":*')) desc";
        Query query = entityManager.createNativeQuery(querystring, UserSearchModel.class);

//        query.unwrap(org.hibernate.query.Query.class).setResultTransformer(Transformers.aliasToBean(UserSearchModel.class));

        return query.getResultList();
    }

    @Override
    public UserModel nonVerifiedUser(Long id) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("id"), id));
        predicates.add(criteriaBuilder.equal(root.get("archive"), false));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }

    }

    @Override
    public UserModel findByEmailActive(String email) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("email"), email));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserModel findByCustomerEmail(String email) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("email"), email));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public UserModel findByCustomerMobile(String mobile) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(root.get("mobile"), mobile));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public PageModel searchLight(UserView userView, List<UserSearchModel> userSearchModels, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = new ArrayList<>();
        List<Predicate> searchPredicates = getSearchPredicates(userView, criteriaBuilder, root, predicates);
        if (userSearchModels != null && !userSearchModels.isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (UserSearchModel userSearchModel : userSearchModels) {
                ids.add(userSearchModel.getUserId());
            }
            searchPredicates.add(criteriaBuilder.in(root.get("id")).value(ids));
        }
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<UserModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(userView);
        return PageModel.create(results, records);
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<UserModel> rootEntity, List<Predicate> commonPredicates) {
        UserView userView = (UserView) searchObject;
        if (userView.getArchive() != null && userView.getArchive()) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("archive"), true));
        } else {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("archive"), false));
        }
        if (!StringUtils.isBlank(userView.getName())) {
            commonPredicates.add(criteriaBuilder.like(rootEntity.get("name"), userView.getName() + "%"));
        }
        if (!StringUtils.isBlank(userView.getEmail())) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("email"), userView.getEmail()));

        }
        if (userView.getMobile() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("mobile"), userView.getMobile()));
        }
        if (userView.getRoleViews() != null && !userView.getRoleViews().isEmpty()) {
            List<Long> ids = new ArrayList<>();
            for (RoleView roleView : userView.getRoleViews()) {
                ids.add(roleView.getId());
            }
            commonPredicates.add(criteriaBuilder.in(rootEntity.join("roleModel").get("id")).value(ids));
        }
        if (userView.getSearchRoleId() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.join("roleModel").get("typeId"), userView.getSearchRoleId()));
        }
        if (userView.getActive() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("active"), userView.getActive()));
        }
        if (userView.getHasLoggedIn() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.get("hasLoggedIn"), userView.getHasLoggedIn()));
        }
       /* if (userView.getClientView() != null && userView.getClientView().getId() != null) {
            commonPredicates.add(criteriaBuilder.equal(rootEntity.join("clientModel").get("id"), userView.getClientView().getId()));
        }*/
        return commonPredicates;
    }

   /* @Override
    public UserModel getClientAdmin(ClientModel clientModel) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserModel> criteriaQuery = getCriteriaQuery();
        Root<UserModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.join("clientModel").get("id"), clientModel.getId()));
        predicates.add(criteriaBuilder.equal(root.get("clientAdmin"), true));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }*/

    @Override
    protected CriteriaQuery<UserModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(UserModel.class);
    }
}