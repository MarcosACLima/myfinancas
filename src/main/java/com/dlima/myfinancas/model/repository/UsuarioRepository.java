package com.dlima.myfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dlima.myfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}