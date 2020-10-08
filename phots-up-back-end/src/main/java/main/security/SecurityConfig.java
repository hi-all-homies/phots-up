package main.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	private final ServerSecurityContextRepository contextRepo;	
	
	public SecurityConfig(ServerSecurityContextRepository contextRepo) {
		this.contextRepo = contextRepo;
	}

	@Bean
	public SecurityWebFilterChain chain(ServerHttpSecurity http) {
		return http.cors().and()
				.csrf().disable()
				.formLogin().disable()
				.logout().disable()
				.httpBasic().disable()
				.exceptionHandling().authenticationEntryPoint(entryPointFunction)
				.and()
				.securityContextRepository(contextRepo)
				.authorizeExchange(exchange ->
					exchange.pathMatchers(
							"/phots/up/api/login", "/phots/up/api/signup", "/phots/up/api/confirm/**").permitAll()
						.anyExchange().authenticated())
				.build();
	}
	
	private ServerAuthenticationEntryPoint entryPointFunction = (swExchange, ex) ->
		Mono.fromRunnable(() ->{
			swExchange.getResponse().setStatusCode(FORBIDDEN);
		});
}
