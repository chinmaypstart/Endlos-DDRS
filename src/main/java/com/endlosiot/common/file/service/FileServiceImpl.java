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
package com.endlosiot.common.file.service;

import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
 * This is service for storing all attachment.
 *
 * @author Dhruvang Joshi.
 * @version 1.0
 * @since 25/11/2017
 */

@Service("fileService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FileServiceImpl extends AbstractService<FileModel> implements FileService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<FileModel> getEntityClass() {
        return FileModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<FileModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<FileModel> rootEntity, List<Predicate> commonPredicates) {
        return commonPredicates;
    }

    @Override
    public void hardDelete(String fileId) {
        entityManager.createQuery("delete from fileModel f where f.fileId = :fileId").setParameter("fileId", fileId)
                .executeUpdate();
    }

    @Override
    public FileModel getByFileId(String fileId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<FileModel> criteriaQuery = getCriteriaQuery();
        Root<FileModel> root = criteriaQuery.from(getEntityClass());
        List<Predicate> predicates = getCommonPredicates(criteriaBuilder, root);
        predicates.add(criteriaBuilder.equal(root.get("fileId"), fileId));
        criteriaQuery.select(root).where(predicates.toArray(new Predicate[]{}));
        try {
            return entityManager.createQuery(criteriaQuery).setFirstResult(0).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected CriteriaQuery<FileModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(FileModel.class);
    }
}
