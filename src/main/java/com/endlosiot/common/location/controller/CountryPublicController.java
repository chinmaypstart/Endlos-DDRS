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
package com.endlosiot.common.location.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.location.operation.CountryOperation;
import com.endlosiot.common.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * This controller maps all client related apis.
 *
 * @author Jaydip
 * @since 23/04/2019
 */
@Controller
@RequestMapping("/public/country")
public class CountryPublicController {

    @Autowired
    private CountryOperation countryOperation;

    /**
     * Prepare a country dropdown.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> all() throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(countryOperation.doGetAll());
    }

    /**
     * Prepare a country code list.
     *
     * @return
     * @throws EndlosiotAPIException
     */
    @GetMapping(value = "/code")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> code() throws EndlosiotAPIException {
        return ResponseEntity.status(HttpStatus.OK).body(countryOperation.doGetCode());
    }
}