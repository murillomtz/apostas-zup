package com.mtz.apostaszup.repository;

import com.mtz.apostaszup.entity.ApostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IApostaRepository extends JpaRepository<ApostaEntity, Long> {

    //List<Aposta> listaPorData(String email);
}
