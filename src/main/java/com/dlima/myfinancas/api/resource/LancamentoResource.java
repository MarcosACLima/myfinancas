package com.dlima.myfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		try {
			Lancamento lancamento = converter(dto);
			lancamento = service.salvar(lancamento);
			return new ResponseEntity(lancamento, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto ) {
		return service.obterPorId(id).map(entidade -> { // entidade encontrado no BD 
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entidade.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet( 
			() -> new ResponseEntity(
				"Lançamento não encontrado na base de dados.", 
				HttpStatus.BAD_REQUEST
		));
	}
	
	@DeleteMapping("id")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map( entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet( 
			() -> new ResponseEntity("Lançamento não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
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
