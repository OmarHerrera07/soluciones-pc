package com.solucionespc.pagos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxTrigger;

@Controller
@RequestMapping("/clientes")
public class ClienteController {
	
	@Autowired
	private IClienteService clienteService;
	
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
    public String registrarCliente(ClienteRegisterDTO cliente 
    		) {
    	clienteService.registrarCliente(cliente);
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el usario</div>";
    }
	
    /**
     * Obtiene el cuerpo del formulario de registrar usuario
     *
     * @return  fragmento de thymeleaf con el cuerpo del formulario
     */
    @GetMapping("/get-form-registrar")
    public String getRegistrarClieneteForm() {
        return "fragments/clientes/registro-cliente :: registrar-cliente-form";
    }
    
    @GetMapping("/get-consulta-cliente")
    public String getEditarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Cliente cliente = clienteService.finById(id);
    	model.addAttribute("cliente", cliente);
    	System.out.println(cliente);
        return "fragments/clientes/consultar-cliente :: datos-consultar-cliente";
    }
    
    @ResponseBody
    @GetMapping("/paginacion")
    public Page<ClienteDTO> paginacion(Pageable pageable,
    		@RequestParam(name = "nombre", required = false) String nombre,
    		@RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
    	pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        return clienteService.paginacionCliente(nombre,pageable);
    }
    
    
}
