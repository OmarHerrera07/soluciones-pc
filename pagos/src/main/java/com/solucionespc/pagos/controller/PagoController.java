package com.solucionespc.pagos.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pagos")
public class PagoController {
    @GetMapping
    public String pagos(){
        return "pagos";
    }
}
