package com.solucionespc.pagos.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import com.itextpdf.text.DocumentException;
import com.solucionespc.pagos.dto.ClienteDTO;
import com.solucionespc.pagos.dto.ClienteRegisterDTO;
import com.solucionespc.pagos.dto.Meses;
import com.solucionespc.pagos.dto.MesesDTO;
import com.solucionespc.pagos.entity.Cliente;
import com.solucionespc.pagos.entity.Usuario;
import com.solucionespc.pagos.service.IClienteService;
import com.solucionespc.pagos.service.IColoniaService;
import com.solucionespc.pagos.service.IPaqueteService;
import com.solucionespc.pagos.service.IUsuarioService;

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
	
	@Autowired
	private IUsuarioService usuarioService;
	
    /**
     * Registrar un cliente nuevo
     * @param 	cliente		cliente a registrar
     * @return	Resultado sobre el registro del usuario
     */
    @PostMapping(value = "/registrar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String registrarCliente(ClienteRegisterDTO cliente) {   	
    	boolean res = clienteService.registrarCliente(cliente);
    	
    	if(res) {
    		return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el cliente</div>";
    	}
    	return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error en el registro del cliente</div>";
    }
    
    /**
     * Editar un usuario nuevo
     * @param	usuario a editar
     * @return	Resultado sobre el la edición del usuario
     */
    @PostMapping(value = "/editar",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String editarCliente(ClienteRegisterDTO cliente) {   	
    	boolean res = clienteService.editarCliente(cliente);
    	
    	if(res) {
    		return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el cliente</div>";
    	}
    	
    	return "<div id=\"result\" data-notify=\"2\" hidden>Ha ocurrido un error al editar al cliente</div>";
    }
	
    /**
     * Obtiene el cuerpo del formulario de registrar usuario
     * @param model				Proporcionar datos a la vista para que se muestren dinámicamente
     * @param authentication	Obtiene información sobre la autenticación actual
     * @return 					fragmento de thymeleaf con el cuerpo del formulario
     */
    @GetMapping("/get-form-registrar")
    public String getRegistrarClieneteForm(Model model, Authentication authentication) {
    	model.addAttribute("paquetes", paqueteService.findAll());
    	model.addAttribute("colonias", coloniaService.findAll());
    	System.out.println(authentication.getName());
        return "fragments/clientes/registro-cliente :: registrar-cliente-form";
    }
    /**
     * Obtiene el cuerpo del formulario de editar usuario
     * @param 	model	Proporcionar datos a la vista para que se muestren dinámicamente
     * @param	id 		del Cliente a consultar
     * @return  		fragmento de thymeleaf con el cuerpo del formulario
     */  
    @GetMapping("/get-consulta-cliente")
    public String getConsultarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Cliente cliente = clienteService.finById(id);
    	model.addAttribute("cliente", cliente);
        return "fragments/clientes/consultar-cliente :: datos-consultar-cliente";
    }
    
    /**
     * 
     * @param id		id del cliente a editar (Para obtener su información)
     * @param model		Proporcionar datos a la vista para que se muestren dinámicamente
     * @return			fragmento con el formulario para mostrar la información del cliente
     */
    @GetMapping("/get-editar-cliente")
    public String getEditarUsuarioForm(@RequestParam(value="id") Integer id, Model model) {
    	Cliente cliente = clienteService.finById(id);
    	model.addAttribute("cliente", cliente);
    	model.addAttribute("paquetes", paqueteService.findAll());
    	model.addAttribute("colonias", coloniaService.findAll());
    	System.out.println(cliente);
        return "fragments/clientes/editar-cliente :: editar-cliente-form";
    }
    
    /**
     * 
     * @param pageable clase para controlar la paginación y ordenamiento de los resultados.
     * @param nombre	nombre del cliente que es usado para el filtro de busqueda
     * @param size		tamaño de la pagina a mostrar
     * @return			clase con los resultados de la paginación (tamaño, clientes, etc)
     */
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
    
    @GetMapping("/pagos")
    public String pagosClientes(@RequestParam(value="id") Integer id,Model model){
    	
    	Date fechaPago = clienteService.obtenerFechaPago(id);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaPago);

        // Obtener el día del mes
        Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
    	System.out.println(fechaPago);
    	
    	int anioPago = calendar.get(Calendar.YEAR);
    	model.addAttribute("anio",anioPago);
    	model.addAttribute("cliente",clienteService.finById(id));
    	model.addAttribute("idCliente",id);
    	model.addAttribute("mesPago",fechaPago);
    	model.addAttribute("meses", clienteService.generarMeses2(diaDelMes));
    	model.addAttribute("mesesPagados",clienteService.obtenerMesesPagados(id));
        return "cliente-pagos";
    }
    
    @PostMapping(value = "/realizar-pago-masivo",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.TEXT_HTML_VALUE)
    @HxTrigger("refresh")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public String pagoMasivo(@RequestParam(value="idCliente") Integer idCliente,@RequestParam(value="meses") List<String> meses, Authentication authentication) throws IOException, DocumentException {   
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        
        List<Date> mesesPagar = new ArrayList<>();
        for (String fechaString : meses) {
            try {
                Date fecha = dateFormat.parse(fechaString);
                // Aquí puedes hacer algo con el objeto Date
                mesesPagar.add(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
                // Manejo de error en caso de que no se pueda parsear la fecha
            }
        }
        
        Usuario user = usuarioService.finUserByUsername(authentication.getName());
        Cliente cliente = clienteService.finById(idCliente);
        clienteService.pagoMasivo(meses,cliente, user.getIdUsuario());
        System.out.println(clienteService.finById(idCliente));
    	return "<div id=\"result\" data-notify=\"1\" hidden>Se ha registro el pago</div>";
    }
    
    @GetMapping("/meses")
    @ResponseBody
    public List<Date> meses(Model model){
    	
        return clienteService.generarMeses(14);
    }
    
    @GetMapping("/prueba")
    @ResponseBody
    public List<MesesDTO> meses2(Model model){
    	
        return clienteService.generarMeses2(2);
    }
    
    
    @GetMapping("/refrescar-meses")
    public String refrescarMeses(@RequestParam(value="idRefresh") Integer id,@RequestParam(value="anioRefresh") String anio,Model model){
    	
    	Date fechaPago = clienteService.obtenerFechaPago(id);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaPago);
        
        // Obtener el día del mes
        Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
    	System.out.println(fechaPago);
    	int anioPago = calendar.get(Calendar.YEAR);
    	model.addAttribute("anio",anioPago);
    	model.addAttribute("idCliente",id);
    	model.addAttribute("mesPago",fechaPago);	
    	model.addAttribute("meses", clienteService.generarMesesPorAnio(diaDelMes,Integer.parseInt(anio)));
        model.addAttribute("mesesPagados",clienteService.obtnerMesesPagadosFiltro(anio, id));
    	  	
        return "cliente-pagos :: lista-meses";
    }
    
    @GetMapping("/pagos-meses-filtro")
    public String mesesPagadosFiltro(@RequestParam(value="id") Integer id,@RequestParam(value="anio") String anio,Model model){
    	
    	Date fechaPago = clienteService.obtenerFechaPago(id);
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaPago);
        
        
        // Obtener el día del mes
        Integer diaDelMes = calendar.get(Calendar.DAY_OF_MONTH);
    	System.out.println(fechaPago);
    	int anioPago = calendar.get(Calendar.YEAR);
    	System.out.println("ANIOOOOOOOOOOOOOoo");
    	model.addAttribute("anio",anio);
    	model.addAttribute("idCliente",id);
    	model.addAttribute("mesPago",fechaPago);	
    	model.addAttribute("meses", clienteService.generarMesesPorAnio(diaDelMes,Integer.parseInt(anio)));
        model.addAttribute("mesesPagados",clienteService.obtnerMesesPagadosFiltro(anio, id));
    	  	
    	
        return "cliente-pagos :: lista-meses";
    }
}
