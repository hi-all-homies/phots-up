package main.websocket;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.server.reactive.AbstractServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.adapter.NettyWebSocketSessionSupport;
import org.springframework.web.reactive.socket.adapter.ReactorNettyWebSocketSession;
import org.springframework.web.reactive.socket.server.RequestUpgradeStrategy;
import org.springframework.web.server.ServerWebExchange;
import main.security.TokenProvider;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.server.WebsocketServerSpec;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

@Component
public class CustomReqUpgradeStrategy implements RequestUpgradeStrategy {
	@Value(value = "${token.param.name}")
	private String JWT_PARAM_NAME;
	private final TokenProvider tokenProvider;

	public CustomReqUpgradeStrategy(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public Mono<Void> upgrade(
			ServerWebExchange exchange,
			WebSocketHandler webSocketHandler,
			@Nullable String subProtocol,
			Supplier<HandshakeInfo> handshakeInfoFactory) {
		
		ServerHttpResponse response = exchange.getResponse();
        HttpServerResponse reactorResponse = getNativeResponse(response);
        HandshakeInfo handshakeInfo = handshakeInfoFactory.get();
        NettyDataBufferFactory bufferFactory = (NettyDataBufferFactory) response.bufferFactory();
        
        var token = exchange.getRequest().getQueryParams().getFirst(JWT_PARAM_NAME);
        var username = tokenProvider.getUsernameFromToken(token);
        handshakeInfo.getHeaders().add("username", username);
        
        var spec = WebsocketServerSpec.builder()
            	.maxFramePayloadLength(NettyWebSocketSessionSupport.DEFAULT_FRAME_MAX_SIZE)
            	.protocols(subProtocol)
            	.build();
        
        return reactorResponse.sendWebsocket(
        		handlerFunction(handshakeInfo, bufferFactory, webSocketHandler), spec);
	}
	
	private BiFunction<WebsocketInbound, WebsocketOutbound, Mono<Void>> handlerFunction(
    		HandshakeInfo info,
    		NettyDataBufferFactory buffer,
    		WebSocketHandler handler) {
    	return (in, out) -> {
    		var session =
        			new ReactorNettyWebSocketSession(in, out, info, buffer);
    		return handler.handle(session);
    	};
    }
	
	private static HttpServerResponse getNativeResponse(ServerHttpResponse response) {
        if (response instanceof AbstractServerHttpResponse) {
            return ((AbstractServerHttpResponse) response).getNativeResponse();
        } else if (response instanceof ServerHttpResponseDecorator) {
            return getNativeResponse(((ServerHttpResponseDecorator) response).getDelegate());
        } else {
            throw new IllegalArgumentException("Couldn't find native response in " + response.getClass()
                                                                                             .getName());
        }
    }
}
