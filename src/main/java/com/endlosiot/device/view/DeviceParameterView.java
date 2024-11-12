package com.endlosiot.device.view;

import com.endlosiot.common.view.IdentifierView;
import com.endlosiot.parametermaster.view.ParameterMasterView;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @author chetanporwal
 * @since 06/09/2023
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties
public class DeviceParameterView extends IdentifierView {

    private ParameterMasterView parameterMasterView;
    private String parameterName;
    private String jsonCode;
    private String registerName;
    private String address;
}
