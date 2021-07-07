package main.security;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class CustomContextRepo implements ServerSecurityContextRepository{
	private final TokenProvider tokenProvider;
	

	@Override
	public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest().getCookies().getFirst("token"))
				.map(cookie -> cookie.getValue())
				.filter(token -> token.startsWith(tokenProvider.getPrefix()))
				.map(tokenProvider::verifyToken)
				.onErrorResume(err -> Mono.empty())
				.map(SecurityContextImpl::new);
	}
}
