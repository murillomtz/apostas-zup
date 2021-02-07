package com.mtz.apostaszup.repository;

import com.mtz.apostaszup.entity.ApostaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface IApostaRepository extends JpaRepository<ApostaEntity, Long> {

    @Query("SELECT a FROM ApostaEntity a where a.user.email = :email ORDER BY a.data")
    List<ApostaEntity> listaPorData(@Param("email") String email);

}
