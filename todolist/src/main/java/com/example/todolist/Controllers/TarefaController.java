package com.example.todolist.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.todolist.Models.Tarefa;
import com.example.todolist.Models.Usuario;
import com.example.todolist.Repository.TarefaRepository;
import com.example.todolist.Repository.UsuarioRepository;

import java.util.List;

@Controller
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Exibe o formulário para cadastrar uma nova tarefa
    @GetMapping("/tarefas/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll(); // Obtém a lista de usuários
        model.addAttribute("usuarios", usuarios); // Passa os usuários para o Thymeleaf
        model.addAttribute("tarefa", new Tarefa()); // Passa a tarefa em branco para o formulário

        // Passa as prioridades para o Thymeleaf
        model.addAttribute("prioridades", Tarefa.Prioridade.values());

        return "cadastro-tarefa"; // Nome da página de cadastro
    }

    // Processa o formulário de cadastro de tarefa
    @PostMapping("/tarefas/cadastro")
    public String cadastrarTarefa(Tarefa tarefa) {
        if (tarefa.getDescricao() == null || tarefa.getDescricao().isEmpty()) {
            return "redirect:/tarefas/cadastro?error=descricao-obrigatoria";
        }

        if (tarefa.getPrioridade() == null) {
            return "redirect:/tarefas/cadastro?error=prioridade-obrigatoria";
        }

        // Salva a nova tarefa no banco de dados
        tarefaRepository.save(tarefa);

        // Redireciona para a página de gerenciamento de tarefas com um parâmetro de
        // sucesso
        return "redirect:/tarefas?success";
    }

    // Exibe todas as tarefas divididas por status
    @GetMapping("/tarefas")
    public String listarTarefas(Model model) {
        // Buscar tarefas de cada status
        List<Tarefa> tarefasAFazer = tarefaRepository.findByStatus(Tarefa.Status.fazer);
        List<Tarefa> tarefasFazendo = tarefaRepository.findByStatus(Tarefa.Status.fazendo);
        List<Tarefa> tarefasPronto = tarefaRepository.findByStatus(Tarefa.Status.pronto);

        // Buscar usuários cadastrados
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Adicionar as listas no modelo
        model.addAttribute("tarefasAFazer", tarefasAFazer);
        model.addAttribute("tarefasFazendo", tarefasFazendo);
        model.addAttribute("tarefasPronto", tarefasPronto);
        model.addAttribute("usuarios", usuarios); // Adicionar usuários ao modelo

        return "gerenciamento-tarefas"; // O nome do seu template HTML
    }

    @PostMapping("/tarefas/mudar-status")
    public String mudarStatus(@RequestParam Long id, @RequestParam String novoStatus) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);

        if (tarefa != null) {
            // Converte a string para o enum Tarefa.Status
            try {
                Tarefa.Status status = Tarefa.Status.valueOf(novoStatus.toUpperCase());
                tarefa.setStatus(status);
                tarefaRepository.save(tarefa); // Salva a tarefa com o novo status
            } catch (IllegalArgumentException e) {
                // Retorna um erro caso o status seja inválido
                return "redirect:/tarefas?error=status-invalido";
            }
        }

        return "redirect:/tarefas"; // Redireciona de volta para a lista de tarefas
    }

}
