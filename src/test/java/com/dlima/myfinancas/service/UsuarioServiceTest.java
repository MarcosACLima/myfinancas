package com.dlima.myfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dlima.myfinancas.exception.ErroAutenticacao;
import com.dlima.myfinancas.exception.RegraNegocioException;
import com.dlima.myfinancas.model.entity.Usuario;
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
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		// cenario
		String email = "email@email.com";
		String senha = "senha";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		// acao
		Usuario resultado = service.autenticar(email, senha);
		
		// verificacao
		Assertions.assertThat(resultado).isNotNull();
	}
	
	@Test()
	public void deverLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {
		// cenario
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		// acao
		Throwable excecao = Assertions.catchThrowable( 
				() -> service.autenticar("email@email.com", "senha")
		); // capturar excecao
		
		// verificacao
		Assertions.assertThat(excecao)
			.isInstanceOf(ErroAutenticacao.class)
			.hasMessage("Usuário não encontrado para o email informado.");
	} // resultado esperado na excecao
	
	@Test()
	public void deveLancarErroQuandoSenhaNaoBater() {
		// cenario
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));  // usuario encontrado
		
		// acao
		Throwable excecao = Assertions.catchThrowable( 
				() -> service.autenticar("email@email.com", "senhaDiferente")
		); 
		Assertions.assertThat(excecao).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}

}
