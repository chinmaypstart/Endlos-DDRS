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

import com.endlosiot.common.service.AbstractService;
import com.endlosiot.common.user.model.UserPasswordModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * This class used to implement all database related operation that will be
 * performed on user password table.
 *
 * @author Nirav.Shah
 * @since 23/06/2018
 */
@Service(value = "userPasswordService")
@Transactional
public class UserPasswordServiceImpl extends AbstractService<UserPasswordModel> implements UserPasswordService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<UserPasswordModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder, Root<UserPasswordModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    protected CriteriaQuery<UserPasswordModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(getEntityClass());
    }

    @Override
    public Class<UserPasswordModel> getEntityClass() {
        return UserPasswordModel.class;
    }


    @Override
    public UserPasswordModel getCurrent(long userId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserPasswordModel> criteriaQuery = getCriteriaQuery();
        Root<UserPasswordModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("userModel").get("id"), userId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("create")));
        try {
            return entityManager.createQuery(criteriaQuery).setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<UserPasswordModel> getByUser(long userId, Integer count) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<UserPasswordModel> criteriaQuery = getCriteriaQuery();
        Root<UserPasswordModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("userModel").get("id"), userId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("create")));
        return entityManager.createQuery(criteriaQuery).setMaxResults(count)
                .getResultList();
    }

    @Override
    public void hardDelete(Long userId) {
        entityManager.createQuery("delete from userPasswordModel up where up.userModel.id = :id")
                .setParameter("id", userId).executeUpdate();
    }
}