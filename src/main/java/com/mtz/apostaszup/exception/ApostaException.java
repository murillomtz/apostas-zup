package com.mtz.apostaszup.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApostaException extends RuntimeException{

    private static final long serialVersionUID = 8042402636193525393L;

    private final HttpStatus httpStatus;

    public ApostaException(final String mensagem, final HttpStatus httpStatus) {
        super(mensagem);
        this.httpStatus = httpStatus;
    }

}
