package com.endlosiot.screen.model;

import com.endlosiot.common.model.IdentifierModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity(name = "rowModel")
@Table(name = "rowmaster")
@EqualsAndHashCode(callSuper = true)
@Data
public class RowModel extends IdentifierModel {

    /*@Serial
    private static final long serialVersionUID = 6510352715534906544L;*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pkid", nullable = false)
    private Long id;

    @Column(name = "rowname", nullable = false, length = 100)
    private String rowName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkscreenid", referencedColumnName = "pkid")
    ScreenModel screenModel;
}
