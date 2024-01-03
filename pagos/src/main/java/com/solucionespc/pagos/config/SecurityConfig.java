package com.solucionespc.pagos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.solucionespc.pagos.service.PcUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	 private final PcUserDetailsService pcUserDetailsService;

	    public SecurityConfig(PcUserDetailsService pcUserDetailsService) {
	        this.pcUserDetailsService = pcUserDetailsService;
	    }
	    
	    @Bean
	    public AuthenticationProvider daoAuthenticationProvider() {
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        provider.setPasswordEncoder(passwordEncoder());
	        provider.setUserDetailsService(pcUserDetailsService);

	        return provider;
	    }

	    /**
	     * Regresa la implementación escogida de PasswordEncoder, en este caso se
	     * escogió el algoritmo de argon2.
	     *
	     * @return el codificador de contraseñas
	     */
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        int saltLength = 32;
	        int hashLength = 32;
	        int parallelism = 1;
	        int memory = 4096;
	        int iterations = 3;

	        return new Argon2PasswordEncoder(
	                saltLength,
	                hashLength,
	                parallelism,
	                memory,
	                iterations
	        );
	    }


	    @Bean
	    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
	        return new MvcRequestMatcher.Builder(introspector);
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
	        http
	                .authorizeHttpRequests((authorize) -> authorize
	                        .requestMatchers(mvc.pattern("css/**"),mvc.pattern("js/**"),mvc.pattern("img/**"),mvc.pattern("bootstrap/**")).permitAll()
	                        .requestMatchers(mvc.pattern("/usuarios/**")).hasAnyRole("ADMINISTRADOR")
	                        //.requestMatchers(mvc.pattern("/solicitud/**")).hasAnyRole("SUBDIRECTOR","ADMINISTRADOR","COORDINADOR","POSGRADO")
	                        .anyRequest().authenticated()
	                )
	                .csrf(AbstractHttpConfigurer::disable);
	        http
	                .formLogin(form -> form
	                        .loginPage("/login")
	                        .loginProcessingUrl("/login")
	                        .defaultSuccessUrl("/", true)
	                        .permitAll()
	                );
	        http
	                .logout(logout -> logout
	                        .invalidateHttpSession(true).clearAuthentication(true)
	                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
	                        .permitAll()
	                );
	        return http.build();
	    }


}
