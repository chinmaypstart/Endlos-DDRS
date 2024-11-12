package com.endlosiot.gateway.model;

import com.endlosiot.common.model.ActivationModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "gatewayModel")
@Table(name = "gateway")
@EqualsAndHashCode(callSuper = true)
@Data
public class GatewayModel extends ActivationModel {
    @Column(name = "name", nullable = false, length = 500)
    private String name;
    @Column(name = "gatewayid", length = 40)
    private String gatewayId;
    @Column(name = "ipaddress", length = 50)
    private String ipAddress;
    @Column(name = "macaddress", length = 50)
    private String macAddress;
    @Column(name = "latitude",  length = 20)
    private String latitude;
    @Column(name = "longitude", length = 20)
    private String longitude;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fklocationid",nullable = false)
    private LocationModel locationModel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkclientid",nullable =false)
    private ClientModel clientModel;*/
}
