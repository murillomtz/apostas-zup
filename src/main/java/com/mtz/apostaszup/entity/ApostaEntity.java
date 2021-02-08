package com.mtz.apostaszup.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.annotations.Cascade;

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

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_email", referencedColumnName = "email")
	@JsonIgnore
	private UserEntity user;

	//@JsonInclude(JsonInclude.Include.NON_EMPTY)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate data;

	@ElementCollection
	private Set<Long> numerosAleatorios = new HashSet<>();

	public ApostaEntity() {

	}

	public ApostaEntity(Long id, UserEntity user) {
		this.id = id;
		this.user = user;
		this.data = LocalDate.now();
		Random rd = new Random();
		getNumerosAleatorios().forEach(num -> {
			long randon = rd.nextLong();
			if ( randon <60 && numerosAleatorios.size() < 6)
				getNumerosAleatorios().add(randon);
		});


	}
}
