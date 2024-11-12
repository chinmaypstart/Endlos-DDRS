package com.endlosiot.devicediagnosis.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.device.model.DeviceModel;
import com.endlosiot.device.model.DeviceParameterModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "alarmHistoryModel")
@Table(name = "alaramhistory")
@EqualsAndHashCode(callSuper = true)
@Data
public class AlarmHistoryModel extends IdentifierModel {

    @ManyToOne
    @JoinColumn(name = "fkdeviceid", referencedColumnName = "pkid")
    DeviceModel deviceModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkdeviceparameterid", referencedColumnName = "pkid")
    DeviceParameterModel deviceParameterModel;

    @Column(name = "createdate", nullable = true)
    private Long createdate;

    @Column(name = "resolvedate", nullable = true)
    private Long resolvedate;

    @Column(name = "noofcount", nullable = true)
    private Long count;
}
