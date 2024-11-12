package com.endlosiot.devicediagnosis.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.device.model.DeviceModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "deviceDiagnosisModel")
@Table(name = "devicediagnosis")
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceDiagnosisModel extends IdentifierModel {

    @ManyToOne
    @JoinColumn(name = "fkdeviceid", referencedColumnName = "pkid")
    DeviceModel deviceModel;

    @Column(name = "jsoncontent", nullable = true, length = 500)
    private String jsoncontent;

    @Column(name = "createdate", nullable = true)
    private Long createdate;

    @Column(name = "receivedate", nullable = true)
    private Long receivedate;

    @Column(name="isalarmcalculated",nullable = true)
    private boolean alarmCalculated;

}
