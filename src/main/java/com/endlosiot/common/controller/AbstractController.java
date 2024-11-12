package com.endlosiot.common.controller;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.operation.BaseOperation;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.view.View;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Its abstract class of controller. It provides abstract methods &
 * implementation of basic methods used by any controller.
 *
 * @param <V>
 * @author Nirav.Shah
 * @since 08/08/2018
 */
public abstract class AbstractController<V extends View> {
    static final String ABSTRACT_CONTROLLER = "AbstractController";

    /**
     * It is used to get operation which will be used inside controller.
     *
     * @return
     */
    public abstract BaseOperation<V> getOperation();

    /**
     * This method is used to handle save request coming from vendor for any module.
     *
     * @param view
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/save")
    public ResponseEntity<Response> save(@RequestBody V view) throws EndlosiotAPIException {
        if (view == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(view);
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doSave(view));
    }

    /**
     * This method is used to handle view request coming from vendor for any module.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/view/{id}")
    public ResponseEntity<Response> view(@PathVariable Long id) throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doView(id));
    }

    /**
     * This method is used to handle update request coming from vendor for any
     * module.
     *
     * @param view
     * @return
     * @throws EndlosiotAPIException
     */
    @PutMapping(value = "/update")
    public ResponseEntity<Response> update(@RequestBody V view) throws EndlosiotAPIException {
        if (view == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(),
                    ResponseCode.INVALID_REQUEST.getMessage());
        }
        isValidSaveData(view);
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doUpdate(view));
    }

    /**
     * This method is used to handle delete request coming from vendor for any
     * module.
     *
     * @param id
     * @return
     * @throws EndlosiotAPIException
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Response> delete(@PathVariable Long id)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doDelete(id));
    }

    /**
     * This method is used to handle active/inactive request.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @PutMapping(value = "/activation/{id}")
    public ResponseEntity<Response> activeInActive(@PathVariable Long id)
            throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(getOperation().doActiveInActive(id));
    }

    /**
     * This method is used to handle search request coming from vendor for any
     * module.
     *
     * @param request
     * @return
     * @throws EndlosiotAPIException
     */
    @PostMapping(value = "/search")
    public abstract ResponseEntity<Response> search(@RequestBody V view,
                                                    @RequestParam(name = "start") Integer start,
                                                    @RequestParam(name = "recordSize") Integer recordSize) throws EndlosiotAPIException;

    /**
     * This methods is used to validate data received from vendor side before saving
     * or updating a module.
     *
     * @param view
     * @throws EndlosiotAPIException
     */
    public abstract void isValidSaveData(V view) throws EndlosiotAPIException;
}
