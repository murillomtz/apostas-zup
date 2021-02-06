package com.mtz.apostaszup.dto;


import com.mtz.apostaszup.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;



import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class ApostaDTO {

    private Long id;


    private UserEntity user;


    private LocalDate data;


    private Set<Integer> numerosAleatorios = new HashSet<>();
}
