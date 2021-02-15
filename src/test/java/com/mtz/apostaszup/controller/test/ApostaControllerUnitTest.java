package com.mtz.apostaszup.controller.test;

import com.mtz.apostaszup.dto.ApostaDTO;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.model.Response;
import com.mtz.apostaszup.service.IApostaService;
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
public class ApostaControllerUnitTest {

    @LocalServerPort
    private int port;

    @MockBean
    private IApostaService apostaService;

    @Autowired
    private TestRestTemplate restTemplate;

    private static ApostaDTO apostaDTO;

    private static UserEntity userEntity;

    @BeforeAll
    public static void init() {

        /*userEntity = new UserEntity();
        userEntity.setEmail("usercadastroteste@email.com");
        userEntity.setNome("User Teste");*/

        apostaDTO = new ApostaDTO();
        apostaDTO.setId(1L);
        apostaDTO.setUser("usercadastroteste@email.com");


    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/aposta/" + urn;
    }

    @Test
    public void testListarAposta() {
        Mockito.when(this.apostaService.listar()).thenReturn(new ArrayList<ApostaDTO>());

        ResponseEntity<Response<List<ApostaDTO>>> aposta = restTemplate.exchange(
                this.montaUri(""), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<List<ApostaDTO>>>() {
                });
        assertNotNull(aposta.getBody().getData());
        assertEquals(200, aposta.getBody().getStatusCode());
    }

    @Test
    public void testConsultarAposta() {
        Mockito.when(this.apostaService.findById(1L)).thenReturn(apostaDTO);

        ResponseEntity<Response<ApostaDTO>> apostaResponse = restTemplate.exchange(
                this.montaUri("/1"), HttpMethod.GET, null,
                new ParameterizedTypeReference<Response<ApostaDTO>>() {
                });
        assertNotNull(apostaResponse.getBody().getData());
        assertEquals(200, apostaResponse.getBody().getStatusCode());
    }


    @Test
    public void testCadastrarAposta() {
        Mockito.when(this.apostaService.cadastrar(apostaDTO)).thenReturn(Boolean.TRUE);

        HttpEntity<ApostaDTO> request = new HttpEntity<>(apostaDTO);

        ResponseEntity<Response<Boolean>> apostas = restTemplate.exchange(
                this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });

        assertTrue(apostas.getBody().getData());
        assertEquals(201, apostas.getBody().getStatusCode());
    }


    @Test
    public void testExcluirAposta() {
        Mockito.when(this.apostaService.excluir(1L)).thenReturn(Boolean.TRUE);

        ResponseEntity<Response<Boolean>> apostas = restTemplate.exchange(
                this.montaUri("/1"), HttpMethod.DELETE, null,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });
        assertTrue(apostas.getBody().getData());
        assertEquals(200, apostas.getBody().getStatusCode());
    }
}
