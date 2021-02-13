package com.mtz.apostaszup.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ApostaDTO extends RepresentationModel<ApostaDTO> {

    private Long id;


    private String user;


    private LocalDate data;


    private Set<Long> numerosAleatorios = new HashSet<>();



}
