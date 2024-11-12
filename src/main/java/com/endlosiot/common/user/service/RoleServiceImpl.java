/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.user.service;

import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.RoleModel;
import com.endlosiot.common.user.view.RoleView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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

/**
 * This class used to implement all database related operation that will be
 * performed on role table.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@Service(value = "roleService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RoleServiceImpl extends AbstractService<RoleModel> implements RoleService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<RoleModel> getEntityClass() {
        return RoleModel.class;
    }

    @Override
    public void hardDelete(Long roleId) {
        entityManager.createQuery("delete from roleModel r where r.id = :id").setParameter("id", roleId).executeUpdate();
    }

    @Override
    public RoleModel getByRoleType(RoleTypeEnum roleTypeEnum) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RoleModel> criteriaQuery = getCriteriaQuery();
        Root<RoleModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("typeId"), roleTypeEnum.getId()));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    public PageModel searchByLight(RoleView roleView, Integer start, Integer recordSize) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RoleModel> criteriaQuery = getCriteriaQuery();
        Root<RoleModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        List<Predicate> searchPredicates = getSearchPredicates(roleView, criteriaBuilder, root, predicates);
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        List<RoleModel> results = getResults(criteriaBuilder, criteriaQuery, root, searchPredicates, start, recordSize);
        long records = getCount(roleView);
        return PageModel.create(results, records);
    }

    @Override
    public List<RoleModel> getRights(List<Long> ids) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RoleModel> criteriaQuery = getCriteriaQuery();
        Root<RoleModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(root.get("id").in(ids));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<RoleModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<RoleModel> rootEntity, List<Predicate> commonPredicates) {
        //return commonPredicates;
        if (searchObject instanceof RoleView) {
            RoleView roleView = (RoleView) searchObject;
            if (!StringUtils.isEmpty(roleView.getName())) {
                commonPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(rootEntity.get("name")), "%"+ roleView.getName().toLowerCase() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<RoleModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(RoleModel.class);
    }

    @Override
    public List<RoleModel> getByName(String name) {
        /*Criteria criteria = getSession().createCriteria(LIGHT_ROLE_MODEL);
        criteria.add(Restrictions.ilike("name", name));
        return (List<RoleModel>) criteria.list();*/

        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RoleModel> criteriaQuery = getCriteriaQuery();
        Root<RoleModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("name"), name));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<RoleModel> getByNameExcludingId(String name, Long currentId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<RoleModel> criteriaQuery = getCriteriaQuery();
        Root<RoleModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        predicates.add(criteriaBuilder.notEqual(root.get("id"), currentId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[0])).orderBy(criteriaBuilder.desc(root.get("id")));
        return entityManager.createQuery(criteriaQuery).getResultList();

    }
}