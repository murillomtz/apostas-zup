package com.mtz.apostaszup.service;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IApostaService {

    Boolean cadastrar(ApostaDTO apostaDTO);

    Boolean excluir(Long id);

    ApostaEntity findById(Long id);

    List<ApostaEntity> listar();

    List<ApostaEntity> listaPorData(String email);
}