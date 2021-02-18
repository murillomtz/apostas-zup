package com.mtz.apostaszup.controller;


import com.mtz.apostaszup.config.SwaggerConfig;
import com.mtz.apostaszup.dto.UserDTO;

import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.model.Response;

import com.mtz.apostaszup.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = SwaggerConfig.USER)
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;

    }
    @ApiOperation(value = "Cadastrar um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário criado com sucesso"),
            @ApiResponse(code = 400, message = "Erro na requisição enviada pelo cliente"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @PostMapping
    public ResponseEntity<Response<Boolean>> cadastrarUser(@Valid @RequestBody UserDTO user) {

        Response<Boolean> response = new Response<>();

        response.setData(userService.cadastrar(user));
        response.setStatusCode(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @ApiOperation(value = "Listar todos os usuários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de usuário exibida com sucesso"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @GetMapping
    public ResponseEntity<Response<List<UserDTO>>> listarUser() {
        Response<List<UserDTO>> response = new Response<>();
        response.setData(this.userService.listar());
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiOperation(value = "Consultar usuário por email")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuário encontrado com sucesso"),
            @ApiResponse(code = 404, message = "Usuário não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno no serviço"),
    })
    @GetMapping(value = "/e-mail/buscarporemail")
    @ResponseBody
    public ResponseEntity<Response<UserDTO>> consultarUserPorEmail(@RequestParam(value = "email") String email) {

        Response<UserDTO> response = new Response<>();
        response.setData(this.userService.findByEmail(email));
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

