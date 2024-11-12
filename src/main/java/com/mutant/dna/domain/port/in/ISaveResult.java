package com.mutant.dna.domain.port.in;

import reactor.core.publisher.Mono;

public interface ISaveResult {
    Mono<Void> saveResult(String[] dna, boolean isMutant);
}
