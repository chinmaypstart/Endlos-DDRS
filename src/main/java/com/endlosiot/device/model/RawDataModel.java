package com.endlosiot.device.model;

import com.endlosiot.common.model.Model;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity(name = "RawDataModel")
@Table(name = "rawdata")
@Data
public class RawDataModel implements Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkdeviceid",nullable =false)
    private DeviceModel deviceModel;

    @Column(name = "flow", nullable = true)
    private BigDecimal flow;

    @Column(name = "chlorine", nullable = true)
    private BigDecimal cholrine;

    @Column(name = "turbidity", nullable = true)
    private BigDecimal turbidity;

    @Column(name = "levelsensor", nullable = true)
    private BigDecimal levelSensor;

    @Column(name = "pressuretransmitter", nullable = true)
    private BigDecimal pressureTransmitter;

    @Column(name = "digitaloutput", nullable = true)
    private BigDecimal digitalOutput;

    @Column(name = "createdate", nullable = false)
    private Long createDate;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public BigDecimal getFlow() {
        return flow;
    }

    public void setFlow(BigDecimal flow) {
        this.flow = flow;
    }

    public BigDecimal getCholrine() {
        return cholrine;
    }

    public void setCholrine(BigDecimal cholrine) {
        this.cholrine = cholrine;
    }

    public BigDecimal getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(BigDecimal turbidity) {
        this.turbidity = turbidity;
    }

    public BigDecimal getLevelSensor() {
        return levelSensor;
    }

    public void setLevelSensor(BigDecimal levelSensor) {
        this.levelSensor = levelSensor;
    }

    public BigDecimal getPressureTransmitter() {
        return pressureTransmitter;
    }

    public void setPressureTransmitter(BigDecimal pressureTransmitter) {
        this.pressureTransmitter = pressureTransmitter;
    }

    public BigDecimal getDigitalOutput() {
        return digitalOutput;
    }

    public void setDigitalOutput(BigDecimal digitalOutput) {
        this.digitalOutput = digitalOutput;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }
}