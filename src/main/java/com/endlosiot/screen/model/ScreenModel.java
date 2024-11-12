package com.endlosiot.screen.model;

import com.endlosiot.common.model.ActivationModel;
import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity(name = "screenModel")
@Table(name = "screen")
@EqualsAndHashCode(callSuper = true)
@Data
public class ScreenModel extends IdentifierModel {

    @Serial
    private static final long serialVersionUID = 6510352715534906544L;

    @Column(name = "screenname", nullable = false, length = 100)
    private String screenName;

    @Column(name = "titletext", length = 100)
    private String titleText;

    @Column(name = "screendesc", length = 500)
    private String screenDesc;

    @Column(name = "rownumber")
    private Integer rowNumber;

    @Column(name = "colnumber")
    private Integer colNumber;
}
