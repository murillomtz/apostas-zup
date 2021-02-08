package com.mtz.apostaszup.controller;

import com.mtz.apostaszup.constant.HyperLinkConstant;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.service.IApostaService;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).
                cadastrarAposta(aposta))
                .withSelfRel());

        /*response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MateriaController.class)
                .atualizarMateria(materia)).withRel(HyperLinkConstant.ATUALIZAR.getValor()));*/

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .listarAposta()).withRel(HyperLinkConstant.LISTAR.getValor()));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping
    public ResponseEntity<Response<List<ApostaDTO>>> listarAposta() {
        Response<List<ApostaDTO>> response = new Response<>();
        response.setData(this.apostaService.listar());
        response.setStatusCode(HttpStatus.OK.value());
        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .listarAposta()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Response<List<ApostaDTO>>> listarApostaPorEmail(@PathVariable String email) {

        Response<List<ApostaDTO>> response = new Response<>();
        List<ApostaDTO> materia = this.apostaService.listaPorData(email);
        response.setData(materia);
        response.setStatusCode(HttpStatus.OK.value());
        /*response.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).consultaMateriaPorHoraMinima(horaMinima))
                .withSelfRel());*/

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //@GetMapping()
    public ResponseEntity<Response<ApostaDTO>> consultarAposta(@PathVariable Long id) {

        Response<ApostaDTO> response = new Response<>();
        ApostaDTO materia = this.apostaService.findById(id);

        response.setData(this.apostaService.findById(id));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .consultarAposta(id)).withSelfRel());

        //response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
              //  .excluirAposta(id)).withRel(HyperLinkConstant.EXCLUIR.getValor()));

        //response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                //.atualizarMateria(materia)).withRel(HyperLinkConstant.ATUALIZAR.getValor()));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    /*DeleteMapping("/{id}")
    public ResponseEntity<Response<Boolean>> excluirAposta(@PathVariable Long id) {

        Response<Boolean> response = new Response<>();
        response.setData(this.apostaService.excluir(id));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).
                excluirAposta(id))
                .withSelfRel());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .listarAposta()).withRel(HyperLinkConstant.LISTAR.getValor()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }*/
}
