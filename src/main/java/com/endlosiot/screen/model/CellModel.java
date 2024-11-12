package com.endlosiot.screen.model;

import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.device.model.DeviceParameterModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity(name = "cellModel")
@Table(name = "cellmaster")
@EqualsAndHashCode(callSuper = true)
@Data
public class CellModel extends IdentifierModel {

    /*@Serial
    private static final long serialVersionUID = 6510352715534906544L;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid", nullable = false)
    private Long id;

    @Column(name = "cellvalue", nullable = false, length = 100)
    private String cellValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkdeviceparameterid", referencedColumnName = "pkid")
    DeviceParameterModel deviceParameterModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkrowid", referencedColumnName = "pkid")
    RowModel rowModel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkcolumnid", referencedColumnName = "pkid")
    ColumnModel columnModel;

    @ManyToOne
    @JoinColumn(name = "fkscreenid", referencedColumnName = "pkid")
    private ScreenModel screenModel;

    @Column(name = "decimal", nullable = false, length = 100)
    private Long decimal;

    @Column(name = "min", length = 20)
    private Long min;

    @Column(name = "max", length = 20)
    private Long max;

    @Column(name = "function", length = 20)
    private String function;

    @Column(name = "unit",  length = 20)
    private String unit;

    @Column(name = "zerobuttontext")
    private String zeroButtonText;

    @Column(name = "onebuttontext")
    private String oneButtonText;

    @Column(name = "showvaluemessage")
    private Boolean showValueMessage;
}
