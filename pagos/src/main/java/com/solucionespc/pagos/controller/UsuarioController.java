package com.solucionespc.pagos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.service.IUsuarioService;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping
    public String user(){
        return "users";
    }
    
    @GetMapping("/get")
    @ResponseBody
    public List<Usuario> getUsers(){
        return usuarioService.findAll();
    }
    
    @GetMapping("/find")
    @ResponseBody
    public Usuario find(){
        return usuarioService.finUserByUsername("fausto");
    }
}
