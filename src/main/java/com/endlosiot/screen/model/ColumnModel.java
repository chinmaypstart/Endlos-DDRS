package com.endlosiot.screen.model;

import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity(name = "columnModel")
@Table(name = "columnmaster")
@EqualsAndHashCode(callSuper = true)
@Data
public class ColumnModel extends IdentifierModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid", nullable = false)
    private Long id;

    @Column(name = "columnname", nullable = false, length = 100)
    private String columnName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkscreenid", referencedColumnName = "pkid")
    private ScreenModel screenModel;
}

