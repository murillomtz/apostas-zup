package com.mtz.apostaszup.handler;

import com.mtz.apostaszup.exception.ApostaException;
import com.mtz.apostaszup.exception.UserException;
import com.mtz.apostaszup.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResourceHandler {
    /**
     * Quando chamar a exception MateriaException ele dará esse tratamento
     */
    @ExceptionHandler(ApostaException.class)
    public ResponseEntity<Response<String>> handlerMateriaException(ApostaException m) {
        Response<String> response = new Response<>();


        response.setStatusCode(m.getHttpStatus().value());
        response.setData(m.getMessage());

        return ResponseEntity.status(m.getHttpStatus()).body(response);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Map<String, String>>> handlerMethodArgumentNotValidException(MethodArgumentNotValidException m) {
        Map<String, String> erros = new HashMap<>();

        /*Busca erro e salva o fild e a mensagem de erro*/
        m.getBindingResult().getAllErrors().forEach(erro -> {
            String campo = ((FieldError) erro).getField();
            String mensagem = erro.getDefaultMessage();
            erros.put(campo, mensagem);
        });

        Response<Map<String, String>> response = new Response<>();

        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        response.setData(erros);

        return ResponseEntity.status(HttpStatus.MULTI_STATUS.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Response<String>> handlerCursoException(UserException m) {
        Response<String> response = new Response<>();
        response.setStatusCode(m.getHttpStatus().value());
        response.setData(m.getMessage());
        return ResponseEntity.status(m.getHttpStatus()).body(response);

    }

}
