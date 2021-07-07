package main.security;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import main.model.entities.User;

@Component
public class TokenProvider {
	@Value("${jwt.secret.word}")
	private String secret;
	@Value("${jwt.expiration.time}")
	private Long expirationTime;
	@Value("${jwt.prefix}")
	private String prefix;
	private SecretKey key;
	
	@PostConstruct
	public void initSecretKeyAndExpTime() {
		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		expirationTime = expirationTime*1000*60*60*24;
	}
	
	public String generateToken(UserDetails userDetails){
		var user = (User) userDetails;
		var userId = user.getId();
		var username = user.getUsername();
		var roles = user.getAuthorities();
		
		var token = Jwts.builder()
				.setSubject(username)
				.claim("userId", userId)
				.claim("roles", roles)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + this.expirationTime))
				.signWith(this.key)
				.compact();
		
		return this.prefix + token;
	}
	
	
	public Authentication verifyToken(String token) {
		Claims claims = extractClaims(token);
		var username = claims.getSubject();
		
		@SuppressWarnings("unchecked")
		var roles = (List<Map<String, String>>) claims.get("roles");
		Set<GrantedAuthority> authorities = roles.stream()
					.map(record -> new SimpleGrantedAuthority(record.get("authority")))
					.collect(Collectors.toSet());
		
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}
	
	public long getUserIdFromToken(String token) {
		var id = (int) extractClaims(token).get("userId");
		return id;
	}
	
	public String getUsernameFromToken(String token) {
		return extractClaims(token).getSubject();
	}
	
	private Claims extractClaims(String token) {
		token = token.replaceFirst(prefix, "");
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String getPrefix() {return prefix;}
}
