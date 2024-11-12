package com.endlosiot.device.model;

import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity(name = "deviceDataModel")
@Table(name = "devicedata")
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDataModel extends IdentifierModel {
    @Column(name = "plus", nullable = true)
    private BigDecimal plus;
    @Column(name = "analog1", nullable = true)
    private BigDecimal analog1;
    @Column(name = "analog2", nullable = true)
    private BigDecimal analog2;
    @Column(name = "turbodity", nullable = true)
    private BigDecimal turbodity;
    @Column(name = "chlorine", nullable = true)
    private BigDecimal chlorine;
    @Column(name = "datecreate", nullable = true)
    private Long dateCreate;
    @Column(name = "type", nullable = true)
    private Long type;
}
