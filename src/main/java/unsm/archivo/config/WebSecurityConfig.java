package unsm.archivo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import unsm.archivo.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig
{
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final AuthenticationProvider authProvider;
	
	public WebSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authProvider) {
		super();
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.authProvider = authProvider;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
			return http
					.csrf(csrf -> csrf
							.disable())
					.authorizeHttpRequests(authRequest -> {
							authRequest.requestMatchers("/auth/**").permitAll();
							authRequest.requestMatchers
							("/documentos/**", "/tipocriterio/**","/tipodocumento/**","/usuario/**")
							.hasAuthority("ADMINISTRADOR");
							authRequest.anyRequest().authenticated();
							})
					
					.sessionManagement(sessionManager -> sessionManager
							.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
					.authenticationProvider(authProvider)
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.build();
	}
}
