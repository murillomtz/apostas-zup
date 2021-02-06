package com.mtz.apostaszup.controller;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.service.IApostaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/aposta")
public class ApostaController {

    private final IApostaService apostaService;

    public ApostaController(IApostaService apostaService) {
        this.apostaService = apostaService;

    }

    @PostMapping
    public ResponseEntity<Response<Boolean>> cadastrarAposta(@Valid @RequestBody ApostaDTO aposta) {

        Response<Boolean> response = new Response<>();

        response.setData(apostaService.cadastrar(aposta));
        response.setStatusCode(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping
    public ResponseEntity<Response<List<ApostaEntity>>> listarAposta() {
        Response<List<ApostaEntity>> response = new Response<>();
        response.setData(this.apostaService.listar());
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Response<ApostaEntity>> consultarApostaPorEmail(@PathVariable String email) {
        Response<ApostaEntity> response = new Response<>();
        response.setData(this.apostaService.consultarPorEmail(email));
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
