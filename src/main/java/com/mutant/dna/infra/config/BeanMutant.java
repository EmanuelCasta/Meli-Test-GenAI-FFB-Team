package com.mutant.dna.infra.config;

import com.mutant.dna.domain.port.in.IFindMutant;
import com.mutant.dna.domain.port.in.ISaveResult;
import com.mutant.dna.domain.port.out.ISaveDnaMutant;
import com.mutant.dna.domain.port.out.IStatsMutant;
import com.mutant.dna.domain.usecase.DnaFindMutant;
import com.mutant.dna.domain.usecase.DnaRegister;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;

@Configuration
public class BeanMutant {

    @Bean
    public IFindMutant mutant(IStatsMutant mutant){
        return new DnaFindMutant(mutant);
    }

    @Bean
    public ISaveResult saveResult(ISaveDnaMutant saveDnaMutant){
        return new DnaRegister(saveDnaMutant);
    }


}
