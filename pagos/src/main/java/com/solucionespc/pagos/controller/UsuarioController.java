package com.solucionespc.pagos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.solucionespc.pagos.dto.UsuarioRegisterDTO;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.service.IUsuarioService;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;


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
    
    /**
     * Registrar un usuario nuevo
     * @param usuario	Usuario a registrar
     * @return	Resultado sobre el registro del usuario
     */
    @HxTrigger("refresh")
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
    @GetMapping("/get-form-registrar")
    public String getRegistrarUsuarioForm() {
    	
        return "fragments/usuarios/registro-usuario :: registrar-usuario-form";
    }
    
    /**
     * Obtiene el cuerpo del formulario de registrar usuario
     *
     * @return  fragmento de thymeleaf con el cuerpo del formulario
     */
    

    @GetMapping("/get-form-editar")
    public String getEditarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Usuario user = usuarioService.findUsuarioById(id);
    	model.addAttribute("user", user);
    	model.addAttribute("roles", usuarioService.findRoles());
        return "fragments/usuarios/editar-usuario :: editar-usuario-form";
    }
    
    @HxTrigger("refresh")
    @PostMapping(value = "/editar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String editarUsuario(UsuarioRegisterDTO usuario) {
    	System.out.println(usuario);
		if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
			usuario.setPassword(usuarioService.findById(usuario.getIdUsuario()).getPassword());
		} else {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		System.out.println(usuario);
    	 usuarioService.updateUser(usuario);
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha editado el usario</div>";
    }
    
    @GetMapping("/paginacion")
    @ResponseBody
    public Page<Usuario> obtenerUsuariosPaginados(
    		@RequestParam(name = "nombre", required = false) String nombre,
    		 @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
            Pageable pageable) {
    	pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        return usuarioService.paginacionUsuariosFiltro(nombre, pageable);
    }
    
}
