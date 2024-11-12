package com.mutant.dna.domain.port.in;

import com.mutant.dna.domain.model.StatsMutant;
import reactor.core.publisher.Mono;

public interface IFindMutant {

    Mono<Boolean> isMutant(String dna[]);

    Mono<StatsMutant> statsMutants();
}
