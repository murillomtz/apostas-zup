package com.mtz.apostaszup.service;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;


import java.util.List;

public interface IApostaService {

    Boolean cadastrar(ApostaDTO apostaDTO);

    Boolean excluir(Long id);

    ApostaDTO findById(Long id);

    List<ApostaDTO> listar();

    List<ApostaDTO> listaPorData(String email);
}
