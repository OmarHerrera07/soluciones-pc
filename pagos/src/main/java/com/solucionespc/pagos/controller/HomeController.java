package com.solucionespc.pagos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.solucionespc.pagos.service.IColoniaService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	/**
	 * Redirige a la vista principal del sistema
	 * @return	la vista principal
	 */
	@Autowired
	private IColoniaService coloniaService;

    @GetMapping()
    public String home(Model model){
    	model.addAttribute("colonias", coloniaService.findAll());
        return "index";
    }
}
