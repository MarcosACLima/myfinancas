package com.dlima.myfinancas.service.impl;

import org.springframework.stereotype.Service;

import com.dlima.myfinancas.exception.RegraNegocioException;
import com.dlima.myfinancas.model.entity.Usuario;
import com.dlima.myfinancas.model.repository.UsuarioRepository;
import com.dlima.myfinancas.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
//	@Autowired
	private UsuarioRepository repository;
	
//	@Autowired
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario salvarUsuario(Usuario usuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}
	
}
