package com.endlosiot.common.operation;


import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.View;

/**
 * This is a base operation which is used to declare common business
 * operations(logic) which can be performed on every entity. Operation bridges
 * the gap between controller & model.
 *
 * @author Nirav.Shah
 * @since 25/12/2019
 */
public interface BaseOperation<V extends View> extends Operation {

    /**
     * This method is used to save entity
     *
     * @param view
     * @return
     * @throws EndlosiotAPIException
     */
    Response doSave(V view) throws EndlosiotAPIException;

    /**
     * This method is used to view entity.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    Response doView(Long id) throws EndlosiotAPIException;

    /**
     * This method is used to update entity
     *
     * @param view
     * @return
     * @throws EndlosiotAPIException
     */
    Response doUpdate(V view) throws EndlosiotAPIException;

    /**
     * This method is used delete existing data.
     *
     * @param id
     * @return
     */
    Response doDelete(Long id) throws EndlosiotAPIException;

    /**
     * This method is used active/inactive entity.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    Response doActiveInActive(Long id) throws EndlosiotAPIException;

    /**
     * This method is used to display data in tabluar format.
     *
     * @param object
     * @param start
     * @param recordSize
     * @return
     */
    Response doSearch(V view, Integer start, Integer recordSize) throws EndlosiotAPIException;

}
