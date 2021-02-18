package com.mtz.apostaszup.service.implementeService;


import com.mtz.apostaszup.constant.MensagensConstant;
import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.exception.UserException;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import com.mtz.apostaszup.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "user")
@Service
public class UserService implements IUserService {


    private final IUserRepository userRepository;
    private final IApostaRepository apostaRepository;
    private final ModelMapper mapper;

    @Autowired
    public UserService(IUserRepository userRepository, IApostaRepository apostaRepository) {
        this.userRepository = userRepository;
        this.apostaRepository = apostaRepository;
        this.mapper = new ModelMapper();

    }

    @Override
    public Boolean cadastrar(UserDTO userDTO) {
        try {

            if (userDTO.getId() != null) {

                throw new UserException(MensagensConstant.ERRO_ID_INFORMADO.getValor(), HttpStatus.BAD_REQUEST);
            }

            if (this.userRepository.findByEmail(userDTO.getEmail()) != null) {

                throw new UserException(MensagensConstant.ERRO_USER_CADASTRADO_ANTERIORMENTE.getValor(), HttpStatus.BAD_REQUEST);
            }

            return this.cadastrarOuAtualizar(userDTO);

        } catch (UserException c) {

            throw c;
        } catch (Exception e) {

            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Boolean excluir(Long id) {
        try {
            if (this.userRepository.findById(id).isPresent()) {
                this.userRepository.deleteById(id);
                return Boolean.TRUE;
            }
            throw new UserException(MensagensConstant.ERRO_USER_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (UserException c) {
            throw c;
        } catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(key = "#id")
    @Override
    public UserDTO findById(Long id) {
        try {
            Optional<UserEntity> userOptional = this.userRepository.findById(id);
            if (userOptional.isPresent()) {
                return this.mapper.map(userOptional.get(), UserDTO.class);
            }
            throw new UserException(MensagensConstant.ERRO_USER_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);
        } catch (UserException c) {
            throw c;
        } catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(unless = "#result.size()<3")
    @Override
    public List<UserDTO> listar() {
        try {
            List<UserEntity> userEntityList = this.userRepository.findAll();

            List<UserDTO> userDTOList = new ArrayList<>();


            for (UserEntity user : userEntityList) {
                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setEmail(user.getEmail());
                userDTO.setNome(user.getNome());
                List<ApostaEntity> apostaEntityList = user.getApostas();
                List<Long> apostaLong = new ArrayList<>();

                for (ApostaEntity aposta : apostaEntityList) {
                    apostaLong.add(aposta.getId());
                }
                userDTO.setApostas(apostaLong);
                userDTOList.add(userDTO);
            }
            return userDTOList;
        } catch (Exception e) {
            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDTO findByEmail(String email) {

        try {
            UserEntity user = this.userRepository.findByEmail(email);

            if (user != null) {

                UserDTO userDTO = new UserDTO();
                userDTO.setId(user.getId());
                userDTO.setNome(user.getNome());
                userDTO.setEmail(user.getEmail());

                List<ApostaEntity> apostaEntityList = user.getApostas();
                List<Long> apostaLong = new ArrayList<>();

                for (ApostaEntity aposta : apostaEntityList) {
                    apostaLong.add(aposta.getId());
                }
                userDTO.setApostas(apostaLong);
                return userDTO;
            }
            throw new UserException(MensagensConstant.ERRO_USER_NAO_ENCONTRADO.getValor(), HttpStatus.NOT_FOUND);

        } catch (UserException c) {
            throw c;
        } catch (Exception e) {

            throw new UserException(MensagensConstant.ERRO_GENERICO.getValor(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean cadastrarOuAtualizar(UserDTO user) {
        List<ApostaEntity> listApostaEntity = new ArrayList<>();

        if (user.getApostas() != null && !user.getApostas().isEmpty()) {

            user.getApostas().forEach(aposta -> {
                if (this.apostaRepository.findById(aposta).isPresent())
                    listApostaEntity.add(this.apostaRepository.findById(aposta).get());
            });
        }
        UserEntity userEntity = new UserEntity();
        if (user.getId() != null) {
            userEntity.setId(user.getId());
        }
        userEntity.setEmail(user.getEmail());
        userEntity.setNome(user.getNome());
        userEntity.setApostas(listApostaEntity);

        this.userRepository.save(userEntity);
        return Boolean.TRUE;
    }
}
