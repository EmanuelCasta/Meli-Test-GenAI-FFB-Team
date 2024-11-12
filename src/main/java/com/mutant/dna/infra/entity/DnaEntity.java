package com.mutant.dna.infra.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Setter
@Table("tcap_dnamutant")
public class DnaEntity {

    @Id
    @Column("id_mutant")
    private String idMutant;

    @Column("is_mutant")
    private Boolean isMutant;


}