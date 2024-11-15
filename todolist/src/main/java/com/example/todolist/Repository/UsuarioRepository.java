package com.example.todolist.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.todolist.Models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email AND u.nome = :nome")
    Optional<Usuario> findByEmailAndNome(String email, String nome);

}
