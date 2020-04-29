package com.dlima.myfinancas.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dlima.myfinancas.api.dto.LancamentoDTO;
import com.dlima.myfinancas.exception.RegraNegocioException;
import com.dlima.myfinancas.model.entity.Lancamento;
import com.dlima.myfinancas.model.entity.Usuario;
import com.dlima.myfinancas.model.enums.StatusLancamento;
import com.dlima.myfinancas.model.enums.TipoLancamento;
import com.dlima.myfinancas.service.LancamentoService;
import com.dlima.myfinancas.service.UsuarioService;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {
	
	private LancamentoService service;
	
	private UsuarioService usuarioService;

	public LancamentoResource(LancamentoService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		return null;
	}
	
	/* Converter DTO em Lancamento */
	private Lancamento converter(LancamentoDTO dto) { 
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService.obterPorId(dto.getUsuario())
			.orElseThrow( () -> new RegraNegocioException(
				"Usuário não encontrado para o Id informado."));
		
		lancamento.setUsuario(usuario);
		
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		
		return lancamento;
	}
	
}
