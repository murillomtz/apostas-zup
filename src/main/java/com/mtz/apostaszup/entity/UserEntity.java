package com.mtz.apostaszup.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "tb_user")
@Data
@NoArgsConstructor
public class UserEntity implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(generator = "incrementacao") //Passa para o JPA a responsabilidade de dar um ID
    @GenericGenerator(name = "incrementacao", strategy = "increment")
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(unique = true,name = "email")
    private String email;

    private String nome;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "userEntity")
    private List<ApostaEntity> apostas = new ArrayList<>();
}
