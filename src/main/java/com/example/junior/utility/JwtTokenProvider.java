package com.example.junior.utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import java.util.stream.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import static java.util.Arrays.*;
import static com.example.junior.constant.SecurityConstant.*;
import com.example.junior.domain.UserPrincipal;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;
	
	public String generateJwtToken(UserPrincipal principal) {
	 String[] claims = getClaimsFromUser(principal);
		
		return JWT.create()
				.withIssuer(GET_ARRAYS_LLC)
				.withAudience(GET_ARRAYS_ADMINISTRATION)
				.withIssuedAt(new Date())
				.withSubject(principal.getUsername())
				.withArrayClaim(AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}
	
	public List<GrantedAuthority> getAuthorities(String token){
		String[] claims = getClaimsFromToken(token);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}
	
	public Authentication getAuthentitication(String username, List<GrantedAuthority> authorities, HttpServletRequest request) {
	
		UsernamePasswordAuthenticationToken userPassAutToken =
				new UsernamePasswordAuthenticationToken(username, null,authorities);
		userPassAutToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return userPassAutToken;
	}
	
	public boolean isTokenValid(String username, String token) {
		JWTVerifier verifier = getFVTVerifier();
		
		return StringUtils.isNoneEmpty(username) && !isTokenExpired(verifier, token);
		
	}
	
	public String getSubject(String token) {
		JWTVerifier verifier = getFVTVerifier();
		return verifier.verify(token).getSubject();
	}

	private boolean isTokenExpired(JWTVerifier verifier, String token) {
		Date expiration = verifier.verify(token).getExpiresAt();
		return expiration.before(new Date());
	}

	private String[] getClaimsFromToken(String token) {
		JWTVerifier verifier = getFVTVerifier();
		
		return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getFVTVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm =Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException("TOKEN CAN'T BE VERIFIED");
		}
		
		return verifier;
	}

	private String[] getClaimsFromUser(UserPrincipal principal) {
		
		List<String> authorities = new ArrayList<>();
		
		for (GrantedAuthority authority : principal.getAuthorities()) {
			authorities.add(authority.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}
}
