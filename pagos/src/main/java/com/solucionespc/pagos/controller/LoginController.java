package com.solucionespc.pagos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    /**
     * Regresa la vista para Iniciar sesión
     * *@return     vista para Login
     */
    @GetMapping("/login")
    String login() {
        return "login";
    }
}