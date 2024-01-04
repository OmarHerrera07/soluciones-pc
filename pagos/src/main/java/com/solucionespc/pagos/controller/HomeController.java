package com.solucionespc.pagos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
	/**
	 * Redirige a la vista principal del sistema
	 * @return	la vista principal
	 */

    @GetMapping()
    public String home(){
        return "index";
    }
}
