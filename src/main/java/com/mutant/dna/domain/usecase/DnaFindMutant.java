package com.mutant.dna.domain.usecase;

import com.mutant.dna.domain.model.Mutant;
import com.mutant.dna.domain.model.StatsMutant;
import com.mutant.dna.domain.port.in.IFindMutant;
import com.mutant.dna.domain.port.out.IStatsMutant;
import reactor.core.publisher.Mono;

public class DnaFindMutant implements IFindMutant {

    private IStatsMutant statsMutant;

    public DnaFindMutant(IStatsMutant statsMutant) {
        this.statsMutant = statsMutant;
    }

    @Override
    public Mono<Boolean> isMutant(String[] dna) {
        return Mono.fromCallable(() -> Mutant.isMutant(dna));

    }

    @Override
    public Mono<StatsMutant> statsMutants() {
        return statsMutant.getMutantAndHumans();
    }


}
