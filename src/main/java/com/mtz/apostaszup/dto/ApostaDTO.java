package com.mtz.apostaszup.dto;


import com.mtz.apostaszup.entity.UserEntity;
import com.sun.istack.NotNull;
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

    @NotNull
    private LocalDate data;


    private Set<Integer> numerosAleatorios = new HashSet<>();
}
