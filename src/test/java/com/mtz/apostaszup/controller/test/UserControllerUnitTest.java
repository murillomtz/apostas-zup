package com.mtz.apostaszup.controller.test;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.repository.IUserRepository;
import com.mtz.apostaszup.service.IApostaService;
import com.mtz.apostaszup.service.IUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(JUnitPlatform.class)
public class UserControllerUnitTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IUserService userService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static  UserDTO userDTO;

    private static UserEntity userEntity;

    @BeforeAll
    public static void init() {

        userEntity = new UserEntity();
        userEntity.setEmail("usercadastroteste@email.com");
        userEntity.setNome("User Teste");

        /*apostaDTO = new ApostaDTO();
        apostaDTO.setId(1L);
        apostaDTO.setUser("usercadastroteste@email.com");*/

        userDTO = new UserDTO();
        userDTO.setNome("User Teste");
        userDTO.setEmail("usercadastroteste@email.com");

    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/user/" + urn;
    }

    @Test
    public void testListarUser() {
        Mockito.when(this.userService.listar()).thenReturn(new ArrayList<UserDTO>());

        ResponseEntity<Response<List<UserEntity>>> user = restTemplate.exchange(
                this.montaUri(""), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<UserEntity>>>() {
                });
        assertNotNull(user.getBody().getData());
        assertEquals(200, user.getBody().getStatusCode());
    }


    @Test
    public void testCadastrarAposta() {


        Mockito.when(this.userService.cadastrar(userDTO)).thenReturn(Boolean.TRUE);

        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO);

        ResponseEntity<Response<Boolean>> users = restTemplate.exchange(
                this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });

        assertTrue(users.getBody().getData());
        assertEquals(201, users.getBody().getStatusCode());
    }
    @Test
    public void testConsultarUserEmail() {

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setNome("Test User Consulta");
        user.setEmail("testeconsulta@email.com");

        Mockito.when(this.userService.findByEmail("testeconsulta@email.com")).thenReturn(user);

        ResponseEntity<Response<UserEntity>> cursoResponse = restTemplate.exchange(
                this.montaUri("/e-mail/testeconsulta@email.com"), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<UserEntity>>() {
                });
        assertNotNull(cursoResponse.getBody().getData());
        assertEquals(200, cursoResponse.getBody().getStatusCode());
    }
}
