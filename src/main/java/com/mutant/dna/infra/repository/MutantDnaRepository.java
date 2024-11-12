package com.mutant.dna.infra.repository;

import com.mutant.dna.api.dto.MutantCountDTO;
import com.mutant.dna.domain.model.StatsMutant;
import com.mutant.dna.domain.port.out.ISaveDnaMutant;
import com.mutant.dna.domain.port.out.IStatsMutant;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MutantDnaRepository implements ISaveDnaMutant, IStatsMutant {

    private final IMutantDnaRepository dnaRepository;

    public MutantDnaRepository(IMutantDnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    @Override
    public Mono<Void> saveResult(String hashId, boolean isMutant) {
        return dnaRepository.findById(hashId)
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.empty();
                    }
                    return dnaRepository.insertNewRecord(hashId, isMutant);
                })
                .then();
    }


    @Override
    public Mono<StatsMutant> getMutantAndHumans() {
        return dnaRepository.countByIsMutantGroup()
                .collectList()
                .map(mutantInformatios->{
                    int countMutantDna = 0;
                    int countHumanDna = 0;

                    for (MutantCountDTO result: mutantInformatios) {
                        if (Boolean.TRUE.equals(result.getIsMutant())) {
                            countMutantDna += result.getCount().intValue();
                            countHumanDna += countMutantDna;
                        } else {
                            countHumanDna += result.getCount().intValue();
                        }
                    }

                    return new StatsMutant(countHumanDna, countMutantDna);
                });
    }
}
