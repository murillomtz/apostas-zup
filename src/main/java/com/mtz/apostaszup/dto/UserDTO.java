package com.mtz.apostaszup.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = false)
public class UserDTO {


    private Long id;

    @NotBlank(message = "Informe o email")
    private String email;

    @NotBlank(message = "Informe o nome")
    private String nome;

    private List<Long> apostas;
}
