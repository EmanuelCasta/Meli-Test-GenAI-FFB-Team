package com.mutant.dna.domain.port.out;

import com.mutant.dna.domain.model.StatsMutant;
import reactor.core.publisher.Mono;

public interface IStatsMutant {

    Mono<StatsMutant> getMutantAndHumans();
}
