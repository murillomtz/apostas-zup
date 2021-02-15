package com.mtz.apostaszup.service;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;


import java.util.List;

public interface IApostaService {

    Boolean cadastrar(ApostaDTO apostaDTO);

    Boolean excluir(Long id);

    ApostaDTO findById(Long id);

    //RETORNA ENTIDADE
    List<ApostaDTO> listar();

    //RETORNA ENTIDADE
    List<ApostaDTO> listaPorData(String email);

    ApostaEntity findByUser(String email);
}
