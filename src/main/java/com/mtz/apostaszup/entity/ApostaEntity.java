package com.mtz.apostaszup.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "tb_apostas")
@Data
public class ApostaEntity implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private UserEntity user;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate data;

    @Column(name = "numeros")
    private List<Integer> numeros = new ArrayList<>();


    public ApostaEntity() {

    }


    public ApostaEntity(Long id, UserEntity user) {
        this.id = id;
        this.user = user;
        this.data = LocalDate.now();
       /* for (int i = 0; i < 7; i++) {
            this.numeros.set(i, i + 80);
        }
        for (int i = 0; i < 7; i++) {
            this.numeros.set(1, randomizandoNumeros());
        }*/


    }

    public Integer randomizandoNumeros() {

        Random random = new Random();
        Integer valorRandomico = null;

        for (int i = 0; i < 5; i++) {
            do {
                valorRandomico = random.nextInt(60) + 1;
            } while (valorRandomico == this.numeros.get(0) || valorRandomico == this.numeros.get(1)
                    || valorRandomico == this.numeros.get(2)
                    || valorRandomico == this.numeros.get(3) || valorRandomico == this.numeros.get(4)
                    || valorRandomico == this.numeros.get(5)) ;
        }
        return valorRandomico;
    }
}
