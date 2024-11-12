package com.endlosiot.parametermaster.view;

import com.endlosiot.common.view.ArchiveView;
import lombok.*;

/**
 * @author chetanporwal
 * @since 29/08/2023
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ParameterMasterView extends ArchiveView {
    private String name;
    private String jsonCode;
    private String paramUnit;
}
