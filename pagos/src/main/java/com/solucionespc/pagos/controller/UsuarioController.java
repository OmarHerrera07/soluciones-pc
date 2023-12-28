package com.solucionespc.pagos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.service.IUsuarioService;


@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
    @Autowired
    private IUsuarioService usuarioService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String user(){
        return "users";
    }
    
    @PostMapping(value = "/registrar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String registrarUsuario(UsuarioRegisterDTO usuario) {
    	
    	 String encodedPwd = passwordEncoder.encode(usuario.getPassword());
    	 
    	 usuario.setPassword(encodedPwd);
    	 
    	 usuarioService.registerUser(usuario);
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el usario</div>";
    }
    
    /**
     * Obtiene el cuerpo del formulario de registrar usuario
     *
     * @return  fragmento de thymeleaf con el cuerpo del formulario
     */
    @GetMapping("/get-registrar-modal")
    public String getRegistrarUsuarioForm() {
        return "fragments/fragmentos-usuarios :: registrar-usuario-modal";
    }
    
    @GetMapping("/paginacion")
    @ResponseBody
    public Page<Usuario> obtenerUsuariosPaginados(
            Pageable pageable) {
    	pageable = PageRequest.of(pageable.getPageNumber(), 4, pageable.getSort());
        return usuarioService.paginacionUsuarios(pageable);
    }
    
}
