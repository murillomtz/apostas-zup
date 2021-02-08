package com.mtz.apostaszup.service;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;

import java.util.List;

public interface IUserService {

    Boolean cadastrar(UserDTO userDTO);

    Boolean excluir(Long id);

    UserEntity findById(Long id);

    List<UserEntity> listar();

    UserEntity findByEmail(String email);
}
