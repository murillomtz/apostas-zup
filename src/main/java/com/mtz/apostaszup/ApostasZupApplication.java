package com.mtz.apostaszup;

import com.mtz.apostaszup.dto.UserDTO;
import com.mtz.apostaszup.entity.ApostaEntity;
import com.mtz.apostaszup.entity.UserEntity;
import com.mtz.apostaszup.repository.IApostaRepository;
import com.mtz.apostaszup.repository.IUserRepository;
import org.apache.tomcat.jni.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@EnableCaching
@SpringBootApplication
public class ApostasZupApplication extends SpringBootServletInitializer implements CommandLineRunner {
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IApostaRepository apostaRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ApostasZupApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ApostasZupApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		UserEntity user1 = new UserEntity();
		user1.setNome("Murillo");
		user1.setEmail("teste@email.com");

		ApostaEntity aposta = new ApostaEntity(null, user1);

		String data = "23-03-1994";
		LocalDate ld = LocalDate.parse(data, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		ApostaEntity aposta2 = new ApostaEntity(null, user1);
		aposta2.setData(ld);
		user1.getApostas().addAll(Arrays.asList(aposta, aposta2));

		/**/



		UserEntity user2 = new UserEntity();
		user2.setNome("Orange Talent");
		user2.setEmail("orange@email.com");

		ApostaEntity aposta3 = new ApostaEntity(null, user2);
		user2.getApostas().add(aposta3);



		/*userRepository.save(user1);
		apostaRepository.save(aposta);
		apostaRepository.save(aposta2);
		userRepository.save(user2);
		apostaRepository.save(aposta3);*/




	}
}
