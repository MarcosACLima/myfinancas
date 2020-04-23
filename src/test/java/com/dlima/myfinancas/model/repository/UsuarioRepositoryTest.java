package com.dlima.myfinancas.model.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlima.myfinancas.model.entity.Usuario;

//@SpringBootTest // sobe todo contexto da aplicacao ao testar
@RunWith(SpringRunner.class)
@ActiveProfiles("test") // declarar que vai usar application-test
@DataJpaTest 
@AutoConfigureTestDatabase(replace = Replace.NONE) 
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager; 
	
	@Test
	public void deveVerificarExistenciaDeEmail() {
		// cenario
		Usuario usuario = Usuario.builder().nome("usuario").email("usuario@mail.com").build();
		entityManager.persist(usuario);
		
		// acao/ execucao
		boolean resultado = repository.existsByEmail("usuario@mail.com");
		
		// verificacao
		Assertions.assertThat(resultado).isTrue();
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComEmail() {
		// cenario
				
		// acao
		boolean resultado = repository.existsByEmail("usuario@gmail.com");
				
		// verificacao
		Assertions.assertThat(resultado).isFalse();
	}

}
