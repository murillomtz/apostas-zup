package com.mtz.apostaszup.controller.test;


import com.mtz.apostaszup.dto.ApostaDTO;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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
public class ApostaControllerIntegradTest {


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
        this.montaApostaBaseDeDados();
    }

    @AfterEach
    public void finish() {
        this.apostaRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    private String montaUri(String urn) {
        return "http://localhost:" + this.port + "/aposta/" + urn;
    }


    private void montaApostaBaseDeDados() {

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

    /*
     *
     * **********************CENÁRIOS DE SUCESSO************************
     *
     */

    @Test
    public void testCadastrarAposta() {

        ApostaDTO aposta1 = new ApostaDTO();
        aposta1.setUser("userteste@email.com");


        HttpEntity<ApostaDTO> request = new HttpEntity<>(aposta1);

        ResponseEntity<Response<Boolean>> aposta = restTemplate.exchange(this.montaUri(""), HttpMethod.POST, request,
                new ParameterizedTypeReference<Response<Boolean>>() {
                });

        List<ApostaEntity> listApostaAtualizado = this.apostaRepository.findAll();

        assertTrue(aposta.getBody().getData());
        assertEquals(4, listApostaAtualizado.size());
        assertEquals(201, aposta.getBody().getStatusCode());
    }

    @Test
    public void testListarAposta() {

        ResponseEntity<Response<List<ApostaDTO>>> cursos = restTemplate.exchange(this.montaUri(""), HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<List<ApostaDTO>>>() {
                });
        assertNotNull(cursos.getBody().getData());
        assertEquals(3, cursos.getBody().getData().size());
        assertEquals(200, cursos.getBody().getStatusCode());
    }

    @Test
    public void testConsultarApostaPorId() {

        List<ApostaEntity> apostaList = this.apostaRepository.findAll();
        Long id = apostaList.get(0).getId();

        ResponseEntity<Response<ApostaDTO>> aposta = restTemplate.exchange(this.montaUri("/" + id), HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<ApostaDTO>>() {
                });

        assertNotNull(aposta.getBody().getData());
        assertEquals(id, aposta.getBody().getData().getId());
        assertEquals(200, aposta.getBody().getStatusCode());

    }

    @Test
    public void testExcluirApostaPorId() {

        List<ApostaEntity> materiaList = this.apostaRepository.findAll();
        Long id = materiaList.get(0).getId();

        ResponseEntity<Response<Boolean>> aposta = restTemplate.exchange(this.montaUri(id.toString()), HttpMethod.DELETE,
                null, new ParameterizedTypeReference<Response<Boolean>>() {
                });

        List<ApostaEntity> listCursoAtualizado = this.apostaRepository.findAll();

        assertTrue(aposta.getBody().getData());
        assertEquals(2, listCursoAtualizado.size());
        assertEquals(200, aposta.getBody().getStatusCode());
    }


    @Test
    public void testlistarApostaPorEmail() {

        List<ApostaEntity> userList = this.apostaRepository.findAll();
        String email = userList.get(0).getUser().getEmail();

        URI uri = UriComponentsBuilder.fromHttpUrl(montaUri("/e-mail")).path("/listarporemail")
                .queryParam("email", email).build().toUri();


        ResponseEntity<Response<List<ApostaDTO>>> cursos = restTemplate.exchange(uri, HttpMethod.GET,
                null, new ParameterizedTypeReference<Response<List<ApostaDTO>>>() {
                });

        assertNotNull(cursos.getBody().getData());
        assertEquals(3, cursos.getBody().getData().size());
        assertEquals(200, cursos.getBody().getStatusCode());


    }

}