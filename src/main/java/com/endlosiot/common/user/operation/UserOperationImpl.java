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
package com.endlosiot.common.user.operation;

import com.endlosiot.common.enums.JWTExceptionEnum;
import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.location.service.CityService;
import com.endlosiot.common.location.service.CountryService;
import com.endlosiot.common.location.service.StateService;
import com.endlosiot.common.location.view.CityView;
import com.endlosiot.common.location.view.CountryView;
import com.endlosiot.common.location.view.StateView;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.model.JwtTokenModel;
import com.endlosiot.common.model.PageModel;
import com.endlosiot.common.modelenums.CommonStatusEnum;
import com.endlosiot.common.notification.enums.CommunicationFields;
import com.endlosiot.common.notification.enums.NotificationEnum;
import com.endlosiot.common.notification.model.NotificationModel;
import com.endlosiot.common.operation.AbstractOperation;
import com.endlosiot.common.response.CommonResponse;
import com.endlosiot.common.response.PageResultResponse;
import com.endlosiot.common.response.Response;
import com.endlosiot.common.response.ViewResponse;
import com.endlosiot.common.service.BaseService;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.threadlocal.Auditor;
import com.endlosiot.common.user.enums.ModuleEnum;
import com.endlosiot.common.user.enums.RightsEnum;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.*;
import com.endlosiot.common.user.service.*;
import com.endlosiot.common.user.view.ModuleView;
import com.endlosiot.common.user.view.RightsView;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.user.view.UserView;
import com.endlosiot.common.user.view.UserView.UserViewBuilder;
import com.endlosiot.common.util.*;
import com.endlosiot.screen.model.ScreenModel;
import com.endlosiot.screen.service.ScreenService;
import com.endlosiot.screen.view.ScreenView;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * This class used to perform all business operation on user model.
 *
 * @author Hemil.Shah
 * @since 01/05/2023
 */
