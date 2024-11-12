package com.mutant.dna.infra.repository;

import com.mutant.dna.api.dto.MutantCountDTO;
import com.mutant.dna.infra.entity.DnaEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IMutantDnaRepository extends ReactiveCrudRepository<DnaEntity, String> {

    @Query("SELECT is_mutant, COUNT(*) as count FROM tcap_dnamutant GROUP BY is_mutant")
    Flux<MutantCountDTO> countByIsMutantGroup();

    @Query("INSERT INTO tcap_dnamutant (id_mutant, is_mutant) VALUES (:id, :isMutant)")
    Mono<Void> insertNewRecord(@Param("id") String id, @Param("isMutant") Boolean isMutant);
}
