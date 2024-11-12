package com.mutant.dna.infra.controller;

import com.mutant.dna.api.ExceptionMutant;
import com.mutant.dna.api.Handler;
import com.mutant.dna.api.dto.DnaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mutant")
public class DnaControllerCommand {
    private final Handler handler;
    public DnaControllerCommand(Handler handler){
        this.handler = handler;
    }

    @PostMapping({"", "/"})
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> processDna(@RequestBody DnaDTO dnaDTO) {
        return this.handler.execute(dnaDTO)
                .onErrorResume(e -> {
                    if (e instanceof ExceptionMutant) {
                        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN));
                    }
                    return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e));
                });

    }
}