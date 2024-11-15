package com.example.todolist.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.todolist.Models.Usuario;
import com.example.todolist.Repository.UsuarioRepository;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método que trata o cadastro via API (POST para "/cadastrar")
    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) {
        // Verifica se o e-mail já está cadastrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmailAndNome(usuario.getEmail(), usuario.getNome());

        if (usuarioExistente.isPresent()) {
            // Retorna um erro de conflito (409) se o e-mail já existir
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("E-mail já cadastrado");
        }

        // Caso o e-mail não exista, salva o novo usuário
        usuarioRepository.save(usuario);

        // Retorna uma resposta de sucesso (201)
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso");
    }

    // Método para mostrar o formulário de cadastro no frontend
    @GetMapping("/usuarios/cadastro")
public String mostrarFormularioCadastro(Model model) {
    List<Usuario> usuarios = usuarioRepository.findAll(); // Busca todos os usuários do banco
    model.addAttribute("usuarios", usuarios); // Passa a lista de usuários para o template
    model.addAttribute("usuario", new Usuario()); // Passa um novo objeto Usuario para o formulário
    return "cadastro-usuario"; // Retorna o nome da página HTML
}


    // Método que trata o envio do formulário de cadastro (POST para "/usuarios/cadastro")
    @PostMapping("/usuarios/cadastro")
    public String cadastrarUsuarioFormulario(Usuario usuario) {
        // Verifica se o e-mail já está cadastrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmailAndNome(usuario.getEmail(), usuario.getNome());

        if (usuarioExistente.isPresent()) {
            // Adiciona um atributo para mensagem de erro
            return "redirect:/usuarios/cadastro?error=E-mail já cadastrado";
        }

        // Caso o e-mail não exista, salva o novo usuário
        usuarioRepository.save(usuario);

        // Redireciona para a página de sucesso
        return "redirect:/usuarios/cadastro?success";
    }
}
