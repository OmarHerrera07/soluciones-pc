package com.solucionespc.pagos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.ClienteRegisterDTO;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.service.IClienteService;
import com.solucionespc.pagos.service.IColoniaService;
import com.solucionespc.pagos.service.IPaqueteService;

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IPaqueteService paqueteService;
	
	@Autowired
	private IColoniaService coloniaService;
	
    /**
     * Registrar un usuario nuevo
     * @param usuario	Usuario a registrar
     * @return	Resultado sobre el registro del usuario
     */
    @PostMapping(value = "/registrar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String registrarCliente(ClienteRegisterDTO cliente) {   	
    	clienteService.registrarCliente(cliente);
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el usario</div>";
    }
    
    /**
     * Registrar un usuario nuevo
     * @param usuario	Usuario a registrar
     * @return	Resultado sobre el registro del usuario
     */
    @PostMapping(value = "/editar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String editarCliente(ClienteRegisterDTO cliente) {   	
    	clienteService.editarCliente(cliente);
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el usario</div>";
    }
	
    /**
     * Obtiene el cuerpo del formulario de registrar usuario
     *
     * @return  fragmento de thymeleaf con el cuerpo del formulario
     */
    @GetMapping("/get-form-registrar")
    public String getRegistrarClieneteForm(Model model, Authentication authentication) {
    	model.addAttribute("paquetes", paqueteService.findAll());
    	model.addAttribute("colonias", coloniaService.findAll());
    	System.out.println(authentication.getName());
        return "fragments/clientes/registro-cliente :: registrar-cliente-form";
    }
    
    @GetMapping("/get-consulta-cliente")
    public String getConsultarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Cliente cliente = clienteService.finById(id);
    	model.addAttribute("cliente", cliente);
        return "fragments/clientes/consultar-cliente :: datos-consultar-cliente";
    }
    
    @GetMapping("/get-editar-cliente")
    public String getEditarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Cliente cliente = clienteService.finById(id);
    	model.addAttribute("cliente", cliente);
    	model.addAttribute("paquetes", paqueteService.findAll());
    	model.addAttribute("colonias", coloniaService.findAll());
    	System.out.println(cliente);
        return "fragments/clientes/editar-cliente :: editar-cliente-form";
    }
    
    @ResponseBody
    @GetMapping("/paginacion")
    public Page<ClienteDTO> paginacion(Pageable pageable,
    		@RequestParam(name = "nombre", required = false) String nombre,
    		@RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
    	pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        return clienteService.paginacionCliente(nombre,pageable);
    }
    
    @PostMapping(value = "/realizar-pago",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String realizarPago(@RequestParam(value="id") Integer id, Authentication authentication) {   
    	if(clienteService.realizarPago(id,authentication.getName())) {
    		System.out.println("dsfiodsjfidosfdio");
    	}else {
    		System.out.println("Elrrorrrr");
    	}
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el pago</div>";
    }
    
}
