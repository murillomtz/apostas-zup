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

import java.util.List;

@CacheConfig(cacheNames = "user")
@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean cadastrar(UserDTO userDTO) {
        try {

            if (userDTO.getId() != null) {
                throw new UserException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }
            if (this.userRepository.findById(userDTO.getId()) != null) {
                throw new UserException(MensagensConstant.ERRO_USER_CADASTRADO_ANTERIORMENTE.getValor(), HttpStatus.BAD_REQUEST);
            }
            return this.cadastrar(userDTO);

        }catch (UserException c) {
            throw c;
        }
        catch (Exception e) {
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
        return null;
    }

    //@CachePut(unless = "#result.size()<3")
    @Override
    public List<UserEntity> listar() {
        try {
            return this.userRepository.findAll();
        }catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
