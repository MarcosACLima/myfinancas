package com.dlima.myfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlima.myfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	// query method
//	Optional<Usuario> findByEmail(String email);
	boolean existsByEmail(String email);
	
}