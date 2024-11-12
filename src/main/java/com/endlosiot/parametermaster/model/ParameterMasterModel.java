package com.endlosiot.parametermaster.model;


import com.endlosiot.common.model.ArchiveModel;
import com.endlosiot.common.model.IdentifierModel;
import com.endlosiot.common.user.model.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author chetanporwal
 * @since 29/08/2023
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity(name = "parameterMasterModel")
@Table(name = "parametermapping")
public class ParameterMasterModel extends IdentifierModel {

    @Column(name = "name", nullable = false, length = 500, unique = true)
    private String name;

    @Column(name = "jsoncode", nullable = false, length = 100, unique = true)
    private String jsonCode;

    @Column(name = "parameterunit", nullable = false, length = 100)
    private String parameterUnit;
    
    @Column(name = "datearchive", nullable = true)
    private Long archiveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkarchiveby")
    private UserModel archiveBy;
}
