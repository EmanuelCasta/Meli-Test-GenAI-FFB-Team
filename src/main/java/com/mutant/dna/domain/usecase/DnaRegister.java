package com.mutant.dna.domain.usecase;

import com.mutant.dna.domain.port.in.ISaveResult;
import com.mutant.dna.domain.port.out.ISaveDnaMutant;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class DnaRegister implements ISaveResult {

    private ISaveDnaMutant saveDnaMutant;

    public DnaRegister(ISaveDnaMutant saveDnaMutant) {
        this.saveDnaMutant = saveDnaMutant;
    }

    @Override
    public Mono<Void> saveResult(String[] dna, boolean isMutant) {
        return generateHash(dna)
                .flatMap(hashId -> saveDnaMutant.saveResult(hashId,isMutant));
    }

    private Mono<String> generateHash(String[] dna){
        return Mono.fromCallable(()->{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(Arrays.toString(dna).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        });
    }
}
