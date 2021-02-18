package com.mtz.apostaszup.controller;

import com.mtz.apostaszup.config.SwaggerConfig;
import com.mtz.apostaszup.constant.HyperLinkConstant;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.service.IApostaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = SwaggerConfig.APOSTA)
@RestController
@RequestMapping("/aposta")
public class ApostaController {

    private final IApostaService apostaService;

    public ApostaController(IApostaService apostaService) {
        this.apostaService = apostaService;

    }

    @ApiOperation(value = "Cadastrar uma novo aposta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Aposta criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
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

    @ApiOperation(value = "Listar todos as aposta cadastradas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de aposta exibida com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @GetMapping
    public ResponseEntity<Response<List<ApostaDTO>>> listarAposta() {
        Response<List<ApostaDTO>> response = new Response<>();
        response.setData(this.apostaService.listar());
        response.setStatusCode(HttpStatus.OK.value());
        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .listarAposta()).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Consultar aposta por email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aposta encontrada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @GetMapping("/e-mail/{email}")
    public ResponseEntity<Response<List<ApostaDTO>>> listarApostaPorEmail(@RequestParam(value = "email") String email) {

        Response<List<ApostaDTO>> response = new Response<>();
        response.setData(this.apostaService.listaPorData(email));
        response.setStatusCode(HttpStatus.OK.value());
        response.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).listarApostaPorEmail(email))
                .withSelfRel());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Consultar aposta por id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aposta encontrada com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrada"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response<ApostaDTO>> consultarAposta(@PathVariable Long id) {

        Response<ApostaDTO> response = new Response<>();
        ApostaDTO materia = this.apostaService.findById(id);

        response.setData(materia);
        response.setStatusCode(HttpStatus.OK.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .consultarAposta(id)).withSelfRel());
        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .excluirAposta(id)).withRel(HyperLinkConstant.EXCLUIR.getValor()));

        //response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
        //.atualizarMateria(materia)).withRel(HyperLinkConstant.ATUALIZAR.getValor()));

        return ResponseEntity.status(HttpStatus.OK).body(response);

    }


    @ApiOperation(value = "Excluir um aposta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Aposta excluído com sucesso"),
            @ApiResponse(code = 404, message = "Aposta não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @DeleteMapping(value = "/{idDelete}")
    public ResponseEntity<Response<Boolean>> excluirAposta(@PathVariable Long idDelete) {
        Response<Boolean> response = new Response<>();
        response.setData(this.apostaService.excluir(idDelete));
        response.setStatusCode(HttpStatus.OK.value());

        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).
                excluirAposta(idDelete))
                .withSelfRel());
        response.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class)
                .listarAposta()).withRel(HyperLinkConstant.LISTAR.getValor()));

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}