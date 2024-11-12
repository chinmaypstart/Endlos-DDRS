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
package com.endlosiot.common.user.view;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.view.FileView;
import com.endlosiot.common.location.view.CountryView;
import com.endlosiot.common.modelenums.CommonStatusEnum;
import com.endlosiot.common.setting.model.SystemSettingModel;
import com.endlosiot.common.user.model.UserModel;
import com.endlosiot.common.view.ArchiveView;
import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.screen.view.ScreenView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonDeserialize(builder = UserView.UserViewBuilder.class)
public class UserView extends ArchiveView {
    private static final long serialVersionUID = 1L;
    private final String name;
    private final String email;
    private final String mobile;
    private final String password;
    private final String confirmPassword;
    private final String oldPassword;
    private final String token;
    private final List<RoleView> roleViews;
    private final Boolean hasLoggedIn;
    private final String loginId;
    private final String shortName;
    private final boolean verificationOtpUsed;
    private final String verificaitionOtp;
    private final Long searchRoleId;
    private final FileView profilepic;
    private final String address;
    private final String landmark;
    private final CountryView countryView;
    private final KeyValueView stateView;
    private final KeyValueView cityView;
    private final String pincode;
    private final String fullTextSearch;
    private final RoleView roleView;
    private final List<ModuleView> moduleViews;
    //    private final String file;
    private final String accessToken;
    private final String refreshToken;
    /*private final ClientView clientView;
    private final LocationView locationView;*/

    private final List<ScreenView> screenViews;
    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getToken() {
        return token;
    }

