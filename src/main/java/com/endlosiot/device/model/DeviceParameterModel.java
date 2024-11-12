package com.endlosiot.device.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.parametermaster.model.ParameterMasterModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author milangohil
 * @since 26/06/2024
 */

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "deviceParameterModel")
@Table(name = "deviceparameters")
public class DeviceParameterModel extends IdentifierModel {

    @ManyToOne
    @JoinColumn(name = "fkdeviceid", referencedColumnName = "pkid")
    DeviceModel deviceModel;

    @ManyToOne
    @JoinColumn(name = "fkparameterid", referencedColumnName = "pkid")
    ParameterMasterModel parameterMasterModel;

    @Column(name = "registername", nullable = false, length = 500)
    private String registerName;

    @Column(name = "address", length = 20)
    private String address;

    @Column(name="isneedtoalarmlogged",nullable = true)
    private boolean needToLogAlarm;

    public DeviceParameterModel(Long id) {
        super(id);
    }
}
