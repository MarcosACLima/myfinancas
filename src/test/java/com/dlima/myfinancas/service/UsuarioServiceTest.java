package com.dlima.myfinancas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlima.myfinancas.exception.RegraNegocioException;
import com.dlima.myfinancas.model.repository.UsuarioRepository;
import com.dlima.myfinancas.service.impl.UsuarioServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	UsuarioService service;
	
	@MockBean // criar instancia fake
	UsuarioRepository repository;
	
	@Before // Antes
	public void setUp() {
//		repository = Mockito.mock(UsuarioRepository.class); // instancia fake
		service = new UsuarioServiceImpl(repository); // injetar o mock dentro da classe testada
	}
	
	@Test(expected = Test.None.class) // nao espera excecao
	public void deveValidarEmail() {
		// cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		// acao
		service.validarEmail("email@email.com");
	}
	
	@Test(expected = RegraNegocioException.class) // espera que lance excecao
	public void deverLancarErroAoValidarEmailQuandoExistirEmailCadastro() {
		// cenario
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		// acao
		service.validarEmail("email@email.com");
	}

}
