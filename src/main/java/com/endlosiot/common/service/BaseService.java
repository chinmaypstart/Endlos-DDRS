package com.endlosiot.common.service;

import com.endlosiot.common.model.Model;
import com.endlosiot.common.model.PageModel;

import java.util.List;

/**
 * It's definition of basic services used to perform database related activities. It works on given
 * model.
 *
 * @author chetanporwal
 * @since 29/08/2023
 */
public interface BaseService<M extends Model> extends Service {

    String ID = "id";
    String CREATE_BY = "createBy";
    String UPDATE_BY = "updateBy";
    String CREATE_DATE = "createDate";
    String UPDATE_DATE = "updateDate";
    String ARCHIVE = "archive";
    String ACTIVE = "active";

    /**
     * It is used to insert a single record into database using default class.
     *
     * @param model
     */
    void create(M model);

    /**
     * It is used to insert bulk record into database using default class.
     *
     * @param modelList
     */
    void createBulk(List<M> modelList);

    /**
     * It is used to update single record into database using default class.
     *
     * @param model
     */
    void update(M model);

    /**
     * It is used to update bulk record into database using default class.
     *
     * @param modelList
     */
    void updateBulk(List<M> modelList);

    /**
     * It is used to get single record base on given id using default class.
     *
     * @param id unique value to identify model
     * @return model
     */
    M get(long id);

    /**
     * It is used to delete single record base on given id using default class.
     *
     * @param model
     */
    void delete(M model);

    /**
     * It is used to delete bulk record base on given model using default class.
     *
     * @param modelList
     */
    void deleteBulk(List<M> modelList);

    /**
     * This method will be used to load all data from table. This will be very
     * useful to load small amount of data from table into map.
     *
     * @return
     */
    List<M> findAll();

    /**
     * This is used to search model data on given criteria using default class.
     *
     * @param searchObject {@link Object}
     * @param start        starting row from where to fetch record
     * @param recordSize   end row of record
     * @return {@link PageModel}
     */

    PageModel search(Object searchObject, Integer start, Integer recordSize);
}
