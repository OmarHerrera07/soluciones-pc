package com.solucionespc.pagos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicios")
public class ServicioController {
    @GetMapping
    public String servicios() {
        return "servicios";
    }

    @GetMapping("/hojas")
    public String hojas() {
        return "hojas";
    }

    @GetMapping("/administrar-hoja")
    public String adminHojas() {
        return "admin-hojas";
    }
}
