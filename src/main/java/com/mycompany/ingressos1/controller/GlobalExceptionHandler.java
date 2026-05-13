package com.mycompany.ingressos1.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("erro", "Erro: " + e.getMessage());
        model.addAttribute("detalhes", e.getClass().getSimpleName());
        return "erro";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(Exception e, Model model) {
        model.addAttribute("erro", "Página não encontrada");
        model.addAttribute("detalhes", "A página que você está procurando não existe.");
        return "erro";
    }
}