@Component(value = "userOperation")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserOperationImpl extends AbstractOperation<UserModel, UserView> implements UserOperation {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPasswordService userPasswordService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TokenBlackListService tokenBlackListService;

    @Autowired
    private StateService stateService;
    @Autowired
    private CityService cityService;
    /*@Autowired
    private ClientService clientService;
    @Autowired
    private LocationService locationService;*/
    @Autowired
    private CountryService countryService;

    @Autowired
    private UserSessionService userSessionService;
    @Autowired
    ScreenService screenService;

    public static Map<String, String> prepareResendResetPasswordEmail(UserModel userModel) {
        Map<String, String> dynamicFields = new TreeMap<>();
        dynamicFields.put(CommunicationFields.EMAIL_TO.getName(), userModel.getEmail());
        dynamicFields.put(CommunicationFields.MOBILE.getName(), userModel.getMobile());
        dynamicFields.put(CommunicationFields.USER_NAME.getName(), userModel.getName());
        dynamicFields.put(CommunicationFields.VERIFICATION_TIME.getName(), String.valueOf(SystemSettingModel.getOtpVerificationValidMinute()));
        dynamicFields.put(CommunicationFields.RESET_PASSWORD_CODE.getName(), userModel.getResetPasswordToken());
        return dynamicFields;
    }

    @Override
    public Response doView(Long id) throws EndlosiotAPIException {
        UserModel userModel = userService.get(id);
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        UserView userView = fromModel(userModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), userView);
    }

    @Override
    public Response doDelete(Long id) throws EndlosiotAPIException {
        UserModel userModel = userService.get(id);
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        if (!Auditor.getAuditor().equals(userModel)) {
            boolean isMasterAdmin = false;
            for (RoleModel roleModel : Auditor.getAuditor().getRoleModels()) {
                if (roleModel.getTypeId() == (RoleTypeEnum.MASTER_ADMIN.getId())) {
                    isMasterAdmin = true;
                    break;
                }
            }
            /*for (RoleModel roleModel : userModel.getRoleModels()) {
                if (roleModel.getTypeId() == (RoleTypeEnum.CLIENT_USER.getId())) {
                    throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
                }
            }*/
            if (!isMasterAdmin) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
            }
        }
        userModel.setArchive(true);
        userService.delete(userModel);
        return CommonResponse.create(ResponseCode.DELETE_SUCCESSFULLY.getCode(), ResponseCode.DELETE_SUCCESSFULLY.getMessage());

    }

    @Override
    public Response doActiveInActive(Long id) throws EndlosiotAPIException {
        UserModel userModel = userService.nonVerifiedUser(id);
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        if (!Auditor.getAuditor().equals(userModel)) {
            // checkAccess("doActivate", RightsEnum.UPDATE, ModuleEnum.USER);
        } else {
            throw new EndlosiotAPIException(ResponseCode.CAN_NOT_CHANGE_OWN_ACTIVATION_STATUS.getCode(), ResponseCode.CAN_NOT_CHANGE_OWN_ACTIVATION_STATUS.getMessage());
        }
        Auditor.activationAudit(userModel, !userModel.isActive());
        userService.update(userModel);
        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(), ResponseCode.UPDATE_SUCCESSFULLY.getMessage());

    }

    @Override
    public UserModel toModel(UserModel userModel, UserView userView) throws EndlosiotAPIException {
        userModel.setName(userView.getName());
        userModel.setEmail(userView.getEmail());
        if (userModel.getId() == null || (userModel.getId() != null && userModel.getId().equals(0L))) {
            userModel.setActive(true);
            //userModel.setArchive(false);
            userModel.setResetPasswordTokenUsed(false);
            userModel.setTwofactorTokenUsed(false);
            userModel.setVerifyTokenUsed(false);
            userModel.setVerifyToken(Utility.generateUuid());
            userModel.setVerifyOtpUsed(false);
            userModel.setVerifyOtp(Utility.generateOTP(6));
        }
        userModel.setMobile(userView.getMobile());
        userModel.setAddress(userView.getAddress());
        userModel.setLandmark(userView.getLandmark());
        userModel.setTempPassword(true);

        userModel.setTemporaryPassword(userView.getPassword());
        if (userView.getStateView() != null && userView.getStateView().getKey() != null) {
            userModel.setStateModel(stateService.get(userView.getStateView().getKey()));
        }
        if (userView.getCityView() != null && userView.getCityView().getKey() != null) {
            userModel.setCityModel(cityService.get(userView.getCityView().getKey()));
        }
        userModel.setCountryModel(countryService.get(96L));
        if (userView.getPincode() != null) {
            userModel.setPincode(userView.getPincode());
        }
        return userModel;
    }

    @Override
    public UserView fromModel(UserModel userModel) {
        UserViewBuilder builder = new UserView.UserViewBuilder().setId(userModel.getId()).setName(userModel.getName()).setEmail(userModel.getEmail()).setCreateDate(userModel.getCreateDate()).setHasLoggedIn(userModel.isHasLoggedIn()).setActive(userModel.isActive());
        if (userModel.getMobile() != null) {
            builder.setMobile(userModel.getMobile());
        }
        if (userModel.getAddress() != null) {
            builder.setAddress(userModel.getAddress());
        }
        if (userModel.getLandmark() != null) {
            builder.setLandmark(userModel.getLandmark());
        }
        if (userModel.getCityModel() != null) {
            builder.setCityView(CityView.setView(userModel.getCityModel()));
        }
        if (userModel.getStateModel() != null) {
            builder.setStateView(StateView.setView(userModel.getStateModel()));
        }
        if (userModel.getCountryModel() != null) {
            builder.setCountryView(CountryView.setView(userModel.getCountryModel()));
        }
        if (userModel.getTemporaryPassword() != null) {
            builder.setPassword(userModel.getTemporaryPassword());
        }
        /*if(userModel.getClientModel()!=null && userModel.getClientModel().getId()!=null){
            ClientView clientView = new ClientView();
            clientView.setId(userModel.getClientModel().getId());
            clientView.setName(userModel.getClientModel().getName());
            builder.setClientView(clientView);
        }
        if(userModel.getLocationModel()!=null && userModel.getLocationModel().getId()!=null){
            LocationView locationView = new LocationView();
            locationView.setId(userModel.getLocationModel().getId());
            locationView.setName(userModel.getLocationModel().getName());
            builder.setLocationView(locationView);
        }*/
        if (userModel.getPincode() != null) {
            builder.setPincode(userModel.getPincode());
        }
        if (userModel.getRoleModels() != null && !userModel.getRoleModels().isEmpty()) {
            List<RoleView> roleViews = new ArrayList<>();
            RoleView roleView = new RoleView.RoleViewBuilder().build();
            roleView.setViewList(userModel.getRoleModels(), roleViews);
            builder.setRoleViews(roleViews);
        }
        if (userModel.getScreenModels() != null && !userModel.getScreenModels().isEmpty()) {
            List<ScreenView> screenViews = new ArrayList<>();
            ScreenView screenView = new ScreenView.ScreenViewBuilder().build();
            screenView.setViewList(userModel.getScreenModels(), screenViews);
            builder.setScreenViews(screenViews);
        }
        builder.setModuleViews(setModules(userModel));

        return builder.build();
    }

    private List<ModuleView> setModules(UserModel userModel) {
        List<Long> ids = new ArrayList<>();
        for (RoleModel roleModel : userModel.getRoleModels()) {
            ids.add(roleModel.getId());
        }
        List<RoleModel> roleModels = roleService.getRights(ids);
        Map<ModuleEnum, List<RightsEnum>> map = new HashMap<>();
        for (RoleModel roleModel : roleModels) {
            Set<RoleModuleRightsModel> roleModuleRightsModels = roleModel.getRoleModuleRightsModels();
            for (RoleModuleRightsModel roleModuleRightsModel : roleModuleRightsModels) {
                ModuleEnum moduleEnum =
                        ModuleEnum.fromId(roleModuleRightsModel.getModuleModel().getId().intValue());
                RightsEnum rightsEnum =
                        RightsEnum.fromId(roleModuleRightsModel.getRightsModel().getId().intValue());
                List<RightsEnum> rightsEnums = new ArrayList<>();
                if (map.get(moduleEnum) != null && !map.get(moduleEnum).isEmpty()) {
                    rightsEnums = map.get(moduleEnum);
                }
                if (!rightsEnums.contains(rightsEnum)) {
                    rightsEnums.add(rightsEnum);
                }
                map.putIfAbsent(moduleEnum, rightsEnums);
            }
        }
        List<ModuleView> moduleViews = new ArrayList<>();
        for (Map.Entry<ModuleEnum, List<RightsEnum>> moduleEnum : map.entrySet()) {
            ModuleView moduleView = new ModuleView.ModuleViewBuilder().setId(moduleEnum.getKey().getId()).setName(moduleEnum.getKey().getName()).setRightsViews(new ArrayList<>()).build();
            for (RightsEnum rightsEnum : moduleEnum.getValue()) {
                RightsView rightsView = new RightsView.RightsViewBuilder().setId(rightsEnum.getId()).setName(rightsEnum.getName()).build();
                moduleView.getRightsViews().add(rightsView);
            }
            moduleViews.add(moduleView);
        }
        return moduleViews;
    }
    @Override
    protected UserModel getNewModel() {
        return new UserModel();
    }

    @Override
    public Response doSave(UserView userView) throws EndlosiotAPIException {
        if (!StringUtils.isBlank(userView.getEmail())) {
            UserModel userModel = userService.findByEmailActive(userView.getEmail());
            if (userModel != null) {
                throw new EndlosiotAPIException(ResponseCode.DUPLICATE_EMAIL_USER.getCode(), ResponseCode.DUPLICATE_EMAIL_USER.getMessage());
            }
        }
        if (!StringUtils.isBlank(userView.getMobile())) {
            UserModel userModel = userService.findByMobile(userView.getMobile());
            if (userModel != null) {
                throw new EndlosiotAPIException(ResponseCode.DUPLICATE_MOBILE_USER.getCode(), ResponseCode.DUPLICATE_MOBILE_USER.getMessage());
            }
        }
        UserModel userModel = toModel(new UserModel(), userView);
       /* if (userView.getClientView() != null && userView.getClientView().getId() != null) {
            ClientModel clientModel = clientService.get(userView.getClientView().getId());
            if (clientModel == null) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_CLIENT_ID.getCode(), ResponseCode.INVALID_CLIENT_ID.getMessage());
            }
            userModel.setClientModel(clientModel);
        }
        if (userView.getLocationView() != null && userView.getLocationView().getId() != null) {
            LocationModel locationModel = locationService.get(userView.getLocationView().getId());
            if (locationModel == null) {
                throw new EndlosiotAPIException(ResponseCode.LOCATION_NAME_IS_INVALID.getCode(), ResponseCode.LOCATION_NAME_IS_INVALID.getMessage());
            }
            userModel.setLocationModel(locationModel);
        }*/
        setRoleModel(userModel, userView);

        if( userView.getScreenViews() !=null)
            setScreenModel(userModel, userView);

        userModel.setResetPasswordTokenUsed(false);
        String token = RandomStringUtils.randomAlphanumeric(8);
        userModel.setUniqueToken(token);
        String tempPassword = userView.getPassword(); //Utility.generateToken(8);
        userModel.setTemporaryPassword(tempPassword);
        String tempPasswordFinal = HashUtil.hash(tempPassword);
        UserView user = new UserView.UserViewBuilder().setPassword(tempPasswordFinal).build();

        userModel.setActive(true);
        userModel.setCreateDate(DateUtility.getCurrentEpoch());
        userService.create(userModel);
        userService.insertSearchParam(userModel.getId());
        setPassword(userModel, user);
        sendUserCreateEmailNotification(userModel, userView);
        return CommonResponse.create(ResponseCode.SAVE_SUCCESSFULLY.getCode(), ResponseCode.SAVE_SUCCESSFULLY.getMessage());

    }
    private void setScreenModel(UserModel userModel, UserView userView) throws EndlosiotAPIException {
        Set<ScreenModel> existScreenModels = new HashSet<>();
        Set<ScreenModel> toDeleteScreenModels = new HashSet<>();
        Set<ScreenModel> toAddScreenModels = new HashSet<>();
        for (ScreenView screenView : userView.getScreenViews()) {
            ScreenModel tempScreenModel = screenService.get(screenView.getId());
            if (tempScreenModel == null) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());// invalidrole
            }
            if (!userModel.getRoleModels().contains(tempScreenModel)) {
                toAddScreenModels.add(tempScreenModel);
            } else {
                existScreenModels.add(tempScreenModel);
            }
        }
        for (ScreenModel screenModel : userModel.getScreenModels()) {
            if (!existScreenModels.contains(screenModel)) {
                toDeleteScreenModels.add(screenModel);
            }
        }
        addRemoveScreenModels(userModel, toAddScreenModels, toDeleteScreenModels);
    }
    private void addRemoveScreenModels(UserModel userModel, Set<ScreenModel> toAddScreenModels, Set<ScreenModel> toDeleteScreenModels) {
        for (ScreenModel screenModel : toDeleteScreenModels) {
            userModel.removeScreenModel(screenModel);
        }
        for (ScreenModel screenModel : toAddScreenModels) {
            userModel.addScreenModel(screenModel);
        }
    }

    @Override
    public Response doSendResentEmail(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException {
        UserModel userModel = validateUser(userView, isLoginThroughEmail);
        sendUserCreateEmailNotification(userModel, userView);
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }

    @Override
    public Response doUpdate(UserView userView) throws EndlosiotAPIException {
        UserModel userModel = userService.get(userView.getId());
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        if (!Auditor.getAuditor().equals(userModel)) {
            // checkAccess("doUpdate",RightsEnum.UPDATE, ModuleEnum.USER);
        }
        boolean updateEmail = false;
        if (!userModel.getEmail().equals(userView.getEmail())) {
            updateEmail = true;
            UserModel emailCheckModel = userService.findByEmailActive(userView.getEmail());
            if (emailCheckModel != null) {
                throw new EndlosiotAPIException(ResponseCode.DUPLICATE_EMAIL_USER.getCode(), ResponseCode.DUPLICATE_EMAIL_USER.getMessage());
            }
        }
        if (!StringUtils.isBlank(userView.getMobile()) && !StringUtils.isBlank(userModel.getMobile()) && !userModel.getMobile().equals(userView.getMobile())) {
            UserModel mobileCheckModel = userService.findByMobile(userView.getMobile());
            if (mobileCheckModel != null) {
                throw new EndlosiotAPIException(ResponseCode.DUPLICATE_MOBILE_USER.getCode(), ResponseCode.DUPLICATE_MOBILE_USER.getMessage());
            }
        }
        if (!Auditor.getAuditor().equals(userModel) && userView.getRoleViews() != null && !userView.getRoleViews().isEmpty()) {
            setRoleModel(userModel, userView);
        }
        /*if (!Auditor.getAuditor().equals(userModel) && userView.getScreenViews() != null && !userView.getScreenViews().isEmpty()) {
            setScreenModel(userModel, userView);
        }*/
        if (!Auditor.getAuditor().equals(userModel)) {
            setScreenModel(userModel, userView);
        }
        userModel = toModel(userModel, userView);
        userService.update(userModel);
        userService.updateSearchParam(userModel.getId());

        if (updateEmail) {
            Map<String, String> dynamicFields = new TreeMap<>();
            dynamicFields.put(CommunicationFields.EMAIL_TO.getName(), userModel.getEmail());
            dynamicFields.put(CommunicationFields.USER_NAME.getName(), userModel.getName());
            String url = SystemSettingModel.getUrl() + File.separator + "user" + File.separator + Constant.VERIFY_EMAIL_URL + File.separator + userModel.getVerifyToken();
            dynamicFields.put(CommunicationFields.VERIFICATION_LINK.getName(), url);

            NotificationEnum.RESEND_VERIFICATION_EMAIL.sendNotification(NotificationModel.getMAP().get(Long.valueOf(NotificationEnum.RESEND_VERIFICATION_EMAIL.getId())), dynamicFields);
        }
        String newPassword = HashUtil.hash(userView.getPassword());
        updateUserPassword(userModel, null, newPassword);

        return CommonResponse.create(ResponseCode.UPDATE_SUCCESSFULLY.getCode(), ResponseCode.UPDATE_SUCCESSFULLY.getMessage());
    }

    @Override
    public Response doSearch(UserView userView, Integer start, Integer recordSize) {
        List<UserSearchModel> userSearchModels = new ArrayList<>();
        if (!StringUtils.isBlank(userView.getFullTextSearch())) {
            userSearchModels = userService.fullTextSearch(userView.getFullTextSearch());
            if (userSearchModels == null || userSearchModels.isEmpty()) {
                return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
            }
        }
        PageModel pageModel = userService.searchLight(userView, userSearchModels, start, recordSize);
        if (pageModel == null || (pageModel.getList() != null && pageModel.getList().isEmpty())) {
            return PageResultResponse.create(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage(), 0, Collections.emptyList());
        }
        return PageResultResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), pageModel.getRecords(), fromModelList((List<UserModel>) pageModel.getList()));
    }

    @Override
    public Response doLogin(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException {
        UserModel userModel = validateUser(userView, isLoginThroughEmail);
        UserPasswordModel userPasswordModel = userPasswordService.getCurrent(userModel.getId());
        if (userPasswordModel == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_LOGINID_OR_PASSWORD.getCode(), ResponseCode.INVALID_LOGINID_OR_PASSWORD.getMessage());
        }
        if (!HashUtil.matchHash(userView.getPassword(), userPasswordModel.getPassword())) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_LOGINID_OR_PASSWORD.getCode(), ResponseCode.INVALID_LOGINID_OR_PASSWORD.getMessage());
        }
        if (!StringUtils.isEmpty(userModel.getResetPasswordToken()) && !userModel.isVerifyTokenUsed()) {
            return CommonResponse.create(ResponseCode.EMAIL_VERIFICATION.getCode(), ResponseCode.EMAIL_VERIFICATION.getMessage());
        }
        boolean isUserHasPassword = userPasswordService.getByUser(userModel.getId(), 3) != null && !userPasswordService.getByUser(userModel.getId(), 3).isEmpty();
        if (!isUserHasPassword) {
            return CommonResponse.create(ResponseCode.THIS_ACCOUNT_HAS_NO_PASSWORD.getCode(), ResponseCode.THIS_ACCOUNT_HAS_NO_PASSWORD.getMessage());
        }
        if (!userModel.isHasLoggedIn()) {
            userModel.setHasLoggedIn(true);
        }
        if (!userModel.isHasLoggedIn()) {
            userModel.setHasLoggedIn(true);
        }
        boolean isDeviceAuthenticationRequired = setDeviceToken(userModel);
        if (CommonStatusEnum.YES.equals(SystemSettingModel.getTwoFactorAuthenticationEnable()) && isDeviceAuthenticationRequired) {
            return generateTwoFactorToken(userModel);
        }
        userModel.setVerifyTokenUsed(true);
        userService.update(userModel);
        UserView responseView = fromModel(userModel);
        ViewResponse viewResponse;
        if (userModel.isTempPassword()) {
            viewResponse = ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), responseView);
        } else {
            viewResponse = ViewResponse.create(ResponseCode.DEFAULT_PASSWORD_CHANGE.getCode(), ResponseCode.DEFAULT_PASSWORD_CHANGE.getMessage(), responseView);
        }
        JwtTokenModel jwtTokenModel = JwtTokenModel.createLoginToken();
        jwtTokenModel.setUniqueToken(userModel.getUniqueToken());
        viewResponse.setAccessToken(JwtUtil.generateAccessToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel), jwtTokenModel));
        viewResponse.setRefreshToken(JwtUtil.generateRefreshToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel)));
        return viewResponse;
    }

    private Response generateTwoFactorToken(UserModel userModel) throws EndlosiotAPIException {
        userModel.setTwofactorTokenUsed(false);
        userModel.setTwofactorToken(Utility.generateOTP(6));
        userModel.setTwofactorDate(DateUtility.getCurrentEpoch());
        JwtTokenModel jwtTokenModel = JwtTokenModel.createTwoFactorToken();
        jwtTokenModel.setUniqueToken(userModel.getUniqueToken());
        userService.update(userModel);
        return CommonResponse.create(ResponseCode.VALIDATE_NEW_DEVICE.getCode(), ResponseCode.VALIDATE_NEW_DEVICE.getMessage(), JwtUtil.generateAccessToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel), jwtTokenModel), null);
    }

    private void setPassword(UserModel userModel, UserView userView) throws EndlosiotAPIException {
        UserPasswordModel userPasswordModel = new UserPasswordModel();
        userPasswordModel.setUserModel(userModel);
        //userPasswordModel.setPassword(HashUtil.hash(userView.getPassword()));
        userPasswordModel.setPassword(userView.getPassword());
        userPasswordModel.setCreate(Instant.now().getEpochSecond());
        userPasswordService.create(userPasswordModel);
    }

    private boolean setDeviceToken(UserModel userModel) throws EndlosiotAPIException {
        Long currentDate = DateUtility.getCurrentEpoch();
        String deviceCookie = null;
        Long maxAllowDevice = SystemSettingModel.getMaxAllowedDevice();
        Integer deviceCookieTime = SystemSettingModel.getDeviceCookieTimeInSeconds();

        Cookie[] cookies = WebUtil.getCurrentRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.DEVICE_TOKEN.equals(cookie.getName())) {
                    deviceCookie = cookie.getValue();
                }
            }
        }
        Long deviceCount = userSessionService.deviceUsed(userModel.getId());
        boolean isDeviceRegistered = userSessionService.isDeviceRegistered(deviceCookie);
        if (isDeviceRegistered) {
            UserSessionModel deviceUserSessionModel = userSessionService.getByDeviceCookie(deviceCookie, userModel.getId());
            if (deviceUserSessionModel == null) {
                deviceUserSessionModel = new UserSessionModel(HttpUtil.getUserBrowser(), HttpUtil.getUserOs(), HttpUtil.getUserIpAddress(), deviceCookie, currentDate, currentDate, userModel);
                userSessionService.create(deviceUserSessionModel);
                CookieUtility.setCookie(WebUtil.getCurrentResponse(), Constant.DEVICE_TOKEN, deviceUserSessionModel.getDeviceCookie(), deviceCookieTime, WebUtil.getCurrentRequest().getContextPath());
                return !deviceCount.equals(Long.valueOf(0));
            } else {
                deviceUserSessionModel.setLastLoginDate(currentDate);
                userSessionService.update(deviceUserSessionModel);
                return false;
            }
        } else {
            if (deviceCount != null && maxAllowDevice <= deviceCount) {
                userSessionService.deleteLeastUnused(userModel.getId());
            }
            UserSessionModel userSessionModel = new UserSessionModel(HttpUtil.getUserBrowser(), HttpUtil.getUserOs(), HttpUtil.getUserIpAddress(), HashUtil.generateDeviceToken(), currentDate, currentDate, userModel);
            userSessionService.create(userSessionModel);
            CookieUtility.setCookie(WebUtil.getCurrentResponse(), Constant.DEVICE_TOKEN, userSessionModel.getDeviceCookie(), deviceCookieTime, WebUtil.getCurrentRequest().getContextPath());
            return !deviceCount.equals(Long.valueOf(0));
        }
    }

    @Override
    public Response doResetPasswordVerification(String otp) throws EndlosiotAPIException {
        UserModel userModel = userService.get(Auditor.getAuditor().getId());
        if (userModel.isResetPasswordTokenUsed()) {
            throw new EndlosiotAPIException(ResponseCode.OTP_EXPIRED.getCode(), ResponseCode.OTP_EXPIRED.getMessage());
        }
        if (!otp.equals(userModel.getResetPasswordToken())) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_OTP.getCode(), ResponseCode.INVALID_OTP.getMessage());
        }
        if (DateUtility.isResetPasswordValidMinutes(userModel.getResetPasswordDate(), SystemSettingModel.getResetPasswordTokenValidMinutes())) {
            throw new EndlosiotAPIException(ResponseCode.OTP_EXPIRED.getCode(), ResponseCode.OTP_EXPIRED.getMessage());
        }
        userModel.setResetPasswordTokenUsed(true);
        userService.update(userModel);
        return CommonResponse.create(ResponseCode.FORGET_PASSWORD_VERIFICATION_SUCCESSFUL.getCode(), ResponseCode.FORGET_PASSWORD_VERIFICATION_SUCCESSFUL.getMessage());
    }

    @Override
    public Response doActivate(String token) throws EndlosiotAPIException {
        return CommonResponse.create(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
    }

    @Override
    public Response doSendResetLink(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException {
        UserModel userModel = validateUser(userView, isLoginThroughEmail);
//        int count = 1;
//        if (userModel.getResetPasswordDate() != null) {
//            LocalDate localDate = DateUtility.getLocalDate(userModel.getResetPasswordDate());
//            if (localDate.equals(LocalDate.now())) {
//                count = userModel.getResetPasswordDayWiseCount() + 1;
//                if (userModel.getResetPasswordDayWiseCount() >= SystemSettingModel.getResetPasswordDayWiseLimit()) {
//                    throw new EndlosiotAPIException(ResponseCode.MAX_LIMIT_REACHED_FOR_RESET_PASSWORD.getCode(), ResponseCode.MAX_LIMIT_REACHED_FOR_RESET_PASSWORD.getMessage());
//                }
//            }
//        }
        userModel.setResetPasswordToken(Utility.generateOTP(4));
        userModel.setResetPasswordTokenUsed(false);
        userModel.setResetPasswordDate(DateUtility.getCurrentEpoch());
        userService.update(userModel);

        Map<String, String> dynamicFields = prepareResetPasswordEmail(userModel);
        NotificationEnum.USER_RESET_PASSWORD.sendNotification(NotificationModel.getMAP().get(Long.valueOf(NotificationEnum.USER_RESET_PASSWORD.getId())), dynamicFields);
        CommonResponse commonResponse = CommonResponse.create(ResponseCode.FORGET_PASSWORD_SUCCESSFUL.getCode(), ResponseCode.FORGET_PASSWORD_SUCCESSFUL.getMessage());
        JwtTokenModel jwtTokenModel = JwtTokenModel.createResetPasswordToken();
        jwtTokenModel.setUniqueToken(userModel.getUniqueToken());
        commonResponse.setAccessToken(JwtUtil.generateAccessToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel), jwtTokenModel));
        commonResponse.setRefreshToken(JwtUtil.generateRefreshToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel)));
        return commonResponse;
    }

    private Map<String, String> prepareResetPasswordEmail(UserModel userModel) {
        Map<String, String> dynamicFields = new TreeMap<>();
        dynamicFields.put(CommunicationFields.EMAIL_TO.getName(), userModel.getEmail());
        dynamicFields.put(CommunicationFields.MOBILE.getName(), userModel.getMobile());
        dynamicFields.put(CommunicationFields.USER_NAME.getName(), userModel.getName());
        dynamicFields.put(CommunicationFields.RESET_PASSWORD_CODE.getName(), userModel.getResetPasswordToken());
        return dynamicFields;
    }

    /**
     * this method used to send email for verification.
     *
     * @param userModel
     */
    private void sendResetPasswordEmail(UserModel userModel) {
        Map<String, String> dynamicFields = new TreeMap<>();
        dynamicFields.put(CommunicationFields.EMAIL_TO.getName(), userModel.getEmail());
        dynamicFields.put(CommunicationFields.USER_NAME.getName(), userModel.getName());
        String link = "";
        dynamicFields.put(CommunicationFields.RESET_PASSWORD_LINK.getName(), link);
        Long hours = TimeUnit.MINUTES.toHours(SystemSettingModel.getResetPasswordTokenValidMinutes());
        dynamicFields.put(CommunicationFields.LINK_EXPIRE_TIME.getName(), hours.toString());
        NotificationEnum.USER_RESET_PASSWORD.sendNotification(NotificationModel.getMAP().get(Long.valueOf(NotificationEnum.USER_RESET_PASSWORD.getId())), dynamicFields);
    }

    private String getResetPasswordJWTToken(UserModel userModel) throws EndlosiotAPIException {
        JwtTokenModel jwtTokenModel = JwtTokenModel.createResetPasswordToken();
        jwtTokenModel.setUniqueToken(userModel.getUniqueToken());
        return JwtUtil.generateAccessToken(userModel.getEmail(), JsonUtil.toJson(jwtTokenModel), jwtTokenModel);
    }

    @Override
    public Response doResetPassword(UserView userView) throws EndlosiotAPIException {
        String jwtAccesssTokenHeader = WebUtil.getCurrentRequest().getHeader("Authorization");
        String[] accessToken = jwtAccesssTokenHeader.split(" ");
        String userEmail = JwtUtil.extractData(accessToken[1], JwtUtil.extractAllClaims(accessToken[1]));
        UserModel userModel = userService.findByEmail(userEmail);

        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.UNAUTHORIZED_ACCESS.getCode(), ResponseCode.UNAUTHORIZED_ACCESS.getMessage());
        }
        String newPassword = HashUtil.hash(userView.getPassword());
        boolean isUserHasPassword = userPasswordService.getByUser(userModel.getId(), SystemSettingModel.getMaxPasswordStoreCountPerUser()) != null && !userPasswordService.getByUser(userModel.getId(), SystemSettingModel.getMaxPasswordStoreCountPerUser()).isEmpty();
        if (isUserHasPassword) {
            List<UserPasswordModel> userPasswordModels = validateLastUsedPasswords(userModel, userView, false);
            updateUserPassword(userModel, userPasswordModels, newPassword);
        } else {
            setPassword(userModel, userView);
        }

        userModel.setVerifyTokenUsed(true);
        updateUserPassword(userModel, null, newPassword);
        TokenBlackListModel tokenBlackListModel = new TokenBlackListModel();
        tokenBlackListModel.setUserModel(userModel);
        tokenBlackListModel.setJwtToken(accessToken[1]);
        tokenBlackListService.create(tokenBlackListModel);
        return CommonResponse.create(ResponseCode.RESET_PASSWORD_SUCCESSFUL.getCode(), ResponseCode.RESET_PASSWORD_SUCCESSFUL.getMessage());
    }

    @Override
    public Response doChangePassword(UserView userView) throws EndlosiotAPIException {
        UserModel userModel = userService.get(Auditor.getAuditor().getId());
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        boolean isUserHasPassword = userPasswordService.getByUser(userModel.getId(), 3) != null && !userPasswordService.getByUser(userModel.getId(), 3).isEmpty();
        if (!isUserHasPassword) {
            return CommonResponse.create(ResponseCode.THIS_ACCOUNT_HAS_NO_PASSWORD.getCode(), ResponseCode.THIS_ACCOUNT_HAS_NO_PASSWORD.getMessage());
        }
        String jwtAccesssTokenHeader = WebUtil.getCurrentRequest().getHeader("Authorization");
        String[] accessToken = jwtAccesssTokenHeader.split(" ");
        String userEmail = JwtUtil.extractData(accessToken[1], JwtUtil.extractAllClaims(accessToken[1]));
        UserModel existUserModel = userService.findByEmail(userEmail);

        if (!existUserModel.equals(userModel)) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        String newPassword = HashUtil.hash(userView.getPassword());
        List<UserPasswordModel> userPasswordModel = validateLastUsedPasswords(existUserModel, userView, true);
        updateUserPassword(existUserModel, userPasswordModel, newPassword);
        if (userModel.isTempPassword()) {
            userModel = userService.get(userModel.getId());
            userModel.setTempPassword(false);
            userService.update(userModel);
        }
        String token = Utility.generateToken(8);
        existUserModel.setUniqueToken(token);
        existUserModel.setTempPassword(true);
        userService.create(existUserModel);
        TokenBlackListModel tokenBlackListModel = new TokenBlackListModel();
        tokenBlackListModel.setUserModel(userModel);
        tokenBlackListModel.setJwtToken(accessToken[1]);
        tokenBlackListService.create(tokenBlackListModel);

        return CommonResponse.create(ResponseCode.CHANGE_PASSWORD_SUCCESSFUL.getCode(), ResponseCode.CHANGE_PASSWORD_SUCCESSFUL.getMessage());
    }

    private void updateUserPassword(UserModel userModel, List<UserPasswordModel> userPasswordModels, String newPassword) {
        if (CommonStatusEnum.YES.equals(SystemSettingModel.getPasswordUsedValidationEnabled())) {
            if (userPasswordModels != null && !userPasswordModels.isEmpty() && userPasswordModels.size() >= SystemSettingModel.getMaxPasswordStoreCountPerUser()) {
                UserPasswordModel userPasswordModel = userPasswordModels.get(userPasswordModels.size() - 1);
                userPasswordModel.setCreate(Instant.now().getEpochSecond());
                userPasswordModel.setPassword(newPassword);
                userPasswordService.update(userPasswordModel);
            } else {
                UserPasswordModel userPasswordModel = new UserPasswordModel();
                userPasswordModel.setUserModel(userModel);
                userPasswordModel.setPassword(newPassword);
                userPasswordModel.setCreate(Instant.now().getEpochSecond());
                userPasswordService.create(userPasswordModel);
            }
        } else {
            UserPasswordModel userPasswordModel = new UserPasswordModel();
            userPasswordModel.setUserModel(userModel);
            userPasswordModel.setPassword(newPassword);
            userPasswordModel.setCreate(Instant.now().getEpochSecond());
            userPasswordService.create(userPasswordModel);
        }
    }


    private UserModel validateUser(UserView userView, boolean isLoginThroughEmail) throws EndlosiotAPIException {
        UserModel userModel = null;
        if (isLoginThroughEmail) {
            userModel = userService.findByEmail(userView.getLoginId());
        } else {
            userModel = userService.findByMobile(userView.getLoginId());
        }
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_LOGINID_OR_PASSWORD.getCode(), ResponseCode.INVALID_LOGINID_OR_PASSWORD.getMessage());
        }
        return userModel;
    }

    private List<UserPasswordModel> validateLastUsedPasswords(UserModel userModel, UserView userView, boolean isChangePwd) throws EndlosiotAPIException {
        Integer count = SystemSettingModel.getMaxPasswordStoreCountPerUser();
        List<UserPasswordModel> userPasswordModels = userPasswordService.getByUser(userModel.getId(), count);
        UserPasswordModel userPasswordModelTemp = userPasswordModels.get(0);
        if (isChangePwd && userPasswordModelTemp != null && !HashUtil.matchHash(userView.getOldPassword(), userPasswordModelTemp.getPassword())) {
            throw new EndlosiotAPIException(ResponseCode.CURRENT_PASSWORD_IS_INVALID.getCode(), ResponseCode.CURRENT_PASSWORD_IS_INVALID.getMessage());
        }
        for (UserPasswordModel userPasswordModel : userPasswordModels) {
            if (CommonStatusEnum.YES.getId().equals(SystemSettingModel.getPasswordUsedValidationEnabled().getId()) && HashUtil.matchHash(userView.getPassword(), userPasswordModel.getPassword())) {
                throw new EndlosiotAPIException(ResponseCode.DUPLICATE_PASSWORD_USER.getCode(), ResponseCode.DUPLICATE_PASSWORD_USER.getMessage());
            }
        }
        return userPasswordModels;
    }

    @Override
    public Response doIsLoggedIn() throws EndlosiotAPIException {
        UserModel userModel = userService.get(Auditor.getAuditor().getId());
        UserView userView = fromModel(userModel);
        return ViewResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage(), userView);
    }

    @Override
    public Response doGetAccessToken(UserView userView) throws EndlosiotAPIException {
        JWTExceptionEnum jwtRefreshPair = JwtUtil.isValidJWTToken(userView.getRefreshToken());
        if (jwtRefreshPair == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        if (jwtRefreshPair.equals(JWTExceptionEnum.SIGNAUTURE_EXCEPTION)) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REFRESH_JSON_TOKEN.getCode(), ResponseCode.INVALID_REFRESH_JSON_TOKEN.getMessage());
        }
        if (jwtRefreshPair.equals(JWTExceptionEnum.EXPIRED_JWT_EXCEPTION)) {
            throw new EndlosiotAPIException(ResponseCode.AUTHENTICATION_REQUIRED.getCode(), ResponseCode.AUTHENTICATION_REQUIRED.getMessage());
        }
        Claims claims = JwtUtil.extractAllClaims(userView.getRefreshToken());
        String userEmail = JwtUtil.extractData(userView.getRefreshToken(), claims);
        UserModel userModel = userService.findByEmail(userEmail);
        JwtTokenModel jwtTokenModel = JwtTokenModel.createLoginToken();
        jwtTokenModel.setUniqueToken(userModel.getUniqueToken());
        CommonResponse commonResponse = CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
        commonResponse.setAccessToken(JwtUtil.generateAccessToken(userEmail, JsonUtil.toJson(jwtTokenModel), null));
        setNewRefreshToken(userView, commonResponse, userEmail, jwtTokenModel);
        return commonResponse;
    }

    private void setNewRefreshToken(UserView userView, CommonResponse commonResponse, String userEmail, JwtTokenModel jwtTokenModel) throws EndlosiotAPIException {
        Date refreshExpiredDate = JwtUtil.extractExpiration(userView.getRefreshToken(), null);
        Duration duration = Duration.between(LocalDateTime.now(), refreshExpiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        if (duration.toMinutes() < SystemSettingModel.getGenerateRefreshTokenTimeInMinutes()) {
            commonResponse.setRefreshToken(JwtUtil.generateRefreshToken(userEmail, JsonUtil.toJson(jwtTokenModel)));
        }
    }

    @Override
    public Response doLogout(String jwtAccessToken) throws EndlosiotAPIException {
        Claims claims = null;
        try {
            claims = JwtUtil.extractAllClaims(jwtAccessToken);
        } catch (EndlosiotAPIException e) {
            LoggerService.exception(e);
        }
        String userEmail = JwtUtil.extractData(jwtAccessToken, claims);
        UserModel userModel = userService.findByEmail(userEmail);
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
        TokenBlackListModel tokenBlackListModel = new TokenBlackListModel();
        tokenBlackListModel.setUserModel(userModel);
        tokenBlackListModel.setJwtToken(jwtAccessToken);
        tokenBlackListService.create(tokenBlackListModel);
        return CommonResponse.create(ResponseCode.LOGGED_OUT_SUCCESSFUL.getCode(), ResponseCode.LOGGED_OUT_SUCCESSFUL.getMessage());
    }

    @Override
    public Response doUnlockAccount(Long id) throws EndlosiotAPIException {
        UserModel userModel = userService.get(id);
        if (userModel == null) {
            throw new EndlosiotAPIException(ResponseCode.NO_DATA_FOUND.getCode(), ResponseCode.NO_DATA_FOUND.getMessage());
        }
        userService.update(userModel);
        return CommonResponse.create(ResponseCode.SUCCESSFUL.getCode(), ResponseCode.SUCCESSFUL.getMessage());
    }

    @Override
    public Response doResendPasswordVerificationOtp() throws EndlosiotAPIException {
        UserModel userModel = userService.get(Auditor.getAuditor().getId());
        if (userModel.isResetPasswordTokenUsed()) {
            throw new EndlosiotAPIException(ResponseCode.OTP_EXPIRED.getCode(), ResponseCode.OTP_EXPIRED.getMessage());
        }
//        if (userModel.getResetPasswordCount() >= SystemSettingModel.getResetPasswordResendLimit()) {
//            throw new EndlosiotAPIException(ResponseCode.MAX_LIMIT_REACHED_FOR_RESEND_RESET_PASSWORD.getCode(), ResponseCode.MAX_LIMIT_REACHED_FOR_RESEND_RESET_PASSWORD.getMessage());
//        }
//        userModel.setResetPasswordCount(userModel.getResetPasswordCount() + 1);
        userModel.setResetPasswordTokenUsed(false);
        userModel.setResetPasswordToken(Utility.generateOTP(4));
        userService.update(userModel);

        Map<String, String> dynamicFields = prepareResendResetPasswordEmail(userModel);
        NotificationEnum.RESEND_PASSWORD_CODE.sendNotification(NotificationModel.getMAP().get(Long.valueOf(NotificationEnum.RESEND_PASSWORD_CODE.getId())), dynamicFields);
        return CommonResponse.create(ResponseCode.RESENT_PASSWORD_VERIFICATION_OTP_SUCCESSFULLY.getCode(), ResponseCode.RESENT_PASSWORD_VERIFICATION_OTP_SUCCESSFULLY.getMessage());
    }

    private void setRoleModel(UserModel userModel, UserView userView) throws EndlosiotAPIException {
        Set<RoleModel> existRoleModels = new HashSet<>();
        Set<RoleModel> toDeleteRoleModels = new HashSet<>();
        Set<RoleModel> toAddRoleModels = new HashSet<>();
        for (RoleView roleView : userView.getRoleViews()) {
            RoleModel tempRoleModel = roleService.get(roleView.getId());
            if (tempRoleModel == null) {
                throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());// invalidrole
            }
            if (!userModel.getRoleModels().contains(tempRoleModel)) {
                toAddRoleModels.add(tempRoleModel);
            } else {
                existRoleModels.add(tempRoleModel);
            }
        }
        for (RoleModel roleModel : userModel.getRoleModels()) {
            if (!existRoleModels.contains(roleModel)) {
                toDeleteRoleModels.add(roleModel);
            }
        }
        addRemoveModels(userModel, toAddRoleModels, toDeleteRoleModels);
        //validateRole(userModel);
    }

    private void addRemoveModels(UserModel userModel, Set<RoleModel> toAddRoleModels, Set<RoleModel> toDeleteRoleModels) {
        for (RoleModel roleModel : toDeleteRoleModels) {
            userModel.removeRoleModel(roleModel);
        }
        for (RoleModel roleModel : toAddRoleModels) {
            userModel.addRoleModel(roleModel);
        }
    }

    private void validateRole(UserModel userModel) throws EndlosiotAPIException {
        boolean customer = false;
        for (RoleModel roleModel : userModel.getRoleModels()) {
            if (roleModel.getTypeId() == (RoleTypeEnum.CLIENT_USER.getId())) {
                customer = true;
                break;
            }
        }
        if (customer) {
            throw new EndlosiotAPIException(ResponseCode.INVALID_REQUEST.getCode(), ResponseCode.INVALID_REQUEST.getMessage());
        }
    }

    private void sendUserCreateEmailNotification(UserModel userModel, UserView userView) {
        Map<String, String> dynamicFields = new TreeMap<>();
        dynamicFields.put(CommunicationFields.EMAIL_TO.getName(), userModel.getEmail());
        dynamicFields.put(CommunicationFields.MOBILE.getName(), userModel.getMobile());
        dynamicFields.put(CommunicationFields.USER_NAME.getName(), userModel.getName());
        dynamicFields.put(CommunicationFields.PASSWORD.getName(), userModel.getTemporaryPassword());
        dynamicFields.put(CommunicationFields.URL.getName(), SystemSettingModel.getUrl());
        NotificationEnum.MASTER_ADMIN_USER_CREATE.sendNotification(NotificationModel.getMAP().get(Long.valueOf(NotificationEnum.MASTER_ADMIN_USER_CREATE.getId())), dynamicFields);
    }

    @Override
    public BaseService getService() {
        return userService;
    }
}