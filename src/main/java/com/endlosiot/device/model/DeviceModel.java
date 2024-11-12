package com.endlosiot.device.model;

import com.endlosiot.common.model.ActivationModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity(name = "deviceModel")
@Table(name = "device")
@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceModel extends ActivationModel {

    @Serial
    private static final long serialVersionUID = 6510352715534906544L;

    @Column(name = "name", nullable = false, length = 500)
    private String name;

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkclientid", nullable = false)
    private ClientModel clientModel;*/


    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fklocationid")
    private LocationModel locationModel;*/

    @Column(name = "imei", length = 100)
    private String imei;
}
