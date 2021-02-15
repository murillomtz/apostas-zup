package com.mtz.apostaszup.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "tb_apostas")
@Getter
@Setter
public class ApostaEntity implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    @JsonIgnore
    private UserEntity user;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate data;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> numerosAleatorios = new HashSet<>();

    public ApostaEntity() {
        this.data = LocalDate.now();
        RadonApostas();
    }

    public ApostaEntity(Long id, UserEntity user) {
        this.id = id;
        this.user = user;
        this.data = LocalDate.now();
        RadonApostas();

    }

    public ApostaEntity(Long id, UserEntity user, LocalDate data, Set<Long> numerosAleatorios) {
        this.id = id;
        this.user = user;
        this.data = data;
        RadonApostas();
    }

    public void RadonApostas() {
        Random rd = new Random();
        while (numerosAleatorios.size() < 6) {
            this.numerosAleatorios.add(Long.valueOf(rd.nextInt(60) + 1));
        }
    }

    @Override
    public String toString() {
        return "ApostaEntity{" +

                ", user=" + user +

                ", numerosAleatorios=" + numerosAleatorios +
                '}';
    }
}


