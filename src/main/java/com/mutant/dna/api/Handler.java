package com.mutant.dna.api;


import com.mutant.dna.domain.model.StatsMutant;
import com.mutant.dna.domain.port.in.IFindMutant;
import com.mutant.dna.domain.port.in.ISaveResult;
import com.mutant.dna.api.dto.DnaDTO;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class Handler {

    private final IFindMutant findMutant;
    private final ISaveResult saveResult;

    public Handler(IFindMutant findMutant, ISaveResult saveResult) {
        this.findMutant = findMutant;
        this.saveResult = saveResult;
    }

    public Mono<Void> execute(DnaDTO dna){
        return findMutant.isMutant(dna.getDna())
                .flatMap(isMutant -> {
                    saveResult.saveResult(dna.getDna(),isMutant).subscribe();

                    if (!isMutant) {
                        return Mono.error(new ExceptionMutant());
                    }

                    return Mono.empty();
                });

    }


    public Mono<StatsMutant> execute(){
        return findMutant.statsMutants();
    }
}
