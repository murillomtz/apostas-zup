package com.mtz.apostaszup.service.implementeService;


import com.mtz.apostaszup.constant.MensagensConstant;
import com.mtz.apostaszup.controller.ApostaController;
import com.mtz.apostaszup.controller.UserController;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.exception.ApostaException;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import com.mtz.apostaszup.service.IApostaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CacheConfig(cacheNames = "aposta")
@Service
public class ApostaService implements IApostaService {

    private IApostaRepository apostaRepository;
    private IUserRepository userRepository;
    private UserController userController;
    private ModelMapper mapper;

    @Autowired
    public ApostaService(IApostaRepository apostaRepository, IUserRepository userRepository,UserController userController) {
        this.mapper = new ModelMapper();
        this.apostaRepository = apostaRepository;
        this.userRepository = userRepository;
        this.userController= userController;
    }

    @Override
    public Boolean cadastrar(ApostaDTO apostaDTO) {
        System.out.println("### cadatrar #####"+ apostaDTO);
        try {
            /*
             * O id não pode ser informado no cadastro
             */

            if (apostaDTO.getId() != null) {
                System.out.println("### tem id #####");
                throw new ApostaException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }

            /*
             * Não permite fazer cadastro de cursos com mesmos códigos.
             */
            /*if (this.apostaRepository.findById(apostaDTO.getId()) != null) {
                throw new ApostaException(MensagensConstant.ERRO_APOSTA_CADASTRADO_ANTERIORMENTE.getValor(), HttpStatus.BAD_REQUEST);
            }*/
            return this.cadastrarOuAtualizar(apostaDTO);

        } catch (ApostaException c) {
            throw c;
        } catch (Exception e) {
            System.out.println("###Erro generico #####");
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean excluir(Long id) {
        try {
            if (this.apostaRepository.findById(id).isPresent()) {
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


    @CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaDTO> listar() {
        try {
            List<ApostaDTO> apostaDto = this.mapper.map(this.apostaRepository.findAll(),
                    new TypeToken<List<ApostaDTO>>() {
                    }.getType());

            apostaDto.forEach(aposta ->
                    aposta.add(WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).consultarAposta(aposta.getId()))
                            .withSelfRel())
            );

            return apostaDto;

        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @CachePut(unless = "#result.size()<3")
    @Override
    public List<ApostaDTO> listaPorData(String email) {
        try {

            List<ApostaDTO> apostasDto = this.mapper.map(this.apostaRepository.findAll(),
                    new TypeToken<List<ApostaDTO>>() {
                    }.getType());


            apostasDto.forEach(materia ->
                    materia.add(WebMvcLinkBuilder
                            .linkTo(WebMvcLinkBuilder.methodOn(ApostaController.class).consultarAposta(materia.getId()))
                            .withSelfRel())
            );

            return apostasDto;
        } catch (Exception e) {
            throw new ApostaException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean cadastrarOuAtualizar(ApostaDTO apostaDTO) {
        System.out.println("###PASSOU CADASTRAR OU ATT #####"+ apostaDTO);
        Set<Long> listApostaEntity = new HashSet<>();

        if (apostaDTO.getNumerosAleatorios() != null && !apostaDTO.getNumerosAleatorios().isEmpty()) {
            System.out.println("###TEM LISTA #####");
            System.out.println("###TEM LISTA" + apostaDTO.getNumerosAleatorios() );
            listApostaEntity = apostaDTO.getNumerosAleatorios();
        }
        System.out.println("######## USER " + userRepository.findByEmail(apostaDTO.getUser().getEmail()));
        ApostaEntity apostaEntity = new ApostaEntity(null,userRepository.findByEmail(apostaDTO.getUser().getEmail()));
        if (apostaDTO.getId() != null) {
            System.out.println("###TEM ID #####");
            apostaEntity.setId(apostaDTO.getId());
        }
       // System.out.println("######## USER " + userRepository.findByEmail(apostaDTO.getUser().getEmail()));
       // apostaEntity.setUser(userRepository.findByEmail(apostaDTO.getUser().getEmail()));

        System.out.println("######## DATA " + apostaDTO.getData());
        apostaEntity.setData(apostaDTO.getData());

        System.out.println("######## LISTA " + listApostaEntity);
        apostaEntity.setNumerosAleatorios(listApostaEntity);
        System.out.println("######## APOSTA " + apostaEntity);
        this.apostaRepository.save(apostaEntity);

        return Boolean.TRUE;
    }


}
