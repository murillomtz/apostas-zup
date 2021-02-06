package com.mtz.apostaszup.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDTO {


    private Long id;

    @NotBlank(message = "Informe o email")
    private String email;

    @NotBlank(message = "Informe o nome")
    private String nome;

    private List<ApostaEntity> apostas = new ArrayList<>();
}
