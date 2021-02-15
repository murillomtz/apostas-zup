package com.mtz.apostaszup.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class Response<T> extends RepresentationModel<Response<T>> {

    private int statusCode;
    private T data;
    private long timeStamp;
    //private String CharacterEncoding;

    public Response(){
        this.timeStamp = System.currentTimeMillis();
    }

}
