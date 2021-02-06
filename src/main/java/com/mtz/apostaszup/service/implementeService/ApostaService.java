package com.mtz.apostaszup.service.implementeService;


import com.mtz.apostaszup.constant.MensagensConstant;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.exception.ApostaException;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.service.IApostaService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "aposta")
@Service
public class ApostaService implements IApostaService {

    private IApostaRepository apostaRepository;

    public ApostaService(IApostaRepository apostaRepository) {
        this.apostaRepository = apostaRepository;
    }

    @Override
    public Boolean cadastrar(ApostaDTO apostaDTO) {
        try {
            /*
             * O id não pode ser informado no cadastro
             */

            if (apostaDTO.getId() != null) {
                throw new ApostaException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }

            /*
             * Não permite fazer cadastro de cursos com mesmos códigos.
             */
            if (this.apostaRepository.findById(apostaDTO.getId()) != null) {
                throw new ApostaException(MensagensConstant.ERRO_APOSTA_CADASTRADO_ANTERIORMENTE.getValor(), HttpStatus.BAD_REQUEST);
            }
            return this.cadastrar(apostaDTO);

        }catch (ApostaException c) {
            throw c;
        }
        catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean excluir(Long id) {
        try {
            if(this.apostaRepository.findById(id).isPresent()) {
                this.apostaRepository.deleteById(id);
                return Boolean.TRUE;
            }
            throw new ApostaException(MensagensConstant.ERRO_APOSTA_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        }catch (ApostaException c) {
            throw c;
        }catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(key = "#id")
    @Override
    public ApostaEntity findById(Long id) {
        try {
            ApostaEntity curso = this.apostaRepository.findById(id).get();


            if (curso == null) {
                throw new ApostaException(MensagensConstant.ERRO_APOSTA_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
            }

            return curso;

        } catch (ApostaException c) {
            throw c;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //@CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaEntity> listar() {
        try {
            return this.apostaRepository.findAll();
        }catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //@CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaEntity> listaPorData(String email) {
        try {
            return this.apostaRepository.listaPorData(email);
        }catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
