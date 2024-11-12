/*******************************************************************************
 * Copyright -2019 @intentlabs
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.user.model;

import com.endlosiot.common.file.model.FileModel;
import com.endlosiot.common.location.model.CityModel;
import com.endlosiot.common.location.model.CountryModel;
import com.endlosiot.common.location.model.StateModel;
import com.endlosiot.common.model.ActivationModel;
import com.endlosiot.common.model.JwtTokenModel;
import com.endlosiot.screen.model.ScreenModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;


/**
 * This is User model which maps user table to class.
 *
 * @author Nirav.Shah
 * @since 24/07/2018
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "userModel")
@Table(name = "users")
public class UserModel extends ActivationModel {

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "mobile", length = 20)
    private String mobile;

    @Column(name = "verificationtoken", length = 64)
    private String verifyToken;

    @Column(name = "isverificationtokenused", columnDefinition = "boolean default false")
    private boolean verifyTokenUsed;

    @Column(name = "resetpasswordtoken", length = 64)
    private String resetPasswordToken;

    @Column(name = "isresetpasswordtokenused", columnDefinition = "boolean default false")
    private boolean resetPasswordTokenUsed;

    @Column(name = "dateresetpassword")
    private Long resetPasswordDate;

    @Column(name = "twofactortoken", length = 16)
    private String twofactorToken;

    @Column(name = "istwofactortokenused", columnDefinition = "boolean default false")
    private boolean twofactorTokenUsed;

    @Column(name = "datetwofactortoken")
    private Long twofactorDate;

    @Column(name = "temppassword", length = 64)
    private String temporaryPassword;

    @Column(name = "hasloggedin", columnDefinition = "boolean default false")
    private boolean hasLoggedIn;
    @Column(name = "verificationotp", length = 64)
    private String verifyOtp;

    @Column(name = "isverificationotpused", columnDefinition = "boolean default false")
    private boolean verifyOtpUsed;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "fkuserid"), inverseJoinColumns = @JoinColumn(name = "fkroleid"))
    private Set<RoleModel> roleModels = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "fkprofilepicid")
    private FileModel fileModel;

    @Column(name = "address", length = 5000)
    private String address;
    @Column(name = "landmark", length = 1000)
    private String landmark;
    @ManyToOne
    @JoinColumn(name = "fkcityid")
    private CityModel cityModel;
    @ManyToOne
    @JoinColumn(name = "fkstateid")
    private StateModel stateModel;
    @ManyToOne
    @JoinColumn(name = "fkcountryid")
    private CountryModel countryModel;

    @Column(name = "pincode", length = 6)
    private String pincode;
    @Column(name = "istemppassword", columnDefinition = "boolean default false")
    private boolean tempPassword;
    @Column(name = "uniquetoken", length = 100)
    private String uniqueToken;
    @Column(name = "isclientadmin", columnDefinition = "boolean default false")
    private boolean clientAdmin;

    /*@ManyToOne
    @JoinColumn(name = "fkclientid")
    private ClientModel clientModel;
    @ManyToOne
    @JoinColumn(name = "fklocationid")
    private LocationModel locationModel;*/
    
    @Column(name = "isplus", columnDefinition = "boolean default false")
    private boolean plus;

    @Column(name = "isanalog1", columnDefinition = "boolean default false")
    private boolean analog1;

    @Column(name = "isanalog2", columnDefinition = "boolean default false")
    private boolean analog2;

    @Column(name = "ismodbus", columnDefinition = "boolean default false")
    private boolean modbus;

    @Transient
    private String accessJWTToken;
    @Transient
    private JwtTokenModel jwtTokenModel;
    @Transient
    private String refreshJWTToken;
    @Transient
    private String newAccessJWTToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userscreen", joinColumns = @JoinColumn(name = "fkuserid"), inverseJoinColumns = @JoinColumn(name = "fkscreenid"))
    private Set<ScreenModel> screenModels = new HashSet<>();

    public void addRoleModel(RoleModel roleModel) {
        this.roleModels.add(roleModel);
    }

    public void addScreenModel(ScreenModel screenModel) {
        this.screenModels.add(screenModel);
    }

    public void removeRoleModel(RoleModel roleModel) {
        this.roleModels.remove(roleModel);
    }
    public void removeScreenModel(ScreenModel screenModel) {
        this.screenModels.remove(screenModel);
    }

    public boolean hasAccess(Long moduleId, Long rightsId) {
        for (RoleModel roleModel : this.getRoleModels()) {
            RoleModuleRightsKey roleModuleRightsKey = new RoleModuleRightsKey(roleModel.getId(), moduleId, rightsId);
            if (roleModel.getRoleModuleRightsModels().stream().anyMatch(o -> o.getId().equals(roleModuleRightsKey))) {
                return true;
            }
        }
        return false;
    }
}