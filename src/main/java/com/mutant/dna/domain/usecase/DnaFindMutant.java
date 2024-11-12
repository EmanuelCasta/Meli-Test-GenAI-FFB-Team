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
        return valid(dna)
                .flatMap(dnaVerified -> Mono.fromCallable(() -> Mutant.isMutant(dnaVerified)));

    }

    @Override
    public Mono<StatsMutant> statsMutants() {
        return statsMutant.getMutantAndHumans();
    }

    public Mono<String[]> valid(String[] dna){
        return Mono.fromCallable(()->{
            int n = dna.length;
            int numeroTotalDeConsecutivos = 4;

            for (String fila : dna) {
                if (fila.length() != n) {
                    throw new RuntimeException("No cumple la regla N(" + n + ") * N(" + n + ").");
                }

                if (fila.length() < numeroTotalDeConsecutivos) {
                    throw new RuntimeException("No cumple la regla mínima para que haya al menos " + numeroTotalDeConsecutivos + " consecutivos.");
                }

                for (char c : fila.toCharArray()) {
                    if (!Mutant.esLetraPermitida(c)) {
                        throw new RuntimeException("La secuencia contiene caracteres no permitidos. Solo se permiten las letras A, T, C, G. Valor encontrado "+c);
                    }
                }
            }
            return dna;
        });
    }
}
