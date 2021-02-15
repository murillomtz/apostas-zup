package com.mtz.apostaszup.controller.test;


import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ESTA PARA ESSA CLASSE INICIAR CORRETAMETNE VOCE DEVE COMENTAR OS ITENS DE INICIALIZAÇÃO DO SISTEMA
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
public class UserControllerIntegradTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IApostaRepository apostaRepository;

    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    public void init() {
        this.montaUserBaseDeDados();
    }

    @AfterEach
    public void finish() {
        this.apostaRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    private void montaUserBaseDeDados() {

        UserEntity u1 = new UserEntity();
        u1.setNome("USER TESTE");
        u1.setEmail("userteste@email.com");

        ApostaEntity a1 = new ApostaEntity();
        a1.setUser(u1);
        u1.getApostas().add(a1);

        ApostaEntity a2 = new ApostaEntity();
        a2.setUser(u1);
        u1.getApostas().add(a2);


        ApostaEntity a3 = new ApostaEntity();
        a3.setUser(u1);
        u1.getApostas().add(a3);


        this.userRepository.save(u1);
        this.apostaRepository.saveAll(Arrays.asList(a1, a2, a3));

    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/user/" + urn;
    }


    /*
     *
     * **********************CENÁRIOS DE SUCESSO************************
     *
     */

    @Test
    public void testCadastrarUser() {

        UserDTO user1 = new UserDTO();
        user1.setNome("User Cadastrado Test");
        user1.setEmail("usercadastroteste@email.com");


        HttpEntity<UserDTO> request = new HttpEntity<>(user1);

        ResponseEntity<Response<Boolean>> user = restTemplate.exchange(this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });

        List<UserEntity> listApostaAtualizado = this.userRepository.findAll();

        assertTrue(user.getBody().getData());
        assertEquals(2, listApostaAtualizado.size());
        assertEquals(201, user.getBody().getStatusCode());
    }

    @Test
    public void testListarUser() {

        ResponseEntity<Response<List<UserEntity>>> users = restTemplate.exchange(this.montaUri(""), HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<List<UserEntity>>>() {
                });

        assertNotNull(users.getBody().getData());
        assertEquals(1, users.getBody().getData().size());
        assertEquals(200, users.getBody().getStatusCode());
    }

    @Test
    public void testConsultarUserPorEmail() {

        List<UserEntity> userList = this.userRepository.findAll();
        String email = userList.get(0).getEmail();
        System.out.println("EMAIL ########" + email);
        ResponseEntity<Response<UserEntity>> user = restTemplate.
                exchange("http://localhost:" + this.port + "/user/e-mail/"+ email, HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<UserEntity>>() {
                });
        System.out.println("USER ########" + user);
        System.out.println("DATA ########" + user.getBody().getData());

        assertNotNull(user.getBody().getData());
        assertEquals("userteste@email.com", user.getBody().getData().getId());
        assertEquals(200, user.getBody().getStatusCode());


    }




}
