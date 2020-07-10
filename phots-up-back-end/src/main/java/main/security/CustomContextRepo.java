package main.security;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomContextRepo implements ServerSecurityContextRepository{
	private final TokenProvider tokenProvider;
	
	public CustomContextRepo(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		return Mono.justOrEmpty(
						exchange.getRequest().getHeaders().getFirst(AUTHORIZATION))
				.switchIfEmpty(
						Mono.justOrEmpty(exchange.getRequest().getQueryParams().getFirst("jwt")))
				.filter(token -> token.startsWith(tokenProvider.getPrefix()))
				.map(tokenProvider::verifyToken)
				.onErrorResume(err -> Mono.empty())
				.map(SecurityContextImpl::new);
	}
}
