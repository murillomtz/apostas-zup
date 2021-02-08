package com.mtz.apostaszup.dto;


import com.mtz.apostaszup.entity.UserEntity;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class ApostaDTO extends RepresentationModel<ApostaDTO> {

    private Long id;


    private UserEntity user;


    private LocalDate data;


    private Set<Long> numerosAleatorios = new HashSet<>();

    public ApostaDTO() {

    }

    public ApostaDTO(Long id, UserEntity user, LocalDate data) {

        this.id = id;
        this.user = user;
        this.data = LocalDate.now();
        Random rd = new Random();
        getNumerosAleatorios().forEach(num -> {
            long randon = rd.nextLong();
            if (randon < 60 && numerosAleatorios.size() < 6)
                getNumerosAleatorios().add(randon);
        });
    }

    public ApostaDTO(Long id, UserEntity user, LocalDate data, Set<Long> numerosAleatorios) {
        this.id = id;
        this.user = user;
        this.data = data;
        this.numerosAleatorios = numerosAleatorios;
    }

}
