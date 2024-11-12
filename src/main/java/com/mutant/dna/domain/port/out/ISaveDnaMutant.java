package com.mutant.dna.domain.port.out;

import reactor.core.publisher.Mono;

public interface ISaveDnaMutant {

    Mono<Void> saveResult(String hashId, boolean isMutant);

}
