package com.mtz.apostaszup.service.implementeService;


import com.mtz.apostaszup.constant.MensagensConstant;
import com.mtz.apostaszup.controller.ApostaController;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.exception.ApostaException;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import com.mtz.apostaszup.service.IApostaService;
import jdk.swing.interop.SwingInterOpUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * COMPARAR COM A ENTIDA CURSOS PARA REFAZEZR ESSAS OPERAÇOES
 */
@CacheConfig(cacheNames = "aposta")
@Service
public class ApostaService implements IApostaService {

    private final IApostaRepository apostaRepository;
    private final IUserRepository userRepository;
    private final ModelMapper mapper;

    @Autowired
    public ApostaService(IApostaRepository apostaRepository, IUserRepository userRepository) {
        this.apostaRepository = apostaRepository;
        this.userRepository = userRepository;
        this.mapper = new ModelMapper();
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
            return this.cadastrarOuAtualizar(apostaDTO);

        } catch (ApostaException c) {
            throw c;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean excluir(Long id) {
        try {
            if (this.apostaRepository.findById(id).isPresent()) {
               // System.out.println("Passou aqui ");
                this.apostaRepository.deleteById(id);
                return Boolean.TRUE;
            }
            throw new ApostaException(MensagensConstant.ERRO_APOSTA_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (ApostaException c) {
            throw c;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(key = "#id")
    @Override
    public ApostaDTO findById(Long id) {
        try {

            Optional<ApostaEntity> apostaOptional = this.apostaRepository.findById(id);
            if (apostaOptional.isPresent()) {
                return this.mapper.map(apostaOptional.get(), ApostaDTO.class);
            }
            throw new ApostaException(MensagensConstant.ERRO_APOSTA_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (ApostaException c) {
            throw c;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //@CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaDTO> listar() {
        try {
            List<ApostaEntity> apostaEntityList = this.apostaRepository.findAll();

            List<ApostaDTO> apostaDTOList = new ArrayList<>();

            for (ApostaEntity aposta : apostaEntityList) {
                ApostaDTO apostaDTO = new ApostaDTO();
                apostaDTO.setId(aposta.getId());
                apostaDTO.setData(aposta.getData());
                apostaDTO.setNumerosAleatorios(aposta.getNumerosAleatorios());
                apostaDTO.setUser(aposta.getUser().getEmail());
                apostaDTOList.add(apostaDTO);

            }
            apostaDTOList.forEach(apostaEa -> apostaEa.add(WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).consultarAposta(apostaEa.getId()))
                    .withSelfRel()));

            return apostaDTOList;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaDTO> listaPorData(String email) {
        try {
            System.out.println("################  Pattou listar por data ########################");

            List<ApostaEntity> apostaEntityList = this.apostaRepository.listaPorData(email);


            List<ApostaDTO> apostaDTOList = new ArrayList<>();

            for (ApostaEntity aposta : apostaEntityList) {
                ApostaDTO apostaDTO = new ApostaDTO();
                apostaDTO.setId(aposta.getId());
                apostaDTO.setData(aposta.getData());
                apostaDTO.setNumerosAleatorios(aposta.getNumerosAleatorios());
                apostaDTO.setUser(aposta.getUser().getEmail());
                apostaDTOList.add(apostaDTO);

            }
            System.out.println("apostaEntityList" + apostaEntityList);
            System.out.println("apostaDTOList" + apostaDTOList);
            return apostaDTOList;
        } catch (Exception e) {
            throw e /*new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR)*/;
        }
    }

    @Override
    public ApostaEntity findByUser(String email) {
        return null;
    }

    private Boolean cadastrarOuAtualizar(ApostaDTO apostaDTO) {


        ApostaEntity apostaEntity = null;
        if (apostaDTO.getUser() != null && !apostaDTO.getUser().isEmpty()) {

            String user = apostaDTO.getUser();

            if (userRepository.findByEmail(user) != null) {
                apostaEntity = new ApostaEntity(null, userRepository.findByEmail(user));
            }
            if (apostaDTO.getId() != null) {
                apostaEntity.setId(apostaDTO.getId());
            }
            //if lista vazia

        }
        this.apostaRepository.save(apostaEntity);

        return Boolean.TRUE;
    }
}