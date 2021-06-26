package com.carsync.challenge.api.auth;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.carsync.challenge.api.exception.InvalidJwtAuthException;
import com.carsync.challenge.api.utils.AuthUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Accessors(prefix = "_")
@Getter
@Slf4j
@Service
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key}")
	private String _secretKey;

	@Value("${security.jwt.token.expire-length-in-ms:7200000}")
	private long _validityInMilliseconds;

	@Value("${security.jwt.token.header:Authorization}")
	private String _jwtHeader;

	@Value("${security.jwt.token.header.prefix:Bearer }")
	private String _headerPrefix;

	@PostConstruct
	protected void init() {
		_secretKey = Base64.getEncoder().encodeToString(getSecretKey().getBytes());
	}

	public String createToken(Long userId, String username) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put("userId", userId);

		Date now = new Date();
		Date expirationTime = new Date(now.getTime() + getValidityInMilliseconds());
		return Jwts.builder().setClaims(claims).setIssuedAt(now).setExpiration(expirationTime)
				.signWith(SignatureAlgorithm.HS256, getSecretKey()).compact();
	}

	public Authentication getAuthentication(String token) {

		String username = getUsername(token);
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		return new UsernamePasswordAuthenticationToken(username, "", authorities);
	}

	public String getUsername(String token) {
		return getClaimsFromToken(token).getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		return AuthUtils.extractTokenFromHeader(req.getHeader(getJwtHeader()));
	}

	public Long getUserId(String token) {
		Claims claims = getClaimsFromToken(token);
		// recode this
		Long userId = Long.parseLong(claims.get("userId").toString());
		return userId;
	}

	public boolean validateToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
				return false;
			}
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature!");
			throw new InvalidJwtAuthException("Invalid JWT signature!");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token!");
			throw new InvalidJwtAuthException("Invalid JWT token!");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token!");
			throw new InvalidJwtAuthException("Expired JWT token!");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token!");
			throw new InvalidJwtAuthException("Unsupported JWT token!");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty!");
			throw new InvalidJwtAuthException("JWT claims string is empty!");
		}
	}

	private Claims getClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(getSecretKey()).parseClaimsJws(token).getBody();
	}
}