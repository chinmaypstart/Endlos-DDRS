package com.endlosiot.device.model;

import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "sendDataModel")
@Table(name = "senddata")
@EqualsAndHashCode(callSuper = true)
@Data
public class SendDataModel extends IdentifierModel {
    @Column(name = "isplus", columnDefinition = "boolean default false")
    private boolean plus;

    @Column(name = "isanalog1", columnDefinition = "boolean default false")
    private boolean analog1;

    @Column(name = "isanalog2", columnDefinition = "boolean default false")
    private boolean analog2;

    @Column(name = "ismodbus", columnDefinition = "boolean default false")
    private boolean modbus;
}
