package com.endlosiot.screen.view;

import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.user.enums.RoleTypeEnum;
import com.endlosiot.common.user.model.RoleModel;
import com.endlosiot.common.user.view.RoleModuleRightsView;
import com.endlosiot.common.user.view.RoleView;
import com.endlosiot.common.validation.InputField;
import com.endlosiot.common.validation.Validator;
import com.endlosiot.common.view.ArchiveView;
import com.endlosiot.common.view.KeyValueView;
import com.endlosiot.screen.model.ScreenModel;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@NonNull
public class ScreenView extends ArchiveView {

    private static final long serialVersionUID = 5678365620021239583L;
    private String screenName;
    private String titleText;
    private String screenDesc;
    private Integer rowNumber;
    private Integer colNumber;

    public ScreenView(ScreenViewBuilder screenViewBuilder) {
        this.setId(screenViewBuilder.id);
        this.screenName = screenViewBuilder.screenName;
        this.titleText = screenViewBuilder.titleText;
        this.screenDesc = screenViewBuilder.screenDesc;
        this.rowNumber = screenViewBuilder.rowNumber;
        this.colNumber = screenViewBuilder.colNumber;
    }


    @JsonPOJOBuilder(withPrefix = "set")
    public static class ScreenViewBuilder {
        private Long id;
        private String screenName;
        private String titleText;
        private String screenDesc;
        private Integer rowNumber;
        private Integer colNumber;
        public ScreenViewBuilder() {
        }
        public ScreenView.ScreenViewBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ScreenView.ScreenViewBuilder setScreenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public ScreenView.ScreenViewBuilder setTitleText(String titleText) {
            this.titleText = titleText;
            return this;
        }
        public ScreenView.ScreenViewBuilder setScreenDesc(String screenDesc) {
            this.screenDesc = screenDesc;
            return this;
        }
        public ScreenView.ScreenViewBuilder setRowNumber(Integer rowNumber) {
            this.rowNumber = rowNumber;
            return this;
        }
        public ScreenView.ScreenViewBuilder setColNumber(Integer colNumber) {
            this.colNumber = colNumber;
            return this;
        }

        public ScreenView build() {
            return new ScreenView(this);
        }
    }
    public void setViewList(Set<ScreenModel> screenModels, List<ScreenView> screenViews) {
        for (ScreenModel screenModel : screenModels) {
            screenViews.add(setView(screenModel));
        }
    }
    public static ScreenView setView(ScreenModel screenModel) {
        ScreenView.ScreenViewBuilder builder = new ScreenViewBuilder()
                .setId(screenModel.getId())
                .setScreenName(screenModel.getScreenName());

        return builder.build();
    }
    public static void isValid(ScreenView deviceView) throws EndlosiotAPIException {
        Validator.STRING.isValid(new InputField("screenname", deviceView.getScreenName(), true, 100));
        Validator.STRING.isValid(new InputField("titletext", deviceView.getTitleText(), true, 100));
    }
}