    public Boolean getHasLoggedIn() {
        return hasLoggedIn;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getShortName() {
        return shortName;
    }

    public Long getSearchRoleId() {
        return searchRoleId;
    }

    public FileView getProfilepic() {
        return profilepic;
    }

    public String getAddress() {
        return address;
    }

    public String getLandmark() {
        return landmark;
    }

    public CountryView getCountryView() {
        return countryView;
    }

    public KeyValueView getStateView() {
        return stateView;
    }

    public String getPincode() {
        return pincode;
    }

    public String getFullTextSearch() {
        return fullTextSearch;
    }

    public List<ModuleView> getModuleViews() {
        return moduleViews;
    }

    public List<RoleView> getRoleViews() {
        return roleViews;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isVerificationOtpUsed() {
        return verificationOtpUsed;
    }

    public String getVerificaitionOtp() {
        return verificaitionOtp;
    }

    public KeyValueView getCityView() {
        return cityView;
    }

    public RoleView getRoleView() {
        return roleView;
    }

    /*public ClientView getClientView() {
        return clientView;
    }

    public LocationView getLocationView() {
        return locationView;
    }*/

    public List<ScreenView> getScreenViews() {
        return screenViews;
    }

    public UserView(UserViewBuilder userViewBuilder) {
        this.setId(userViewBuilder.id);
        this.name = userViewBuilder.name;
        this.email = userViewBuilder.email;
        this.mobile = userViewBuilder.mobile;
        this.password = userViewBuilder.password;
        this.confirmPassword = userViewBuilder.confirmPassword;
        this.oldPassword = userViewBuilder.oldPassword;
        this.token = userViewBuilder.token;
        this.hasLoggedIn = userViewBuilder.hasLoggedIn;
        this.loginId = userViewBuilder.loginId;
        this.shortName = userViewBuilder.shortName;
        this.searchRoleId = userViewBuilder.searchRoleId;
        this.profilepic = userViewBuilder.profilepic;
        this.address = userViewBuilder.address;
        this.landmark = userViewBuilder.landmark;
        this.countryView = userViewBuilder.countryView;
        this.stateView = userViewBuilder.stateView;
        this.pincode = userViewBuilder.pincode;
        this.fullTextSearch = userViewBuilder.fullTextSearch;
        this.moduleViews = userViewBuilder.moduleViews;
        this.roleViews = userViewBuilder.roleViews;
        this.accessToken = userViewBuilder.accessToken;
        this.refreshToken = userViewBuilder.refreshToken;
        this.setCreateDate(userViewBuilder.createDate);
        this.setActive(userViewBuilder.active);
        this.setArchive(userViewBuilder.archive);
        this.roleView = userViewBuilder.roleView;
        /*this.clientView = userViewBuilder.clientView;
        this.locationView = userViewBuilder.locationView;*/
        this.cityView = userViewBuilder.cityView;
        this.verificaitionOtp = userViewBuilder.verificaitionOtp;
        this.verificationOtpUsed = userViewBuilder.verificationOtpUsed;
        this.screenViews = userViewBuilder.screenView;

    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class UserViewBuilder {
        private long id;
        private String name;
        private String email;
        private String mobile;
        private String password;
        private String confirmPassword;
        private String oldPassword;
        private String token;
        private List<RoleView> roleViews;
        private Boolean hasLoggedIn;
        private String loginId;
        private String shortName;
        private boolean verificationOtpUsed;
        private String verificaitionOtp;
        private Long searchRoleId;
        private FileView profilepic;
        private String address;
        private String landmark;
        private CountryView countryView;
        private KeyValueView stateView;
        private KeyValueView cityView;
        private String pincode;
        private String fullTextSearch;
        private RoleView roleView;
        private List<ModuleView> moduleViews;
        private String file;
        private String accessToken;
        private String refreshToken;
        /*private ClientView clientView;
        private LocationView locationView;*/
        private Long createDate;
        private Boolean active;
        private Boolean archive;
        private List<ScreenView> screenView;

        public UserViewBuilder setId(long id) {
            this.id = id;
            return this;
        }

        public UserViewBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserViewBuilder setEmail(String email) {
            if (!StringUtils.isBlank(email)) {
                this.email = email.trim().toLowerCase();
            } else {
                this.email = null;
            }
            return this;
        }

        public UserViewBuilder setMobile(String mobile) {
            if (!StringUtils.isBlank(mobile)) {
                this.mobile = mobile.trim();
            } else {
                this.mobile = null;
            }
            return this;
        }

        public UserViewBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserViewBuilder setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public UserViewBuilder setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
            return this;
        }

        public UserViewBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public UserViewBuilder setHasLoggedIn(Boolean hasLoggedIn) {
            this.hasLoggedIn = hasLoggedIn;
            return this;
        }

        public UserViewBuilder setLoginId(String loginId) {
            if (!StringUtils.isBlank(loginId)) {
                this.loginId = loginId.trim().toLowerCase();
            } else {
                this.loginId = null;
            }
            return this;
        }

        public UserViewBuilder setShortName(String shortName) {
            this.shortName = shortName;
            return this;
        }

        public UserViewBuilder setSearchRoleId(Long searchRoleId) {
            this.searchRoleId = searchRoleId;
            return this;
        }

        public UserViewBuilder setProfilepic(FileView profilepic) {
            this.profilepic = profilepic;
            return this;
        }

        public UserViewBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public UserViewBuilder setLandmark(String landmark) {
            this.landmark = landmark;
            return this;
        }

        public UserViewBuilder setCountryView(CountryView countryView) {
            this.countryView = countryView;
            return this;
        }

        public UserViewBuilder setStateView(KeyValueView stateView) {
            this.stateView = stateView;
            return this;
        }

        public UserViewBuilder setPincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public UserViewBuilder setFullTextSearch(String fullTextSearch) {
            this.fullTextSearch = fullTextSearch;
            return this;
        }

        public UserViewBuilder setModuleViews(List<ModuleView> moduleViews) {
            this.moduleViews = moduleViews;
            return this;
        }

        public UserViewBuilder setRoleViews(List<RoleView> roleViews) {
            this.roleViews = roleViews;
            return this;
        }

        public UserViewBuilder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public UserViewBuilder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }


        public UserViewBuilder setCreateDate(Long createDate) {
            this.createDate = createDate;
            return this;
        }

        public UserViewBuilder setActive(Boolean active) {
            this.active = active;
            return this;
        }

        public UserViewBuilder setArchive(Boolean archive) {
            this.archive = archive;
            return this;
        }

        public UserViewBuilder setVerificationOtpUsed(boolean verificationOtpUsed) {
            this.verificationOtpUsed = verificationOtpUsed;
            return this;
        }

        public UserViewBuilder setVerificaitionOtp(String verificaitionOtp) {
            this.verificaitionOtp = verificaitionOtp;
            return this;
        }

        public UserViewBuilder setCityView(KeyValueView cityView) {
            this.cityView = cityView;
            return this;
        }

        public UserViewBuilder setRoleView(RoleView roleView) {
            this.roleView = roleView;
            return this;
        }

        public UserViewBuilder setFile(String file) {
            this.file = file;
            return this;
        }

        /*public UserViewBuilder setClientView(ClientView clientView) {
            this.clientView = clientView;
            return this;
        }

        public UserViewBuilder setLocationView(LocationView locationView) {
            this.locationView = locationView;
            return this;
        }*/

        public UserViewBuilder setScreenViews(List<ScreenView> screenViews) {
            this.screenView = screenViews;
            return this;
        }

        public UserView build() {
            return new UserView(this);
        }
    }

    public static UserView setUserView(UserModel userModel) {
        UserViewBuilder builder = new UserViewBuilder().setId(userModel.getId())
                .setName(userModel.getName());
        if (!StringUtils.isBlank(userModel.getEmail())) {
            builder.setEmail(userModel.getEmail());
        }
        if (!StringUtils.isBlank(userModel.getMobile())) {
            builder.setMobile(userModel.getMobile());
        }
        return builder.build();
    }

    public static UserView setView(UserModel userModel) {
        UserViewBuilder builder = new UserViewBuilder().setId(userModel.getId())
                .setName(userModel.getName())
                .setEmail(userModel.getEmail());
        if (userModel.getMobile() != null) {
            builder.setMobile(userModel.getMobile());
        }
        return builder.build();
    }


    public static void validatePassword(String password) throws NumberFormatException, EndlosiotAPIException {
        if (CommonStatusEnum.YES
                .equals(CommonStatusEnum.fromId(SystemSettingModel.getPasswordGenerationSyntaxCheckingEnabled()))) {
            char[] charArray = password.toCharArray();
            Integer upperCase = 0, lowerCase = 0, specialChar = 0, numbers = 0;
            for (int i = 0; i < charArray.length; i++) {
                if (Character.isUpperCase(charArray[i])) {
                    upperCase++;
                }
                if (Character.isLowerCase(charArray[i])) {
                    lowerCase++;
                }
                if (Character.isDigit(charArray[i])) {
                    numbers++;
                }
                if ("!@#$%^&*()_+=-".contains(String.valueOf(charArray[i]))) {
                    specialChar++;
                }
            }
            if (SystemSettingModel.getPasswordGenerationMinLength() > password.length()) {
                sendErrorMessage();
            }
            if (SystemSettingModel.getPasswordGenerationMaxLength() < password.length()) {
                sendErrorMessage();
            }
            if (SystemSettingModel.getPasswordGenerationMinUpperCaseAlphabets() > upperCase) {
                sendErrorMessage();
            }
            if (SystemSettingModel.getPasswordGenerationMinLowerCaseAlphabets() > lowerCase) {
                sendErrorMessage();
            }
            if (SystemSettingModel.getPasswordGenerationMinNumerics() > numbers) {
                sendErrorMessage();
            }
            if (SystemSettingModel.getPasswordGenerationMinSpecialCharacters() > specialChar) {
                sendErrorMessage();
            }
        }
    }

    private static void sendErrorMessage() throws EndlosiotAPIException {
        throw new EndlosiotAPIException(ResponseCode.PASSWORD_IS_INVALID.getCode(),
                ResponseCode.PASSWORD_IS_INVALID.getMessage());
    }

    public static void isValid(UserView userView) throws EndlosiotAPIException {
        /*Validator.STRING.isValid(new InputField("NAME", userView.getName(), true, 100, RegexEnum.ALPHABETS_WITH_SPACE_DOT));

        Validator.STRING.isValid(new InputField("EMAIL", userView.getEmail(), true, 100, RegexEnum.EMAIL));
        Validator.STRING.isValid(new InputField("MOBILE", userView.getMobile(), false, 15, RegexEnum.MOBILE_NUMBER));
        Validator.STRING.isValid(new InputField("ADDRESS", userView.getAddress(), false, 200));
        Validator.STRING.isValid(new InputField("LANDMARK", userView.getLandmark(), false, 100));
        if (userView.getStateView() == null
                || userView.getStateView() == null && userView.getStateView().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.DATA_IS_MISSING.getCode(),
                    "State " + ResponseCode.DATA_IS_MISSING.getMessage());
        }
        if (userView.getCityView() == null
                || userView.getCityView() == null && userView.getCityView().getKey() == null) {
            throw new EndlosiotAPIException(ResponseCode.DATA_IS_MISSING.getCode(),
                    "City " + ResponseCode.DATA_IS_MISSING.getMessage());
        }*/
    }
}
