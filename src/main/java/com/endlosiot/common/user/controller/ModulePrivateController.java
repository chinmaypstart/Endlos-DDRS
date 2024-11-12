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
package com.endlosiot.common.user.controller;

import com.endlosiot.aop.AccessLog;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.view.ModuleView;
import com.endlosiot.common.user.view.RightsView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * This controller maps all Role related APIs.
 *
 * @author Nirav.Shah
 * @since 08/02/2018
 */
@RestController
@RequestMapping("/private/module")
public class ModulePrivateController {
    /**
     * This method is used to get list of modules.
     *
     * @return
     */
   /* @GetMapping(value = "/all")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> all() {
        List<ModuleView> moduleViews = new ArrayList<>();
        for (Map.Entry<Integer, ModuleEnum> moduleEnum : ModuleEnum.MAP.entrySet()) {
            List<RightsView> rightsViews = new ArrayList<>();
            for (RightsEnum rightsEnum : moduleEnum.getValue().getAssignedRights()) {
                RightsView rightsView = new RightsView.RightsViewBuilder().build();
                rightsView.setRightView(rightsEnum);
                rightsViews.add(rightsView);
            }
            ModuleView moduleView = new ModuleView.ModuleViewBuilder().setRightsViews(rightsViews).build();
            moduleView = ModuleView.setModuleView(moduleEnum.getValue());
            moduleViews.add(moduleView);
        }
        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(), moduleViews.size(), moduleViews));
    }*/
    @GetMapping(value = "/all")
    @ResponseBody
    @AccessLog
    public ResponseEntity<Response> all() {
        List<ModuleView> moduleViews = new ArrayList<>();
        for (Map.Entry<Integer, ModuleEnum> moduleEnumEntry : ModuleEnum.MAP.entrySet()) {
            ModuleEnum moduleEnum = moduleEnumEntry.getValue();

            List<RightsView> rightsViews = new ArrayList<>();
            for (RightsEnum rightsEnum : moduleEnum.getAssignedRights()) {
                RightsView rightsView = new RightsView.RightsViewBuilder()
                        .setId(rightsEnum.getId())
                        .setName(rightsEnum.getName())
                        .build();
                rightsViews.add(rightsView);
            }

            ModuleView moduleView = new ModuleView.ModuleViewBuilder()
                    .setId(moduleEnum.getId())
                    .setName(moduleEnum.getName())
                    .setRightsViews(rightsViews)
                    .build();
            moduleViews.add(moduleView);
        }

        return ResponseEntity.status(HttpStatus.OK).body(PageResultResponse.create(
                ResponseCode.SUCCESSFUL.getCode(),
                ResponseCode.SUCCESSFUL.getMessage(),
                moduleViews.size(),
                moduleViews
        ));
    }
}
