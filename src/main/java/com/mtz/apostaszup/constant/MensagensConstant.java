package com.mtz.apostaszup.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensagensConstant {

    ERRO_GENERICO("Erro interno identificado. Contate o suporte."),

    ERRO_APOSTA_CADASTRADO_ANTERIORMENTE("Aposta já possui cadastro."),

    ERRO_USER_NAO_ENCONTRADO("Usuario não encontrado."),

    ERRO_USER_CADASTRADO_ANTERIORMENTE("Usuario já possui cadastro."),

    ERRO_APOSTA_NAO_ENCONTRADO("Aposta não encontrado."),

    ERRO_ID_INFORMADO("ID não pode ser informado na operação de cadastro.");

    private final String valor;
}
