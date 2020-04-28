package com.dlima.myfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
	
	@SpyBean // utiliza metodos originais
	UsuarioServiceImpl service;
	
	@MockBean // criar instancia fake
	UsuarioRepository repository;
	
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
	
	@Test(expected = Test.None.class)
	public void deveSalvarUmUsuario() {
		// cenario
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder()
				.id(1l)
				.nome("nome")
				.email("email@email.com")
				.senha("senha").build();
		Mockito.when(repository.save(Mockito.any(Usuario.class)))
				.thenReturn(usuario); // ao salvar qualquer usuario retorna o usuario builder
		
		// acao
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//verificacao
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		// cenario 
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class)
			.when(service).validarEmail(email);
		
		// acao
		service.salvarUsuario(usuario);
		
		// verificacao
		Mockito.verify(repository, Mockito.never()).save(usuario); // espera que nunca tenha sido chamado o metodo de salvar com este usuario
	}
	
}
