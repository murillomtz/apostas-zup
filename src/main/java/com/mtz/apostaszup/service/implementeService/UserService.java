package com.mtz.apostaszup.service.implementeService;


import com.mtz.apostaszup.constant.MensagensConstant;
import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.exception.ApostaException;
import com.mtz.apostaszup.exception.UserException;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import com.mtz.apostaszup.service.IApostaService;
import com.mtz.apostaszup.service.IUserService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@CacheConfig(cacheNames = "user")
@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;
    private IApostaRepository apostaRepository;

    public UserService(IUserRepository userRepository, IApostaRepository apostaRepository) {
        this.userRepository = userRepository;
        this.apostaRepository=apostaRepository;

    }

    @Override
    public Boolean cadastrar(UserDTO userDTO) {
        System.out.println("#######   PASSO CADASTRAR ##########");
        try {
            System.out.println("#######   PASSO TRY ##########");
            if (userDTO.getId() != null) {
                System.out.println("#######   PASSO NULL ##########");
                throw new UserException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);

            }
            if (this.userRepository.findById(userDTO.getId()) != null) {
                System.out.println("#######   PASSO NULL 2 ##########");
                throw new UserException(MensagensConstant.ERRO_USER_CADASTRADO_ANTERIORMENTE.getValor(), HttpStatus.BAD_REQUEST);
            }
            System.out.println("#######   PASSO 1 ##########");
            return this.cadastrarOuAtualizar(userDTO);

        }catch (UserException c) {
            System.out.println("#######   PASSO C ##########");
            throw c;
        }
        catch (Exception e) {
            System.out.println("#######   PASSO AND ##########");
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean excluir(Long id) {
        try {
            if(this.userRepository.findById(id).isPresent()) {
                this.userRepository.deleteById(id);
                return Boolean.TRUE;
            }
            throw new UserException(MensagensConstant.ERRO_USER_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        }catch (UserException c) {
            throw c;
        }catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(key = "#id")
    @Override
    public UserEntity findById(Long id) {
        try {
            UserEntity user = this.userRepository.findById(id).get();


            if (user == null) {
                throw new UserException(MensagensConstant.ERRO_USER_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
            }

            return user;

        } catch (UserException c) {
            throw c;
        } catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserEntity consultarPorEmail(String email) {
        //TODO - Fazer em outro momento
        return null;
    }

    @CachePut(unless = "#result.size()<3")
    @Override
    public List<UserEntity> listar() {
        try {
            return this.userRepository.findAll();
        }catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean cadastrarOuAtualizar(UserDTO user) {
        System.out.println("#######   PASSO CASTRO OU ATT ##########");
        List<ApostaEntity> listApostaEntity = new ArrayList<>();

        if (user.getApostas()!=null && !user.getApostas().isEmpty()) {

            user.getApostas().forEach(aposta -> {
                if (this.apostaRepository.findById(aposta).isPresent())
                    listApostaEntity.add(this.apostaRepository.findById(aposta).get());
            });
        }
        UserEntity userEntity = new UserEntity();
        if(user.getId()!=null) {
            userEntity.setId(user.getId());
        }
        userEntity.setEmail(user.getEmail());
        userEntity.setNome(user.getNome());
        userEntity.setApostas(listApostaEntity);

        this.userRepository.save(userEntity);
        return Boolean.TRUE;
    }
}
