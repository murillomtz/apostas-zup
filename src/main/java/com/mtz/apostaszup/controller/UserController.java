package com.mtz.apostaszup.controller;


import com.mtz.apostaszup.dto.UserDTO;

import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.model.Response;

import com.mtz.apostaszup.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;

    }

    @PostMapping
    public ResponseEntity<Response<Boolean>> cadastrarUser(@Valid @RequestBody UserDTO user) {

        Response<Boolean> response = new Response<>();

        response.setData(userService.cadastrar(user));
        response.setStatusCode(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


    @GetMapping
    public ResponseEntity<Response<List<UserEntity>>> listarUser() {
        Response<List<UserEntity>> response = new Response<>();
        response.setData(this.userService.listar());
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Response<UserEntity>> consultarUserPorEmail(@PathVariable String email) {
        Response<UserEntity> response = new Response<>();
        response.setData(this.userService.consultarPorEmail(email));
        response.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
