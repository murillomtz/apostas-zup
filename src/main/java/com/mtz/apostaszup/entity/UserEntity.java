package com.mtz.apostaszup.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.*;


@Entity
@Table(name = "tb_user")
@Getter
@Setter
public class UserEntity implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(generator = "incrementacao")
    @GenericGenerator(name = "incrementacao", strategy = "increment")
    private Long id;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(unique = true,name = "email")
    @Email
    private String email;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String nome;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "user", fetch= FetchType.EAGER,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<ApostaEntity> apostas = new ArrayList<>();


    public UserEntity() {

    }
    public UserEntity(Long id, String email, String nome, List<ApostaEntity> apostas) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.apostas = apostas;
    }

    }
