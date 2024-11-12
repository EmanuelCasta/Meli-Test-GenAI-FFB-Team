package com.mutant.dna.infra.controller;

import com.mutant.dna.api.Handler;
import com.mutant.dna.domain.model.StatsMutant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/stats")
public class DnaControllerQuery {

    private final Handler handler;
    public DnaControllerQuery(Handler handler){
        this.handler = handler;
    }

    @GetMapping({"", "/"})
    @ResponseStatus(HttpStatus.OK)
    public Mono<StatsMutant> statsMutantMono() {
        return this.handler.execute()
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e)));

    }
}
