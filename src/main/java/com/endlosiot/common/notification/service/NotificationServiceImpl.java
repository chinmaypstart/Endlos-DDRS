/*******************************************************************************
 * Copyright -2017 @intentlabs
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
package com.endlosiot.common.notification.service;


import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.kernal.CustomInitializationBean;
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.notification.view.NotificationView;
import com.endlosiot.common.service.AbstractService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
import java.util.Map.Entry;

/**
 * This is definition of Notification service which defines database operation
 * which can be performed on this table.
 *
 * @author Nirav.Shah
 * @since 23/05/2020
 */
@Service(value = "notificationService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class NotificationServiceImpl extends AbstractService<NotificationModel>
        implements NotificationService, CustomInitializationBean {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Class<NotificationModel> getEntityClass() {
        return NotificationModel.class;
    }

    @Override
    protected List<Predicate> getCommonPredicates(CriteriaBuilder criteriaBuilder, Root<NotificationModel> rootEntity) {
        return new ArrayList<>();
    }

    @Override
    protected List<Predicate> getSearchPredicates(Object searchObject, CriteriaBuilder criteriaBuilder,
                                                  Root<NotificationModel> rootEntity, List<Predicate> commonPredicates) {
        if (searchObject instanceof NotificationView) {
            NotificationView notificationView = (NotificationView) searchObject;
            if (!StringUtils.isEmpty(notificationView.getName())) {
                commonPredicates.add(criteriaBuilder.like(rootEntity.get("name"), notificationView.getName() + "%"));
            }
        }
        return commonPredicates;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public void onStartUp() throws EndlosiotAPIException {
        for (Entry<Integer, NotificationEnum> entry : NotificationEnum.MAP.entrySet()) {
            NotificationModel notificationModel = get(entry.getKey());
            if (notificationModel == null) {
                notificationModel = new NotificationModel();
                notificationModel.setId(Long.valueOf(entry.getKey()));
                notificationModel.setName(entry.getValue().getName());
                notificationModel.setEmail(false);
                notificationModel.setPush(false);
                create(notificationModel);
            }
            NotificationModel.getMAP().put(notificationModel.getId(), notificationModel);
        }
    }

//	private NotificationModel setNewModel(Entry<Integer, NotificationEnum> entry) {
//		NotificationModel notificationModel = new NotificationModel();
//		notificationModel.setId(Long.valueOf(entry.getKey()));
//		notificationModel.setName(entry.getValue().getName());
//		notificationModel.setEmail(false);
//		notificationModel.setPush(false);
//		return notificationModel;
//	}

//	@Override
//	public NotificationModel getByMasterAdmin(Long notificationId) {
//		Criteria criteria = setCommonCriteria();
//		criteria.add(Restrictions.eq("id", notificationId));
//		criteria.add(Restrictions.isNull("schoolModel.id"));
//		return (NotificationModel) criteria.uniqueResult();
//	}

//	@Override
//	public NotificationModel getByTrigger(Integer id) {
//		Criteria criteria = setCommonCriteria();
//		criteria.add(Restrictions.eq("triggerId", id));
//		return (NotificationModel) criteria.uniqueResult();
//	}

//	@Override
//	public PageModel searchByMasterAdmin(NotificationView notificationView, Integer start, Integer recordSize) {
//		Criteria criteria = setCommonCriteria();
//		criteria.add(Restrictions.isNull("schoolModel"));
//		if (!StringUtils.isEmpty(notificationView.getName())) {
//			criteria.add(Restrictions.ilike("name", notificationView.getName(), MatchMode.START));
//		}
//		long records = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
//		criteria.setProjection(null);
//		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
//		if (start != null && recordSize != null) {
//			criteria.setFirstResult(start);
//			criteria.setMaxResults(recordSize);
//		}
//		setOrder(criteria);
//		List<NotificationModel> results = (List<NotificationModel>) criteria.list();
//		return PageModel.create(results, records);
//
//	}

    //	@Override
//	public PageModel searchBySchool(NotificationView notificationView, Integer start, Integer recordSize) {
//		Criteria criteria = setCommonCriteria();
//		if (!StringUtils.isEmpty(notificationView.getName())) {
//			criteria.add(Restrictions.ilike("name", notificationView.getName(), MatchMode.START));
//		}
//		long records = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
//		criteria.setProjection(null);
//		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
//		if (start != null && recordSize != null) {
//			criteria.setFirstResult(start);
//			criteria.setMaxResults(recordSize);
//		}
//		setOrder(criteria);
//		List<NotificationModel> results = (List<NotificationModel>) criteria.list();
//		return PageModel.create(results, records);
//	}
    @Override
    protected CriteriaQuery<NotificationModel> getCriteriaQuery() {
        return getCriteriaBuilder().createQuery(NotificationModel.class);
    }
}
