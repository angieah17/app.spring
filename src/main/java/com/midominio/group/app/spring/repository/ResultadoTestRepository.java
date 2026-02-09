package com.midominio.group.app.spring.repository;

import com.midominio.group.app.spring.entity.ResultadoTest;
import com.midominio.group.app.spring.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoTestRepository extends JpaRepository<ResultadoTest, Long> {
    List<ResultadoTest> findByUsuario(Usuario usuario);
    List<ResultadoTest> findByUsuarioAndTematica(Usuario usuario, String tematica);
}
