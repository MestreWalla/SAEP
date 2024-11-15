package com.example.todolist.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


public class Routes {
    @GetMapping("/")
    public String mostrarFormularioCadastro(Model model) {
        return "gerenciamento-tarefas";
    }
}
